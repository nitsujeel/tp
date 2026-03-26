package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.service.Service;
import seedu.address.model.util.SampleDataUtil;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code DeleteServiceCommand}.
 */
public class DeleteServiceCommandTest {

    private final Model model = new ModelManager(SampleDataUtil.getSampleAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Service serviceToDelete = model.getServiceList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteServiceCommand deleteServiceCommand = new DeleteServiceCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteServiceCommand.MESSAGE_DELETE_SERVICE_SUCCESS, serviceToDelete);

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteService(serviceToDelete);

        assertCommandSuccess(deleteServiceCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundsIndex = Index.fromOneBased(model.getServiceList().size() + 1);
        DeleteServiceCommand deleteServiceCommand = new DeleteServiceCommand(outOfBoundsIndex);

        assertCommandFailure(deleteServiceCommand, model,
                DeleteServiceCommand.MESSAGE_INVALID_SERVICE_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        DeleteServiceCommand deleteFirstCommand = new DeleteServiceCommand(INDEX_FIRST_PERSON);
        DeleteServiceCommand deleteSecondCommand = new DeleteServiceCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteServiceCommand deleteFirstCommandCopy = new DeleteServiceCommand(INDEX_FIRST_PERSON);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different index -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeleteServiceCommand deleteServiceCommand = new DeleteServiceCommand(targetIndex);
        String expected = DeleteServiceCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, deleteServiceCommand.toString());
    }
}
