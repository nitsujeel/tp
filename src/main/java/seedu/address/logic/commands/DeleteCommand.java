package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OWNER_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET_INDEX;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.pet.Pet;

/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person identified by the index number used in the displayed person list.\n"
            + "Parameters: " + PREFIX_OWNER_INDEX + "OWNER_INDEX "
            + "[" + PREFIX_PET_INDEX + "PET_INDEX]\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_OWNER_INDEX + "1 "
            + PREFIX_PET_INDEX + "1";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";
    public static final String MESSAGE_DELETE_PET_SUCCESS = "Deleted Pet: %1$s";
    public static final String MESSAGE_INVALID_PET_DISPLAYED_INDEX = "The pet index provided is invalid";

    private final Index targetIndex;
    private final Optional<Index> petIndex;

    /**
     * Creates an DeleteCommand to delete the specified {@code Person}
     */
    public DeleteCommand(Index targetIndex) {
        this(targetIndex, null);
    }

    /**
     * Creates an DeleteCommand to delete the specified {@code Pet}
     */
    public DeleteCommand(Index targetIndex, Index petIndex) {
        requireNonNull(targetIndex);
        this.targetIndex = targetIndex;
        this.petIndex = Optional.ofNullable(petIndex);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();
        validateOwnerIndex(lastShownList);

        Person personToDelete = lastShownList.get(targetIndex.getZeroBased());
        if (petIndex.isEmpty()) {
            model.deletePerson(personToDelete);
            return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(personToDelete)));
        }

        List<Pet> petList = personToDelete.getPetList();
        validatePetIndex(petList);

        Pet petToDelete = petList.get(petIndex.get().getZeroBased());
        Set<Pet> updatedPets = new LinkedHashSet<>(personToDelete.getPets());
        updatedPets.remove(petToDelete);

        Person editedPerson = new Person(personToDelete.getName(), personToDelete.getPhone(), personToDelete.getEmail(),
                personToDelete.getAddress(), personToDelete.getTags(), updatedPets);
        model.setPerson(personToDelete, editedPerson);
        return new CommandResult(String.format(MESSAGE_DELETE_PET_SUCCESS, petToDelete));
    }

    private void validateOwnerIndex(List<Person> lastShownList) throws CommandException {
        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
    }

    private void validatePetIndex(List<Pet> petList) throws CommandException {
        if (petIndex.get().getZeroBased() >= petList.size()) {
            throw new CommandException(MESSAGE_INVALID_PET_DISPLAYED_INDEX);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        return targetIndex.equals(otherDeleteCommand.targetIndex)
                && petIndex.equals(otherDeleteCommand.petIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("petIndex", petIndex)
                .toString();
    }
}
