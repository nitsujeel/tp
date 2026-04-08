package seedu.address.model.session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.service.Service;

public class SessionTest {

    private static final String VALID_START = "2026-03-25 10:00";
    private static final String VALID_END = "2026-03-25 11:00";
    private static final String VALID_ADJACENT_START = "2026-03-25 11:00";
    private static final String VALID_ADJACENT_END = "2026-03-25 12:00";
    private static final String VALID_OVERLAPPING_START = "2026-03-25 10:30";
    private static final String VALID_OVERLAPPING_END = "2026-03-25 11:30";
    private static final Service SHAMPOO = new Service("Shampoo", 30.00);
    private static final Service NAIL_TRIM = new Service("Nail trim", 10.00);

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

    @Test
    public void constructor_withServices_storesImmutableServiceList() {
        Session session = new Session(VALID_START, VALID_END, 40.0, List.of(SHAMPOO, NAIL_TRIM));

        assertEquals(List.of(SHAMPOO, NAIL_TRIM), session.getServices());
        assertThrows(UnsupportedOperationException.class, () -> session.getServices().add(SHAMPOO));
    }

    @Test
    public void equals_sameTimeAndFeeButDifferentServices_notEqual() {
        Session shampooSession = new Session(VALID_START, VALID_END, 30.0, List.of(SHAMPOO));
        Session nailTrimSession = new Session(VALID_START, VALID_END, 30.0, List.of(NAIL_TRIM));

        assertFalse(shampooSession.equals(nailTrimSession));
    }

    @Test
    public void toStringMethod_withoutServices() {
        Session session = new Session(VALID_START, VALID_END, 0.0);
        String expected = "Start: 2026-03-25 10:00; End: 2026-03-25 11:00; Fee: $0.00; Services: None";

        assertEquals(expected, session.toString());
    }

    @Test
    public void toStringMethod_withServices() {
        Session session = new Session(VALID_START, VALID_END, 40.0, List.of(SHAMPOO, NAIL_TRIM));
        String expected = "Start: 2026-03-25 10:00; End: 2026-03-25 11:00; Fee: $40.00; "
                + "Services: Shampoo, Nail trim";

        assertEquals(expected, session.toString());
    }
}
