package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.service.Service;
import seedu.address.testutil.TypicalAddressBooks;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code AddServiceCommand}.
 */
public class AddServiceCommandTest {

    @Test
    public void constructor_nullService_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddServiceCommand(null));
    }

    @Test
    public void execute_serviceAcceptedByModel_addSuccessful() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        Service validService = new Service("Ear cleaning", 12.50);
        AddServiceCommand addServiceCommand = new AddServiceCommand(validService);

        String expectedMessage = String.format(AddServiceCommand.MESSAGE_SUCCESS, validService);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addService(validService);

        assertCommandSuccess(addServiceCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicateService_throwsCommandException() {
        Model model = new ModelManager(TypicalAddressBooks.getTypicalPetLog(), new UserPrefs());
        Service duplicateService = model.getServiceList().get(0);
        AddServiceCommand addServiceCommand = new AddServiceCommand(duplicateService);

        assertCommandFailure(addServiceCommand, model, AddServiceCommand.MESSAGE_DUPLICATE_SERVICE);
    }

    @Test
    public void equals() {
        Service furTrim = new Service("Fur trim", 25);
        Service shampoo = new Service("Shampoo", 30);
        AddServiceCommand addFurTrimCommand = new AddServiceCommand(furTrim);
        AddServiceCommand addShampooCommand = new AddServiceCommand(shampoo);

        // same object -> returns true
        assertTrue(addFurTrimCommand.equals(addFurTrimCommand));

        // same values -> returns true
        AddServiceCommand addFurTrimCommandCopy = new AddServiceCommand(furTrim);
        assertTrue(addFurTrimCommand.equals(addFurTrimCommandCopy));

        // different types -> returns false
        assertFalse(addFurTrimCommand.equals(1));

        // null -> returns false
        assertFalse(addFurTrimCommand.equals(null));

        // different service -> returns false
        assertFalse(addFurTrimCommand.equals(addShampooCommand));
    }

    @Test
    public void toStringMethod() {
        Service service = new Service("Fur trim", 25.00);
        AddServiceCommand addServiceCommand = new AddServiceCommand(service);
        String expected = AddServiceCommand.class.getCanonicalName() + "{toAdd=" + service + "}";
        assertEquals(expected, addServiceCommand.toString());
    }
}
