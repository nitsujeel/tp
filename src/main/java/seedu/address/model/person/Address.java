package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.commons.util.StringUtil.normalizeWhitespace;

/**
 * Represents an owner's address.
 * Guarantees: immutable; is valid as declared in {@link #isValidAddress(String)}
 */
public class Address {

    public static final String MESSAGE_CONSTRAINTS = "Address must be 1 to 100 characters.";
    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 100;

    public final String value;

    /**
     * Constructs an {@code Address}.
     *
     * @param address A valid address.
     */
    public Address(String address) {
        requireNonNull(address);
        String normalizedAddress = normalizeWhitespace(address);
        checkArgument(isValidAddress(normalizedAddress), MESSAGE_CONSTRAINTS);
        value = normalizedAddress;
    }

    /**
     * Returns true if a given string is a valid address.
     */
    public static boolean isValidAddress(String test) {
        requireNonNull(test);
        int normalizedLength = normalizeWhitespace(test).length();
        return normalizedLength >= MIN_LENGTH && normalizedLength <= MAX_LENGTH;
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
        if (!(other instanceof Address)) {
            return false;
        }

        Address otherAddress = (Address) other;
        return value.equals(otherAddress.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
