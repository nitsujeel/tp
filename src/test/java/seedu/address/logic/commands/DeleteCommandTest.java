package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.pet.Pet;
import seedu.address.model.service.Service;
import seedu.address.model.session.Session;
import seedu.address.testutil.PetBuilder;
import seedu.address.testutil.TypicalAddressBooks;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteCommandTest {

    private static final String EXISTING_SERVICE_NAME = "Base service charge";
    private static final String EXISTING_SERVICE_NAME_DIFFERENT_CASE = "base service charge";
    private static final String EXISTING_SERVICE_NAME_LONG_WHITESPACE = "  Base   service   charge ";
    private static final String NON_EXISTENT_SERVICE_NAME = "Non existent service";
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validLastIndexUnfilteredList_success() {
        Index lastIndex = Index.fromOneBased(model.getFilteredPersonList().size());
        Person personToDelete = model.getFilteredPersonList().get(lastIndex.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(lastIndex);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_OWNER_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToDelete = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PERSON_SUCCESS,
                Messages.format(personToDelete));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deletePerson(personToDelete);
        showNoPerson(expectedModel);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_OWNER_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validPetIndexUnfilteredList_success() {
        Model modelWithPets = new ModelManager(TypicalAddressBooks.getTypicalPetLog(), new UserPrefs());
        Person owner = modelWithPets.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        List<Pet> ownerPets = owner.getPetList();
        Pet petToDelete = ownerPets.get(INDEX_FIRST_PERSON.getZeroBased());

        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON);
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PET_SUCCESS, petToDelete);

        Model expectedModel = new ModelManager(new AddressBook(modelWithPets.getAddressBook()), new UserPrefs());
        Set<Pet> updatedPets = new LinkedHashSet<>(owner.getPets());
        updatedPets.remove(petToDelete);
        Person editedOwner = new Person(owner.getName(), owner.getPhone(), owner.getEmail(),
                owner.getAddress(), owner.getTags(), updatedPets);
        expectedModel.setPerson(owner, editedOwner);

        assertCommandSuccess(deleteCommand, modelWithPets, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validLastPetIndexUnfilteredList_success() {
        Model modelWithMultiplePets = getModelWithMultiplePetsForFirstOwner();
        Person owner = modelWithMultiplePets.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Index lastPetIndex = Index.fromOneBased(owner.getPetCount());
        Pet petToDelete = owner.getPetList().get(lastPetIndex.getZeroBased());

        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON, lastPetIndex);
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PET_SUCCESS, petToDelete);

        Model expectedModel = new ModelManager(new AddressBook(modelWithMultiplePets.getAddressBook()),
                new UserPrefs());
        Set<Pet> updatedPets = new LinkedHashSet<>(owner.getPets());
        updatedPets.remove(petToDelete);
        Person editedOwner = new Person(owner.getName(), owner.getPhone(), owner.getEmail(),
                owner.getAddress(), owner.getTags(), updatedPets);
        expectedModel.setPerson(owner, editedOwner);

        assertCommandSuccess(deleteCommand, modelWithMultiplePets, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validPetIndexFilteredList_success() {
        Model modelWithPets = new ModelManager(TypicalAddressBooks.getTypicalPetLog(), new UserPrefs());
        showPersonAtIndex(modelWithPets, INDEX_FIRST_PERSON);

        Person owner = modelWithPets.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Pet petToDelete = owner.getPetList().get(INDEX_FIRST_PERSON.getZeroBased());

        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON);
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_PET_SUCCESS, petToDelete);

        Model expectedModel = new ModelManager(new AddressBook(modelWithPets.getAddressBook()), new UserPrefs());
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);
        Set<Pet> updatedPets = new LinkedHashSet<>(owner.getPets());
        updatedPets.remove(petToDelete);
        Person editedOwner = new Person(owner.getName(), owner.getPhone(), owner.getEmail(),
                owner.getAddress(), owner.getTags(), updatedPets);
        expectedModel.setPerson(owner, editedOwner);

        assertCommandSuccess(deleteCommand, modelWithPets, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPetIndexUnfilteredList_throwsCommandException() {
        Model modelWithPets = new ModelManager(TypicalAddressBooks.getTypicalPetLog(), new UserPrefs());
        Person owner = modelWithPets.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Index outOfBoundsPetIndex = Index.fromOneBased(owner.getPetCount() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON, outOfBoundsPetIndex);

        assertCommandFailure(deleteCommand, modelWithPets, DeleteCommand.MESSAGE_INVALID_PET_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidOwnerIndexPetDeletion_throwsCommandException() {
        Model modelWithPets = new ModelManager(TypicalAddressBooks.getTypicalPetLog(), new UserPrefs());
        Index outOfBoundOwnerIndex = Index.fromOneBased(modelWithPets.getFilteredPersonList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundOwnerIndex, INDEX_FIRST_PERSON);

        assertCommandFailure(deleteCommand, modelWithPets, Messages.MESSAGE_INVALID_OWNER_DISPLAYED_INDEX);
    }

    @Test
    public void execute_petDeletionForOwnerWithNoPets_throwsCommandException() {
        Model modelWithOwnerWithoutPets = getModelWithNoPetsForThirdOwner();
        DeleteCommand deleteCommand = new DeleteCommand(Index.fromOneBased(3), INDEX_FIRST_PERSON);

        assertCommandFailure(deleteCommand, modelWithOwnerWithoutPets,
                DeleteCommand.MESSAGE_INVALID_PET_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validSessionIndexUnfilteredList_success() throws Exception {
        Model modelWithSessions = getModelWithTwoSessionsForFirstPet();
        Person owner = modelWithSessions.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Pet pet = owner.getPetList().get(INDEX_FIRST_PERSON.getZeroBased());
        Session sessionToDelete = pet.getSessions().get(INDEX_FIRST_PERSON.getZeroBased());
        Session sessionToKeep = pet.getSessions().get(INDEX_SECOND_PERSON.getZeroBased());

        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, INDEX_FIRST_PERSON);
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_SESSION_SUCCESS, sessionToDelete);

        CommandResult result = deleteCommand.execute(modelWithSessions);
        assertEquals(expectedMessage, result.getFeedbackToUser());

        Pet updatedPet = modelWithSessions.getFilteredPersonList()
                .get(INDEX_FIRST_PERSON.getZeroBased())
                .getPetList()
                .get(INDEX_FIRST_PERSON.getZeroBased());
        assertEquals(1, updatedPet.getSessions().size());
        assertEquals(sessionToKeep, updatedPet.getSessions().get(INDEX_FIRST_PERSON.getZeroBased()));
    }

    @Test
    public void execute_invalidSessionIndexUnfilteredList_throwsCommandException() {
        Model modelWithSessions = getModelWithTwoSessionsForFirstPet();
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON,
                Index.fromOneBased(3));

        assertCommandFailure(deleteCommand, modelWithSessions, DeleteCommand.MESSAGE_INVALID_SESSION_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validServiceName_success() {
        Model modelWithServices = new ModelManager(TypicalAddressBooks.getTypicalPetLog(), new UserPrefs());
        Service serviceToDelete = modelWithServices.getServiceList().stream()
                .filter(service -> service.getName().equals(EXISTING_SERVICE_NAME))
                .findFirst()
                .get();

        DeleteCommand deleteCommand = new DeleteCommand(EXISTING_SERVICE_NAME);
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_SERVICE_SUCCESS, serviceToDelete);

        Model expectedModel = new ModelManager(modelWithServices.getAddressBook(), new UserPrefs());
        expectedModel.deleteService(serviceToDelete);

        assertCommandSuccess(deleteCommand, modelWithServices, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validServiceNameDifferentCase_success() {
        Model modelWithServices = new ModelManager(TypicalAddressBooks.getTypicalPetLog(), new UserPrefs());
        Service serviceToDelete = modelWithServices.getServiceList().stream()
                .filter(service -> service.getName().equals(EXISTING_SERVICE_NAME))
                .findFirst()
                .get();

        DeleteCommand deleteCommand = new DeleteCommand(EXISTING_SERVICE_NAME_DIFFERENT_CASE);
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_SERVICE_SUCCESS, serviceToDelete);

        Model expectedModel = new ModelManager(modelWithServices.getAddressBook(), new UserPrefs());
        expectedModel.deleteService(serviceToDelete);

        assertCommandSuccess(deleteCommand, modelWithServices, expectedMessage, expectedModel);
    }

    @Test
    public void execute_validServiceNameWithLongWhitespace_success() {
        Model modelWithServices = new ModelManager(TypicalAddressBooks.getTypicalPetLog(), new UserPrefs());
        Service serviceToDelete = modelWithServices.getServiceList().stream()
                .filter(service -> service.getName().equals(EXISTING_SERVICE_NAME))
                .findFirst()
                .get();

        DeleteCommand deleteCommand = new DeleteCommand(EXISTING_SERVICE_NAME_LONG_WHITESPACE);
        String expectedMessage = String.format(DeleteCommand.MESSAGE_DELETE_SERVICE_SUCCESS, serviceToDelete);

        Model expectedModel = new ModelManager(modelWithServices.getAddressBook(), new UserPrefs());
        expectedModel.deleteService(serviceToDelete);

        assertCommandSuccess(deleteCommand, modelWithServices, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidServiceName_throwsCommandException() {
        Model modelWithServices = new ModelManager(TypicalAddressBooks.getTypicalPetLog(), new UserPrefs());
        DeleteCommand deleteCommand = new DeleteCommand(NON_EXISTENT_SERVICE_NAME);

        assertCommandFailure(deleteCommand, modelWithServices, DeleteCommand.MESSAGE_INVALID_SERVICE_NAME);
    }

    @Test
    public void equals() {
        DeleteCommand deleteFirstCommand = new DeleteCommand(INDEX_FIRST_PERSON);
        DeleteCommand deleteSecondCommand = new DeleteCommand(INDEX_SECOND_PERSON);
        DeleteCommand deleteFirstPetCommand = new DeleteCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON);
        DeleteCommand deleteFirstSessionCommand = new DeleteCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON,
                INDEX_FIRST_PERSON);
        DeleteCommand deleteServiceCommand = new DeleteCommand(EXISTING_SERVICE_NAME);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new DeleteCommand(INDEX_FIRST_PERSON);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));
        DeleteCommand deleteFirstSessionCommandCopy = new DeleteCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON,
                INDEX_FIRST_PERSON);
        assertTrue(deleteFirstSessionCommand.equals(deleteFirstSessionCommandCopy));
        DeleteCommand deleteServiceCommandCopy = new DeleteCommand(EXISTING_SERVICE_NAME);
        assertTrue(deleteServiceCommand.equals(deleteServiceCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));

        // different pet index state -> returns false
        assertFalse(deleteFirstCommand.equals(deleteFirstPetCommand));
        assertFalse(deleteFirstPetCommand.equals(deleteFirstSessionCommand));
        assertFalse(deleteFirstCommand.equals(deleteServiceCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeleteCommand deleteCommand = new DeleteCommand(targetIndex);
        String expected = DeleteCommand.class.getCanonicalName()
                + "{targetIndex=Optional[" + targetIndex + "], petIndex=Optional.empty, sessionIndex=Optional.empty, "
                + "serviceName=Optional.empty}";
        assertEquals(expected, deleteCommand.toString());
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);

        assertTrue(model.getFilteredPersonList().isEmpty());
    }

    private Model getModelWithMultiplePetsForFirstOwner() {
        Model baseModel = new ModelManager(TypicalAddressBooks.getTypicalPetLog(), new UserPrefs());
        Person owner = baseModel.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<Pet> updatedPets = new LinkedHashSet<>(owner.getPets());
        updatedPets.add(new PetBuilder().withName("Nova").withSpecies("Cat")
                .withPetRemark("Playful").build());

        Person editedOwner = new Person(owner.getName(), owner.getPhone(), owner.getEmail(),
                owner.getAddress(), owner.getTags(), updatedPets);
        Model modelWithMultiplePets = new ModelManager(new AddressBook(baseModel.getAddressBook()), new UserPrefs());
        modelWithMultiplePets.setPerson(owner, editedOwner);
        return modelWithMultiplePets;
    }

    private Model getModelWithNoPetsForThirdOwner() {
        Model baseModel = new ModelManager(TypicalAddressBooks.getTypicalPetLog(), new UserPrefs());
        Person owner = baseModel.getFilteredPersonList().get(2);
        Person editedOwner = new Person(owner.getName(), owner.getPhone(), owner.getEmail(),
                owner.getAddress(), owner.getTags(), new LinkedHashSet<>());
        Model modelWithOwnerWithoutPets = new ModelManager(new AddressBook(baseModel.getAddressBook()),
                new UserPrefs());
        modelWithOwnerWithoutPets.setPerson(owner, editedOwner);
        return modelWithOwnerWithoutPets;
    }

    private Model getModelWithTwoSessionsForFirstPet() {
        Model modelWithSessions = new ModelManager(new AddressBook(TypicalAddressBooks.getTypicalPetLog()),
                new UserPrefs());
        Pet firstPet = modelWithSessions.getFilteredPersonList()
                .get(INDEX_FIRST_PERSON.getZeroBased())
                .getPetList()
                .get(INDEX_FIRST_PERSON.getZeroBased());
        firstPet.addSession(new Session("2026-04-01 10:00", "2026-04-01 11:00", 20.0));
        firstPet.addSession(new Session("2026-04-01 11:00", "2026-04-01 12:00", 15.0));
        modelWithSessions.updateDisplayedSessions(modelWithSessions.getFilteredPersonList());
        return modelWithSessions;
    }
}
