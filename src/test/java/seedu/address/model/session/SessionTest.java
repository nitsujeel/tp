package seedu.address.model.session;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class SessionTest {

    private static final String VALID_START = "2026-03-25 10:00";
    private static final String VALID_END = "2026-03-25 11:00";
    private static final String VALID_ADJACENT_START = "2026-03-25 11:00";
    private static final String VALID_ADJACENT_END = "2026-03-25 12:00";
    private static final String VALID_OVERLAPPING_START = "2026-03-25 10:30";
    private static final String VALID_OVERLAPPING_END = "2026-03-25 11:30";

    @Test
    public void constructor_invalidDateTime_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, Session.MESSAGE_DATETIME_CONSTRAINTS, (
                ) -> new Session("2026-02-30 10:00", VALID_END));
        assertThrows(IllegalArgumentException.class, Session.MESSAGE_DATETIME_CONSTRAINTS, (
                ) -> new Session("2026/03/25 10:00", VALID_END));
    }

    @Test
    public void constructor_endNotAfterStart_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, Session.MESSAGE_INVALID_TIME_RANGE, (
                ) -> new Session(VALID_END, VALID_START));
        assertThrows(IllegalArgumentException.class, Session.MESSAGE_INVALID_TIME_RANGE, (
                ) -> new Session(VALID_START, VALID_START));
    }

    @Test
    public void overlapsWith() {
        Session session = new Session(VALID_START, VALID_END);
        Session overlappingSession = new Session(VALID_OVERLAPPING_START, VALID_OVERLAPPING_END);
        Session adjacentSession = new Session(VALID_ADJACENT_START, VALID_ADJACENT_END);

        assertTrue(session.overlapsWith(overlappingSession));
        assertFalse(session.overlapsWith(adjacentSession));
    }
}
