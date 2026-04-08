package seedu.address.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.person.Person;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command.";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format.\n%1$s";
    public static final String MESSAGE_INVALID_OWNER_DISPLAYED_INDEX = "Owner index is invalid.";
    public static final String MESSAGE_INVALID_PET_DISPLAYED_INDEX = "Pet index is invalid.";
    public static final String MESSAGE_INVALID_SESSION_DISPLAYED_INDEX = "Session index is invalid.";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "Listed %1$d owner(s).";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Duplicate values provided for single-valued field(s): ";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields) + ".";
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        return person.toString();
    }

}
