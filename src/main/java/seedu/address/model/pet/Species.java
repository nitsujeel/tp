package seedu.address.model.pet;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.commons.util.StringUtil.normalizeWhitespace;

/**
 * Represents a Pet's species in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidSpecies(String)}
 */
public class Species {

    public static final String MESSAGE_CONSTRAINTS =
            "Species must be 1 to 30 characters.";
    private static final int MIN_LENGTH = 1;
    private static final int MAX_LENGTH = 30;

    public final String value;

    /**
     * Constructs a {@code Species}.
     *
     * @param species A valid species.
     */
    public Species(String species) {
        requireNonNull(species);
        String normalizedSpecies = normalizeWhitespace(species);
        checkArgument(isValidSpecies(normalizedSpecies), MESSAGE_CONSTRAINTS);
        value = normalizedSpecies;
    }

    /**
     * Returns true if a given string is a valid species.
     */
    public static boolean isValidSpecies(String test) {
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
        if (!(other instanceof Species)) {
            return false;
        }

        Species otherSpecies = (Species) other;
        return value.equals(otherSpecies.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
