package seedu.address.model.session;

import static java.util.Objects.requireNonNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents a booked session for a Pet in PetLog.
 * Guarantees: immutable; startTime and endTime are present and not null.
 */
public class Session {

    public static final String MESSAGE_DATETIME_CONSTRAINTS =
            "Session date/time should be in the format yyyy-MM-dd HH:mm and be a valid date and time.";
    public static final String MESSAGE_INVALID_TIME_RANGE =
            "Session end time must be after the session start time.";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm")
            .withResolverStyle(ResolverStyle.STRICT);

    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final double fee;

    /**
     * Constructs a {@code Session} with no fee.
     *
     * @param startTime The start time string of the session.
     * @param endTime   The end time string of the session.
     */
    public Session(String startTime, String endTime) {
        this(parseDateTime(startTime), parseDateTime(endTime), 0.0);
    }

    /**
     * Constructs a {@code Session}.
     *
     * @param startTime The start time string of the session.
     * @param endTime   The end time string of the session.
     * @param fee       The total fee for this session.
     */
    public Session(String startTime, String endTime, double fee) {
        this(parseDateTime(startTime), parseDateTime(endTime), fee);
    }

    /**
     * Constructs a {@code Session}.
     *
     * @param startTime The session start time.
     * @param endTime   The session end time.
     * @param fee       The total fee for this session.
     */
    public Session(LocalDateTime startTime, LocalDateTime endTime, double fee) {
        requireNonNull(startTime);
        requireNonNull(endTime);
        if (!endTime.isAfter(startTime)) {
            throw new IllegalArgumentException(MESSAGE_INVALID_TIME_RANGE);
        }
        this.startTime = startTime;
        this.endTime = endTime;
        this.fee = fee;
    }

    public String getStartTime() {
        return formatDateTime(startTime);
    }

    public String getEndTime() {
        return formatDateTime(endTime);
    }

    /**
     * Returns the start date and time of this session.
     */
    public LocalDateTime getStartDateTime() {
        return startTime;
    }

    /**
     * Returns the end date and time of this session.
     */
    public LocalDateTime getEndDateTime() {
        return endTime;
    }

    public double getFee() {
        return fee;
    }

    /**
     * Returns true if this session overlaps with the other session.
     * Sessions that touch exactly at the boundary do not overlap.
     */
    public boolean overlapsWith(Session otherSession) {
        requireNonNull(otherSession);
        return startTime.isBefore(otherSession.endTime)
                && otherSession.startTime.isBefore(endTime);
    }

    /**
     * Returns true if the given string is a valid session date/time in {@code yyyy-MM-dd HH:mm} format.
     */
    public static boolean isValidDateTime(String dateTime) {
        requireNonNull(dateTime);
        try {
            LocalDateTime.parse(dateTime, FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Parses the given string into a {@code LocalDateTime}.
     *
     * @throws IllegalArgumentException if the input is not a valid session date/time
     */
    public static LocalDateTime parseDateTime(String dateTime) {
        requireNonNull(dateTime);
        try {
            return LocalDateTime.parse(dateTime, FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(MESSAGE_DATETIME_CONSTRAINTS, e);
        }
    }

    /**
     * Formats the given {@code LocalDateTime} into the canonical session date/time string form.
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        requireNonNull(dateTime);
        return dateTime.format(FORMATTER);
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
