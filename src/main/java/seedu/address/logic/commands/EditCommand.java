package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADD_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OWNER_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OWNER_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMOVE_TAG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.pet.Pet;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing owner in PetLog.
 * Copies the pet list.
 * Change pet list with delete or add pet instead.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "editowner";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the owner identified "
            + "by the owner index number used in the displayed owner list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: " + PREFIX_OWNER_INDEX + "OWNER_INDEX "
            + "[" + PREFIX_OWNER_NAME + "OWNER_NAME] "
            + "[" + PREFIX_PHONE + "PHONE_NUMBER] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_TAG + "OVERWRITE_TAG]..."
            + "[" + PREFIX_ADD_TAG + "ADD_TAG]..."
            + "[" + PREFIX_REMOVE_TAG + "REMOVE_TAG]...\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_OWNER_INDEX + "1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited owner: %1$s";
    public static final String MESSAGE_NOT_EDITED = "No fields provided to edit.";
    public static final String MESSAGE_DUPLICATE_PERSON = "Owner already exists.";
    public static final String MESSAGE_TAG_NOT_FOUND = "Tag not found for this owner: %1$s";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param index of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_OWNER_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());
        validateTagsToRemoveExist(personToEdit, editPersonDescriptor);
        Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);

        if (!personToEdit.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, Messages.format(editedPerson)));
    }

    /**
     * Validates that every requested tag to remove currently exists on the owner.
     */
    private static void validateTagsToRemoveExist(Person personToEdit, EditPersonDescriptor descriptor)
            throws CommandException {
        Optional<Set<Tag>> tagsToRemoveOptional = descriptor.getTagsToRemove();
        if (tagsToRemoveOptional.isEmpty()) {
            return;
        }

        Set<Tag> ownerTags = personToEdit.getTags();
        Optional<Tag> missingTag = tagsToRemoveOptional.get().stream()
                .filter(tag -> !ownerTags.contains(tag))
                .sorted((tag1, tag2) -> tag1.tagName.compareToIgnoreCase(tag2.tagName))
                .findFirst();

        if (missingTag.isPresent()) {
            throw new CommandException(String.format(MESSAGE_TAG_NOT_FOUND, missingTag.get().tagName));
        }
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());
        Address updatedAddress = editPersonDescriptor.getAddress().orElse(personToEdit.getAddress());
        Set<Pet> updatedPets = personToEdit.getPets();
        Set<Tag> updatedTags;

        if (editPersonDescriptor.getTags().isPresent()) {
            // full replacement: existing behaviour
            updatedTags = new HashSet<>(editPersonDescriptor.getTags().get());
        } else {
            // increment update
            updatedTags = new HashSet<>(personToEdit.getTags());
            editPersonDescriptor.getTagsToAdd().ifPresent(updatedTags::addAll);
            editPersonDescriptor.getTagsToRemove().ifPresent(updatedTags::removeAll);
        }

        return new Person(updatedName, updatedPhone, updatedEmail, updatedAddress, updatedTags, updatedPets);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        EditCommand otherEditCommand = (EditCommand) other;
        return index.equals(otherEditCommand.index)
                && editPersonDescriptor.equals(otherEditCommand.editPersonDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editPersonDescriptor", editPersonDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Address address;
        private Set<Tag> tags;
        private Set<Pet> pets;
        private Set<Tag> tagsToAdd;
        private Set<Tag> tagsToRemove;

        public EditPersonDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setTags(toCopy.tags);
            setPets(toCopy.pets);
            setTagsToAdd(toCopy.tagsToAdd);
            setTagsToRemove(toCopy.tagsToRemove);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, address, tags,
                    pets, tagsToAdd, tagsToRemove);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        /**
         * Sets {@code pets} to this object's {@code pets}.
         * A defensive copy of {@code pets} is used internally.
         */
        public void setPets(Set<Pet> pets) {
            this.pets = (pets != null) ? new LinkedHashSet<>(pets) : null;
        }

        /**
         * Returns an unmodifiable pet set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code pets} is null.
         */
        public Optional<Set<Pet>> getPets() {
            return (pets != null) ? Optional.of(Collections.unmodifiableSet(pets)) : Optional.empty();
        }

        /**
         * Sets the tags to be added, replaces many existing tagsToAdd with a copy of provided set.
         *
         * @param tags A set of tags to be added, or {@code null} if none are to be added.
         */
        public void setTagsToAdd(Set<Tag> tags) {
            this.tagsToAdd = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns and unmodifiable view of the tags to be added (if present).
         *
         * @return An {@code Optional} containing the set of tags to add,
         *         or {@code Optional.empty()} if no tags set.
         */
        public Optional<Set<Tag>> getTagsToAdd() {
            return (tagsToAdd != null) ? Optional.of(Collections.unmodifiableSet(tagsToAdd)) : Optional.empty();
        }

        /**
         * Sets tags to be removed, replaces any existing tagsToRemove with copy of provided set.
         *
         * @param tags A set of tags to remove, or {@code null} if no tags are to be removed.
         */
        public void setTagsToRemove(Set<Tag> tags) {
            this.tagsToRemove = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable view of tags to be removed (if present).
         *
         * @return An {@code Optional} containing set of tags to remove,
         *         or {@code Optional.empty()} if no tags set.
         */
        public Optional<Set<Tag>> getTagsToRemove() {
            return (tagsToRemove != null) ? Optional.of(Collections.unmodifiableSet(tagsToRemove)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            EditPersonDescriptor otherEditPersonDescriptor = (EditPersonDescriptor) other;
            return Objects.equals(name, otherEditPersonDescriptor.name)
                    && Objects.equals(phone, otherEditPersonDescriptor.phone)
                    && Objects.equals(email, otherEditPersonDescriptor.email)
                    && Objects.equals(address, otherEditPersonDescriptor.address)
                    && Objects.equals(tags, otherEditPersonDescriptor.tags)
                    && Objects.equals(pets, otherEditPersonDescriptor.pets)
                    && Objects.equals(tagsToAdd, otherEditPersonDescriptor.tagsToAdd)
                    && Objects.equals(tagsToRemove, otherEditPersonDescriptor.tagsToRemove);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("phone", phone)
                    .add("email", email)
                    .add("address", address)
                    .add("tags", tags)
                    .add("pets", pets)
                    .add("tagsToAdd", tagsToAdd)
                    .add("tagsToRemove", tagsToRemove)
                    .toString();
        }
    }
}
