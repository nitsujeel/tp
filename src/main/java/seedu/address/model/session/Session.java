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
    private final double fee;

    /**
     * Constructs a {@code Session} with no fee.
     *
     * @param startTime The start time string of the session.
     * @param endTime   The end time string of the session.
     */
    public Session(String startTime, String endTime) {
        this(startTime, endTime, 0.0);
    }

    /**
     * Constructs a {@code Session}.
     *
     * @param startTime The start time string of the session.
     * @param endTime   The end time string of the session.
     * @param fee       The total fee for this session.
     */
    public Session(String startTime, String endTime, double fee) {
        requireNonNull(startTime);
        requireNonNull(endTime);
        this.startTime = startTime;
        this.endTime = endTime;
        this.fee = fee;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public double getFee() {
        return fee;
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
                && endTime.equals(otherSession.endTime)
                && Double.compare(fee, otherSession.fee) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime, fee);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("startTime", startTime)
                .add("endTime", endTime)
                .add("fee", fee)
                .toString();
    }
}
