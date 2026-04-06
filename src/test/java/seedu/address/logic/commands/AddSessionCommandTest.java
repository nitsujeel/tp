package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.pet.Pet;
import seedu.address.model.session.Session;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code AddSessionCommand}.
 */
public class AddSessionCommandTest {

    private static final String VALID_START = "2026-03-25 10:00";
    private static final String VALID_END = "2026-03-25 11:00";
    private static final String VALID_START_2 = "2026-03-26 14:00";
    private static final String VALID_END_2 = "2026-03-26 15:00";
    private static final String OVERLAPPING_START = "2026-03-25 10:30";
    private static final String OVERLAPPING_END = "2026-03-25 11:30";
    private static final String ADJACENT_START = "2026-03-25 11:00";
    private static final String ADJACENT_END = "2026-03-25 12:00";

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_validIndices_success() {
        Person owner = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Pet pet = owner.getPetList().get(INDEX_FIRST_PERSON.getZeroBased());

        AddSessionCommand command = new AddSessionCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, VALID_START, VALID_END);

        String expectedMessage = String.format(AddSessionCommand.MESSAGE_SUCCESS,
                owner.getName(), pet.getName(), VALID_START, VALID_END) + ". Total fee: $0.00";

        // Model equality is unaffected by session addition (sessions excluded from equals)
        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_sessionAddedToPet() throws Exception {
        AddSessionCommand command = new AddSessionCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, VALID_START, VALID_END);
        command.execute(model);

        Pet pet = model.getFilteredPersonList()
                .get(INDEX_FIRST_PERSON.getZeroBased())
                .getPetList()
                .get(INDEX_FIRST_PERSON.getZeroBased());

        assertEquals(1, pet.getSessions().size());
        assertEquals(VALID_START, pet.getSessions().get(0).getStartTime());
        assertEquals(VALID_END, pet.getSessions().get(0).getEndTime());
    }

    @Test
    public void execute_multipleSessionsAddedToPet() throws Exception {
        AddSessionCommand first = new AddSessionCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, VALID_START, VALID_END);
        AddSessionCommand second = new AddSessionCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, VALID_START_2, VALID_END_2);

        first.execute(model);
        second.execute(model);

        Pet pet = model.getFilteredPersonList()
                .get(INDEX_FIRST_PERSON.getZeroBased())
                .getPetList()
                .get(INDEX_FIRST_PERSON.getZeroBased());

        assertEquals(2, pet.getSessions().size());
        assertEquals(new Session(VALID_START, VALID_END), pet.getSessions().get(0));
        assertEquals(new Session(VALID_START_2, VALID_END_2), pet.getSessions().get(1));
    }

    @Test
    public void execute_sessionListUpdatedInModel() throws Exception {
        AddSessionCommand command = new AddSessionCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, VALID_START, VALID_END);
        command.execute(model);

        assertEquals(1, model.getSessionList().size());
        assertEquals(new Session(VALID_START, VALID_END), model.getSessionList().get(0));
    }

    @Test
    public void execute_invalidOwnerIndex_throwsCommandException() {
        Index outOfBound = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        AddSessionCommand command = new AddSessionCommand(
                outOfBound, INDEX_FIRST_PERSON, VALID_START, VALID_END);

        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_OWNER_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidPetIndex_throwsCommandException() {
        // Every typical person has exactly 1 pet, so index 2 is always out of bounds
        Index outOfBound = Index.fromOneBased(2);
        AddSessionCommand command = new AddSessionCommand(
                INDEX_FIRST_PERSON, outOfBound, VALID_START, VALID_END);

        assertCommandFailure(command, model, Messages.MESSAGE_INVALID_PET_DISPLAYED_INDEX);
    }

    @Test
    public void execute_endBeforeStart_throwsCommandException() {
        AddSessionCommand command = new AddSessionCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, VALID_END, VALID_START);

        assertCommandFailure(command, model, Session.MESSAGE_INVALID_TIME_RANGE);
    }

    @Test
    public void execute_overlappingSession_throwsCommandException() throws Exception {
        AddSessionCommand first = new AddSessionCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, VALID_START, VALID_END);
        first.execute(model);

        AddSessionCommand overlapping = new AddSessionCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, OVERLAPPING_START, OVERLAPPING_END);

        assertCommandFailure(overlapping, model, AddSessionCommand.MESSAGE_OVERLAPPING_SESSION);
    }

    @Test
    public void execute_adjacentSession_success() throws Exception {
        AddSessionCommand first = new AddSessionCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, VALID_START, VALID_END);
        AddSessionCommand adjacent = new AddSessionCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, ADJACENT_START, ADJACENT_END);

        first.execute(model);
        adjacent.execute(model);

        Pet pet = model.getFilteredPersonList()
                .get(INDEX_FIRST_PERSON.getZeroBased())
                .getPetList()
                .get(INDEX_FIRST_PERSON.getZeroBased());

        assertEquals(2, pet.getSessions().size());
        assertEquals(new Session(ADJACENT_START, ADJACENT_END), pet.getSessions().get(1));
    }

    @Test
    public void equals() {
        AddSessionCommand commandA = new AddSessionCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, VALID_START, VALID_END);
        AddSessionCommand commandB = new AddSessionCommand(
                INDEX_SECOND_PERSON, INDEX_FIRST_PERSON, VALID_START, VALID_END);

        // same object -> true
        assertTrue(commandA.equals(commandA));

        // same values -> true
        AddSessionCommand commandACopy = new AddSessionCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, VALID_START, VALID_END);
        assertTrue(commandA.equals(commandACopy));

        // null -> false
        assertFalse(commandA.equals(null));

        // different type -> false
        assertFalse(commandA.equals("string"));

        // different owner index -> false
        assertFalse(commandA.equals(commandB));

        // different pet index -> false
        AddSessionCommand commandDiffPet = new AddSessionCommand(
                INDEX_FIRST_PERSON, INDEX_SECOND_PERSON, VALID_START, VALID_END);
        assertFalse(commandA.equals(commandDiffPet));

        // different start time -> false
        AddSessionCommand commandDiffStart = new AddSessionCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, VALID_START_2, VALID_END);
        assertFalse(commandA.equals(commandDiffStart));

        // different end time -> false
        AddSessionCommand commandDiffEnd = new AddSessionCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, VALID_START, VALID_END_2);
        assertFalse(commandA.equals(commandDiffEnd));
    }

    @Test
    public void toStringMethod() {
        AddSessionCommand command = new AddSessionCommand(
                INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, VALID_START, VALID_END);
        String expected = AddSessionCommand.class.getCanonicalName()
                + "{ownerIndex=" + INDEX_FIRST_PERSON
                + ", petIndex=" + INDEX_FIRST_PERSON
                + ", startTime=" + VALID_START
                + ", endTime=" + VALID_END
                + ", serviceNames=[]}";
        assertEquals(expected, command.toString());
    }
}
