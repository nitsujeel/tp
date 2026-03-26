package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.service.Service;
import seedu.address.model.util.SampleDataUtil;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code DeleteServiceCommand}.
 */
public class DeleteServiceCommandTest {

    private static final String EXISTING_SERVICE_NAME = "Base service charge";
    private static final String ANOTHER_EXISTING_SERVICE_NAME = "Shampoo";
    private static final String NON_EXISTENT_SERVICE_NAME = "Non existent service";

    private final Model model = new ModelManager(SampleDataUtil.getSampleAddressBook(), new UserPrefs());

    @Test
    public void execute_validServiceName_success() {
        Service serviceToDelete = model.getServiceList().stream()
                .filter(service -> service.getName().equals(EXISTING_SERVICE_NAME))
                .findFirst()
                .get();
        DeleteServiceCommand deleteServiceCommand = new DeleteServiceCommand(EXISTING_SERVICE_NAME);

        String expectedMessage = String.format(DeleteServiceCommand.MESSAGE_DELETE_SERVICE_SUCCESS, serviceToDelete);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteService(serviceToDelete);

        assertCommandSuccess(deleteServiceCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidServiceName_throwsCommandException() {
        DeleteServiceCommand deleteServiceCommand = new DeleteServiceCommand(NON_EXISTENT_SERVICE_NAME);

        assertCommandFailure(deleteServiceCommand, model,
                DeleteServiceCommand.MESSAGE_INVALID_SERVICE_NAME);
    }

    @Test
    public void equals() {
        DeleteServiceCommand deleteFirstCommand = new DeleteServiceCommand(EXISTING_SERVICE_NAME);
        DeleteServiceCommand deleteSecondCommand = new DeleteServiceCommand(ANOTHER_EXISTING_SERVICE_NAME);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteServiceCommand deleteFirstCommandCopy = new DeleteServiceCommand(EXISTING_SERVICE_NAME);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different service name -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void toStringMethod() {
        DeleteServiceCommand deleteServiceCommand = new DeleteServiceCommand(EXISTING_SERVICE_NAME);
        String expected = DeleteServiceCommand.class.getCanonicalName()
                + "{targetServiceName=" + EXISTING_SERVICE_NAME + "}";
        assertEquals(expected, deleteServiceCommand.toString());
    }
}
