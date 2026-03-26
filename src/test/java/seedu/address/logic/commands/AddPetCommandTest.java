package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
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
 * {@code AddPetCommand}.
 */
public class AddPetCommandTest {

    @Test
    public void execute_newPet_success() {
        Model model = new ModelManager(SampleDataUtil.getSampleAddressBook(), new UserPrefs());
        Pet petToAdd = new PetBuilder().withName("Nova").withSpecies("Dog").withPetRemark("Energetic").build();
        AddPetCommand command = new AddPetCommand(INDEX_FIRST_PERSON, petToAdd);

        Person owner = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Set<Pet> updatedPets = new LinkedHashSet<>(owner.getPets());
        updatedPets.add(petToAdd);

        Person editedOwner = new Person(owner.getName(), owner.getPhone(), owner.getEmail(),
                owner.getAddress(), owner.getTags(), updatedPets);
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(owner, editedOwner);

        assertCommandSuccess(command, model, String.format(AddPetCommand.MESSAGE_SUCCESS, petToAdd), expectedModel);
    }

    @Test
    public void execute_duplicatePetForSameOwner_throwsCommandException() {
        Model model = new ModelManager(SampleDataUtil.getSampleAddressBook(), new UserPrefs());
        Person owner = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Pet duplicatePet = owner.getPetList().get(0);

        AddPetCommand command = new AddPetCommand(INDEX_FIRST_PERSON,
                new Pet(duplicatePet.getName(), duplicatePet.getSpecies(), duplicatePet.getRemark()));

        assertCommandFailure(command, model, AddPetCommand.MESSAGE_DUPLICATE_PET);
    }

    @Test
    public void execute_duplicatePetForDifferentOwner_success() {
        Model model = new ModelManager(SampleDataUtil.getSampleAddressBook(), new UserPrefs());
        Person firstOwner = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Pet petToAdd = firstOwner.getPetList().get(0);

        AddPetCommand command = new AddPetCommand(INDEX_SECOND_PERSON,
                new Pet(petToAdd.getName(), petToAdd.getSpecies(), petToAdd.getRemark()));

        Person secondOwner = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        Set<Pet> updatedPets = new LinkedHashSet<>(secondOwner.getPets());
        updatedPets.add(new Pet(petToAdd.getName(), petToAdd.getSpecies(), petToAdd.getRemark()));

        Person editedOwner = new Person(secondOwner.getName(), secondOwner.getPhone(), secondOwner.getEmail(),
                secondOwner.getAddress(), secondOwner.getTags(), updatedPets);
        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(secondOwner, editedOwner);

        assertCommandSuccess(command, model, String.format(AddPetCommand.MESSAGE_SUCCESS, petToAdd), expectedModel);
    }

    @Test
    public void execute_invalidOwnerIndex_throwsCommandException() {
        Model model = new ModelManager(SampleDataUtil.getSampleAddressBook(), new UserPrefs());
        Pet petToAdd = new PetBuilder().build();
        Index outOfBoundsOwnerIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);

        AddPetCommand command = new AddPetCommand(outOfBoundsOwnerIndex, petToAdd);

        assertCommandFailure(command, model, "The owner index provided is invalid.");
    }

    @Test
    public void equals() {
        Pet pet = new PetBuilder().build();
        Pet otherPet = new PetBuilder().withName("Nova").build();

        AddPetCommand addPetCommand = new AddPetCommand(INDEX_FIRST_PERSON, pet);
        AddPetCommand addPetCommandCopy = new AddPetCommand(INDEX_FIRST_PERSON, pet);
        AddPetCommand differentOwnerCommand = new AddPetCommand(INDEX_SECOND_PERSON, pet);
        AddPetCommand differentPetCommand = new AddPetCommand(INDEX_FIRST_PERSON, otherPet);

        assertTrue(addPetCommand.equals(addPetCommand));
        assertTrue(addPetCommand.equals(addPetCommandCopy));
        assertFalse(addPetCommand.equals(1));
        assertFalse(addPetCommand.equals(null));
        assertFalse(addPetCommand.equals(differentOwnerCommand));
        assertFalse(addPetCommand.equals(differentPetCommand));
    }

    @Test
    public void toStringMethod() {
        Pet pet = new PetBuilder().build();
        AddPetCommand addPetCommand = new AddPetCommand(INDEX_FIRST_PERSON, pet);
        String expected = AddPetCommand.class.getCanonicalName()
                + "{ownerIndex=" + INDEX_FIRST_PERSON + ", toAdd=" + pet + "}";
        assertEquals(expected, addPetCommand.toString());
    }
}
