package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OWNER_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SESSION_INDEX;

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
import seedu.address.model.session.Session;

/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes an owner, a pet under an owner, or a session under a pet.\n"
            + "Parameters: " + PREFIX_OWNER_INDEX + "OWNER_INDEX "
            + "[" + PREFIX_PET_INDEX + "PET_INDEX "
            + "[" + PREFIX_SESSION_INDEX + "SESSION_INDEX]]\n"
            + "Examples:\n"
            + COMMAND_WORD + " " + PREFIX_OWNER_INDEX + "1\n"
            + COMMAND_WORD + " " + PREFIX_OWNER_INDEX + "1 " + PREFIX_PET_INDEX + "1\n"
            + COMMAND_WORD + " " + PREFIX_OWNER_INDEX + "1 " + PREFIX_PET_INDEX + "1 "
            + PREFIX_SESSION_INDEX + "1";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted owner: %1$s";
    public static final String MESSAGE_DELETE_PET_SUCCESS = "Deleted pet: %1$s";
    public static final String MESSAGE_DELETE_SESSION_SUCCESS = "Deleted session: %1$s";
    public static final String MESSAGE_INVALID_PET_DISPLAYED_INDEX = Messages.MESSAGE_INVALID_PET_DISPLAYED_INDEX;
    public static final String MESSAGE_INVALID_SESSION_DISPLAYED_INDEX =
            Messages.MESSAGE_INVALID_SESSION_DISPLAYED_INDEX;

    private final Index targetIndex;
    private final Optional<Index> petIndex;
    private final Optional<Index> sessionIndex;

    /**
     * Creates an DeleteCommand to delete the specified {@code Person}
     */
    public DeleteCommand(Index targetIndex) {
        this(targetIndex, null, null);
    }

    /**
     * Creates an DeleteCommand to delete the specified {@code Pet}
     */
    public DeleteCommand(Index targetIndex, Index petIndex) {
        this(targetIndex, petIndex, null);
    }

    /**
     * Creates a DeleteCommand to delete the specified {@code Session}.
     */
    public DeleteCommand(Index targetIndex, Index petIndex, Index sessionIndex) {
        requireNonNull(targetIndex);
        this.targetIndex = targetIndex;
        this.petIndex = Optional.ofNullable(petIndex);
        this.sessionIndex = Optional.ofNullable(sessionIndex);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();
        validateOwnerIndex(lastShownList);

        Person personToDelete = lastShownList.get(targetIndex.getZeroBased());
        if (petIndex.isEmpty()) {
            model.deletePerson(personToDelete);
            model.updateDisplayedSessions(model.getFilteredPersonList());
            return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(personToDelete)));
        }

        List<Pet> petList = personToDelete.getPetList();
        validatePetIndex(petList);

        Pet selectedPet = petList.get(petIndex.get().getZeroBased());
        if (sessionIndex.isPresent()) {
            return deleteSession(model, selectedPet);
        }
        return deletePet(model, personToDelete, selectedPet);
    }

    private CommandResult deletePet(Model model, Person owner, Pet petToDelete) {
        Set<Pet> updatedPets = new LinkedHashSet<>(owner.getPets());
        updatedPets.remove(petToDelete);

        Person editedPerson = new Person(owner.getName(), owner.getPhone(), owner.getEmail(),
                owner.getAddress(), owner.getTags(), updatedPets);
        model.setPerson(owner, editedPerson);
        model.updateDisplayedSessions(model.getFilteredPersonList());
        return new CommandResult(String.format(MESSAGE_DELETE_PET_SUCCESS, petToDelete));
    }

    private CommandResult deleteSession(Model model, Pet pet) throws CommandException {
        List<Session> sessions = pet.getSessions();
        validateSessionIndex(sessions);

        int targetSessionZeroBased = sessionIndex.get().getZeroBased();
        Session sessionToDelete = sessions.get(targetSessionZeroBased);
        pet.removeSession(targetSessionZeroBased);
        model.updateDisplayedSessions(model.getFilteredPersonList());
        return new CommandResult(String.format(MESSAGE_DELETE_SESSION_SUCCESS, sessionToDelete));
    }

    private void validateOwnerIndex(List<Person> lastShownList) throws CommandException {
        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_OWNER_DISPLAYED_INDEX);
        }
    }

    private void validatePetIndex(List<Pet> petList) throws CommandException {
        assert petIndex.isPresent();
        if (petIndex.get().getZeroBased() >= petList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PET_DISPLAYED_INDEX);
        }
    }

    private void validateSessionIndex(List<Session> sessionList) throws CommandException {
        assert sessionIndex.isPresent();
        if (sessionIndex.get().getZeroBased() >= sessionList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_SESSION_DISPLAYED_INDEX);
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
                && petIndex.equals(otherDeleteCommand.petIndex)
                && sessionIndex.equals(otherDeleteCommand.sessionIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("petIndex", petIndex)
                .add("sessionIndex", sessionIndex)
                .toString();
    }
}
