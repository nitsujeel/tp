package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_THIRD_PERSON;
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
import seedu.address.model.util.SampleDataUtil;
import seedu.address.testutil.PetBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteCommand}.
 */
public class DeleteCommandTest {

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

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
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

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validPetIndexUnfilteredList_success() {
        Model modelWithPets = new ModelManager(SampleDataUtil.getSampleAddressBook(), new UserPrefs());
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
        Model modelWithPets = new ModelManager(SampleDataUtil.getSampleAddressBook(), new UserPrefs());
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
        Model modelWithPets = new ModelManager(SampleDataUtil.getSampleAddressBook(), new UserPrefs());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON);

        assertCommandFailure(deleteCommand, modelWithPets, DeleteCommand.MESSAGE_INVALID_PET_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidOwnerIndexPetDeletion_throwsCommandException() {
        Model modelWithPets = new ModelManager(SampleDataUtil.getSampleAddressBook(), new UserPrefs());
        Index outOfBoundOwnerIndex = Index.fromOneBased(modelWithPets.getFilteredPersonList().size() + 1);
        DeleteCommand deleteCommand = new DeleteCommand(outOfBoundOwnerIndex, INDEX_FIRST_PERSON);

        assertCommandFailure(deleteCommand, modelWithPets, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_petDeletionForOwnerWithNoPets_throwsCommandException() {
        Model modelWithPets = new ModelManager(SampleDataUtil.getSampleAddressBook(), new UserPrefs());
        DeleteCommand deleteCommand = new DeleteCommand(INDEX_THIRD_PERSON, INDEX_FIRST_PERSON);

        assertCommandFailure(deleteCommand, modelWithPets, DeleteCommand.MESSAGE_INVALID_PET_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        DeleteCommand deleteFirstCommand = new DeleteCommand(INDEX_FIRST_PERSON);
        DeleteCommand deleteSecondCommand = new DeleteCommand(INDEX_SECOND_PERSON);
        DeleteCommand deleteFirstPetCommand = new DeleteCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCommand deleteFirstCommandCopy = new DeleteCommand(INDEX_FIRST_PERSON);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));

        // different pet index state -> returns false
        assertFalse(deleteFirstCommand.equals(deleteFirstPetCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeleteCommand deleteCommand = new DeleteCommand(targetIndex);
        String expected = DeleteCommand.class.getCanonicalName()
                + "{targetIndex=" + targetIndex + ", petIndex=Optional.empty}";
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
        Model baseModel = new ModelManager(SampleDataUtil.getSampleAddressBook(), new UserPrefs());
        Person owner = baseModel.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<Pet> updatedPets = new LinkedHashSet<>(owner.getPets());
        updatedPets.add(new PetBuilder().withName("Nova").withSpecies("Cat").withPetRemark("Playful").build());

        Person editedOwner = new Person(owner.getName(), owner.getPhone(), owner.getEmail(),
                owner.getAddress(), owner.getTags(), updatedPets);
        Model modelWithMultiplePets = new ModelManager(new AddressBook(baseModel.getAddressBook()), new UserPrefs());
        modelWithMultiplePets.setPerson(owner, editedOwner);
        return modelWithMultiplePets;
    }
}
