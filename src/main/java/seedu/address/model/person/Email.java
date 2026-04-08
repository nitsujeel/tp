package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.commons.util.StringUtil.normalizeWhitespace;

/**
 * Represents a Person's email in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidEmail(String)}
 */
public class Email {

    private static final String SPECIAL_CHARACTERS = "+_.-";
    public static final String MESSAGE_CONSTRAINTS = "Emails should be of the format local-part@domain "
            + "and adhere to the following constraints:\n"
            + "1. The local-part should only contain 1-30 alphanumeric characters and these special characters, "
            + "excluding "
            + "the parentheses, (" + SPECIAL_CHARACTERS + "). The local-part may not start or end with any special "
            + "characters.\n"
            + "2. This is followed by a '@' and then a domain name. The domain name is made up of domain labels "
            + "separated by periods.\n"
            + "The domain name must:\n"
            + "    - be 2-30 characters long in total;\n"
            + "    - end with a domain label at least 2 characters long;\n"
            + "    - have each domain label start and end with alphanumeric characters;\n"
            + "    - have each domain label consist of alphanumeric characters, separated only by hyphens, if any.";
    // Alphanumeric characters except underscore.
    private static final String ALPHANUMERIC_NO_UNDERSCORE = "[^\\W_]+";
    // Local part: 1-30 chars; special characters can only appear between alphanumeric groups.
    private static final String LOCAL_PART_REGEX = "(?=.{1,30}@)" + ALPHANUMERIC_NO_UNDERSCORE
            + "(?:[" + SPECIAL_CHARACTERS + "]" + ALPHANUMERIC_NO_UNDERSCORE + ")*";
    // Each domain label starts/ends with alphanumeric chars, with optional hyphen-separated groups.
    private static final String DOMAIN_LABEL_REGEX = ALPHANUMERIC_NO_UNDERSCORE
            + "(?:-" + ALPHANUMERIC_NO_UNDERSCORE + ")*";
    // Final domain label must be at least 2 characters long.
    private static final String DOMAIN_LAST_LABEL_REGEX = "(?=.{2,}$)" + DOMAIN_LABEL_REGEX;
    // Domain part: 2-30 chars total.
    private static final String DOMAIN_REGEX = "(?=.{2,30}$)(?:" + DOMAIN_LABEL_REGEX + "\\.)*"
            + DOMAIN_LAST_LABEL_REGEX;
    public static final String VALIDATION_REGEX = "^" + LOCAL_PART_REGEX + "@" + DOMAIN_REGEX + "$";

    public final String value;

    /**
     * Constructs an {@code Email}.
     *
     * @param email A valid email address.
     */
    public Email(String email) {
        requireNonNull(email);
        String normalizedEmail = normalizeWhitespace(email);
        checkArgument(isValidEmail(normalizedEmail), MESSAGE_CONSTRAINTS);
        value = normalizedEmail;
    }

    /**
     * Returns if a given string is a valid email.
     */
    public static boolean isValidEmail(String test) {
        requireNonNull(test);
        return normalizeWhitespace(test).matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Email)) {
            return false;
        }

        Email otherEmail = (Email) other;
        return value.equals(otherEmail.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
