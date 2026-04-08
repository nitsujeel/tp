package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import java.util.LinkedHashSet;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.TypicalAddressBooks;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code UpdatePetRemarkCommand}.
 */
public class UpdatePetRemarkCommandTest {

    @Test
    public void execute_validOwnerAndPetIndex_success() {
        Model model = new ModelManager(TypicalAddressBooks.getTypicalPetLog(), new UserPrefs());
        String newRemark = "Very friendly and loves to play fetch";

        UpdatePetRemarkCommand command = new UpdatePetRemarkCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON,
                newRemark);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(command, model, UpdatePetRemarkCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_secondOwnerSecondPet_success() {
        Model model = new ModelManager(TypicalAddressBooks.getTypicalPetLog(), new UserPrefs());
        Person secondOwner = model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased());

        // Only test if second owner has at least one pet
        if (secondOwner.getPetCount() >= 1) {
            String newRemark = "Updated remark for second pet";
            UpdatePetRemarkCommand command = new UpdatePetRemarkCommand(INDEX_SECOND_PERSON, INDEX_FIRST_PERSON,
                    newRemark);

            Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

            assertCommandSuccess(command, model, UpdatePetRemarkCommand.MESSAGE_SUCCESS, expectedModel);
        }
    }

    @Test
    public void execute_invalidOwnerIndexTooLarge_throwsCommandException() {
        Model model = new ModelManager(TypicalAddressBooks.getTypicalPetLog(), new UserPrefs());
        int outOfBoundsOwnerIndex = model.getFilteredPersonList().size() + 1;
        Index invalidOwnerIndex = Index.fromOneBased(outOfBoundsOwnerIndex);

        UpdatePetRemarkCommand command = new UpdatePetRemarkCommand(invalidOwnerIndex, INDEX_FIRST_PERSON,
                "Some remark");

        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_OWNER_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidPetIndexTooLarge_throwsCommandException() {
        Model model = new ModelManager(TypicalAddressBooks.getTypicalPetLog(), new UserPrefs());
        Person owner = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        int outOfBoundsPetIndex = owner.getPetCount() + 1;
        Index invalidPetIndex = Index.fromOneBased(outOfBoundsPetIndex);

        UpdatePetRemarkCommand command = new UpdatePetRemarkCommand(INDEX_FIRST_PERSON, invalidPetIndex,
                "Some remark");

        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_PET_DISPLAYED_INDEX);
    }

    @Test
    public void execute_ownerWithNoPets_throwsCommandException() {
        Model model = getModelWithNoPetsForThirdOwner();
        UpdatePetRemarkCommand command = new UpdatePetRemarkCommand(Index.fromOneBased(3), INDEX_FIRST_PERSON,
                "Some remark");

        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_PET_DISPLAYED_INDEX);
    }

    @Test
    public void execute_emptyRemark_success() {
        Model model = new ModelManager(TypicalAddressBooks.getTypicalPetLog(), new UserPrefs());
        String emptyRemark = "";

        UpdatePetRemarkCommand command = new UpdatePetRemarkCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON,
                emptyRemark);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(command, model, UpdatePetRemarkCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_longRemark_success() {
        Model model = new ModelManager(TypicalAddressBooks.getTypicalPetLog(), new UserPrefs());
        String longRemark = "This is a very long remark about the pet. It includes detailed information "
                + "about the pet's health, behavior, and special needs. The pet is friendly with other dogs "
                + "and cats. It needs special nutrition for joint health. It should be exercised for at "
                + "least 2 hours daily.";

        UpdatePetRemarkCommand command = new UpdatePetRemarkCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON,
                longRemark);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(command, model, UpdatePetRemarkCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_remarkWithSpecialCharacters_success() {
        Model model = new ModelManager(TypicalAddressBooks.getTypicalPetLog(), new UserPrefs());
        String specialRemark = "Pet needs: @home, #vaccination, $medicine";

        UpdatePetRemarkCommand command = new UpdatePetRemarkCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON,
                specialRemark);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(command, model, UpdatePetRemarkCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_updateRemarkMultipleTimes_success() {
        Model model = new ModelManager(TypicalAddressBooks.getTypicalPetLog(), new UserPrefs());

        // First update
        String remark1 = "Initial remark";
        UpdatePetRemarkCommand command1 = new UpdatePetRemarkCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON,
                remark1);

        Model expectedModel1 = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        assertCommandSuccess(command1, model, UpdatePetRemarkCommand.MESSAGE_SUCCESS, expectedModel1);

        // Second update
        String remark2 = "Updated remark again";
        UpdatePetRemarkCommand command2 = new UpdatePetRemarkCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON,
                remark2);

        Model expectedModel2 = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        assertCommandSuccess(command2, model, UpdatePetRemarkCommand.MESSAGE_SUCCESS, expectedModel2);
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        UpdatePetRemarkCommand command = new UpdatePetRemarkCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON,
                "Some remark");

        assertThrows(NullPointerException.class, () -> command.execute(null));
    }

    @Test
    public void equals_sameCommand_returnsTrue() {
        UpdatePetRemarkCommand command1 = new UpdatePetRemarkCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON,
                "Friendly pet");
        UpdatePetRemarkCommand command2 = new UpdatePetRemarkCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON,
                "Friendly pet");

        assertTrue(command1.equals(command2));
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        UpdatePetRemarkCommand command = new UpdatePetRemarkCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON,
                "Friendly pet");

        assertTrue(command.equals(command));
    }

    @Test
    public void equals_null_returnsFalse() {
        UpdatePetRemarkCommand command = new UpdatePetRemarkCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON,
                "Friendly pet");

        assertFalse(command.equals(null));
    }

    @Test
    public void equals_differentType_returnsFalse() {
        UpdatePetRemarkCommand command = new UpdatePetRemarkCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON,
                "Friendly pet");

        assertFalse(command.equals(1));
    }

    @Test
    public void equals_differentOwnerIndex_returnsFalse() {
        UpdatePetRemarkCommand command1 = new UpdatePetRemarkCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON,
                "Friendly pet");
        UpdatePetRemarkCommand command2 = new UpdatePetRemarkCommand(INDEX_SECOND_PERSON, INDEX_FIRST_PERSON,
                "Friendly pet");

        assertFalse(command1.equals(command2));
    }

    @Test
    public void equals_differentPetIndex_returnsFalse() {
        UpdatePetRemarkCommand command1 = new UpdatePetRemarkCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON,
                "Friendly pet");
        UpdatePetRemarkCommand command2 = new UpdatePetRemarkCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON,
                "Friendly pet");

        assertFalse(command1.equals(command2));
    }

    @Test
    public void equals_differentRemark_returnsFalse() {
        UpdatePetRemarkCommand command1 = new UpdatePetRemarkCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON,
                "Friendly pet");
        UpdatePetRemarkCommand command2 = new UpdatePetRemarkCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON,
                "Aggressive pet");

        assertFalse(command1.equals(command2));
    }

    @Test
    public void equals_allFieldsDifferent_returnsFalse() {
        UpdatePetRemarkCommand command1 = new UpdatePetRemarkCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON,
                "Remark 1");
        UpdatePetRemarkCommand command2 = new UpdatePetRemarkCommand(INDEX_SECOND_PERSON, INDEX_SECOND_PERSON,
                "Remark 2");

        assertFalse(command1.equals(command2));
    }

    @Test
    public void toStringMethod() {
        UpdatePetRemarkCommand command = new UpdatePetRemarkCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON,
                "Friendly pet");
        String result = command.toString();

        assertTrue(result.contains("New Remark"));
        assertTrue(result.contains("Friendly pet"));
    }

    @Test
    public void toStringMethod_withSpecialCharacters() {
        String specialRemark = "Pet@Home#Medical$Care";
        UpdatePetRemarkCommand command = new UpdatePetRemarkCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON,
                specialRemark);
        String result = command.toString();

        assertTrue(result.contains(specialRemark));
    }

    @Test
    public void toStringMethod_emptyRemark() {
        UpdatePetRemarkCommand command = new UpdatePetRemarkCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, "");
        String result = command.toString();

        assertTrue(result.contains("New Remark"));
    }

    @Test
    public void constructor_validParameters_success() {
        UpdatePetRemarkCommand command = new UpdatePetRemarkCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON,
                "Test remark");

        assertEquals(command.getNewRemark(), "Test remark");
    }

    @Test
    public void constructor_nullRemark_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new UpdatePetRemarkCommand(INDEX_FIRST_PERSON,
                INDEX_FIRST_PERSON, null));
    }

    @Test
    public void execute_validIndicesWithWhitespaceRemark_success() {
        Model model = new ModelManager(TypicalAddressBooks.getTypicalPetLog(), new UserPrefs());
        String remarkWithWhitespace = "   Remark with spaces   ";

        UpdatePetRemarkCommand command = new UpdatePetRemarkCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON,
                remarkWithWhitespace);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(command, model, UpdatePetRemarkCommand.MESSAGE_SUCCESS, expectedModel);
    }

    @Test
    public void execute_validIndicesWithNumericRemark_success() {
        Model model = new ModelManager(TypicalAddressBooks.getTypicalPetLog(), new UserPrefs());
        String numericRemark = "Age: 5, Weight: 25kg";

        UpdatePetRemarkCommand command = new UpdatePetRemarkCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON,
                numericRemark);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(command, model, UpdatePetRemarkCommand.MESSAGE_SUCCESS, expectedModel);
    }

    private Model getModelWithNoPetsForThirdOwner() {
        Model baseModel = new ModelManager(TypicalAddressBooks.getTypicalPetLog(), new UserPrefs());
        Person owner = baseModel.getFilteredPersonList().get(2);
        Person editedOwner = new Person(owner.getName(), owner.getPhone(), owner.getEmail(),
                owner.getAddress(), owner.getTags(), new LinkedHashSet<>());
        Model modelWithOwnerWithoutPets = new ModelManager(
                new AddressBook(baseModel.getAddressBook()), new UserPrefs());
        modelWithOwnerWithoutPets.setPerson(owner, editedOwner);
        return modelWithOwnerWithoutPets;
    }
}
