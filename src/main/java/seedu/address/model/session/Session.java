package seedu.address.model.session;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents a booked session for a Pet in PetLog.
 * Guarantees: immutable; startTime and endTime are present and not null.
 */
public class Session {

    private final String startTime;
    private final String endTime;

    /**
     * Constructs a {@code Session}.
     *
     * @param startTime The start time string of the session.
     * @param endTime   The end time string of the session.
     */
    public Session(String startTime, String endTime) {
        requireNonNull(startTime);
        requireNonNull(endTime);
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Session)) {
            return false;
        }
        Session otherSession = (Session) other;
        return startTime.equals(otherSession.startTime)
                && endTime.equals(otherSession.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("startTime", startTime)
                .add("endTime", endTime)
                .toString();
    }
}
