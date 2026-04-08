package seedu.address.model.tag;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.commons.util.StringUtil.normalizeForComparison;
import static seedu.address.commons.util.StringUtil.normalizeWhitespace;

/**
 * Represents a Tag in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidTagName(String)}
 */
public class Tag {

    public static final String MESSAGE_CONSTRAINTS = "Tag names should be 1 to 20 characters.";
    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 20;

    public final String tagName;

    /**
     * Constructs a {@code Tag}.
     *
     * @param tagName A valid tag name.
     */
    public Tag(String tagName) {
        requireNonNull(tagName);
        String normalizedTagName = normalizeWhitespace(tagName);
        checkArgument(isValidTagName(normalizedTagName), MESSAGE_CONSTRAINTS);
        this.tagName = normalizedTagName;
    }

    /**
     * Returns true if a given string is a valid tag name.
     */
    public static boolean isValidTagName(String test) {
        requireNonNull(test);
        int normalizedLength = normalizeWhitespace(test).length();
        return normalizedLength >= MIN_LENGTH && normalizedLength <= MAX_LENGTH;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Tag)) {
            return false;
        }

        Tag otherTag = (Tag) other;
        return normalizeForComparison(tagName).equals(normalizeForComparison(otherTag.tagName));
    }

    @Override
    public int hashCode() {
        return normalizeForComparison(tagName).hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + tagName + ']';
    }

}
