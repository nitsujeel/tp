package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class NameTest {
    private static final String VALID_MAX_LENGTH_NAME = "A".repeat(50);
    private static final String INVALID_TOO_LONG_NAME = "A".repeat(51);


    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Name(null));
    }

    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        String invalidName = "";
        assertThrows(IllegalArgumentException.class, () -> new Name(invalidName));
    }

    @Test
    public void isValidName() {
        // null name
        assertThrows(NullPointerException.class, () -> Name.isValidName(null));

        // invalid name
        assertFalse(Name.isValidName("")); // empty string
        assertFalse(Name.isValidName(" ")); // spaces only
        assertFalse(Name.isValidName(INVALID_TOO_LONG_NAME)); // exceeds 50 characters
        assertFalse(Name.isValidName("  " + INVALID_TOO_LONG_NAME + "  ")); // still exceeds limit after trimming

        // valid name
        assertTrue(Name.isValidName("peter jack")); // alphabets only
        assertTrue(Name.isValidName("12345")); // numbers only
        assertTrue(Name.isValidName("peter the 2nd")); // alphanumeric characters
        assertTrue(Name.isValidName("Capital Tan")); // with capital letters
        assertTrue(Name.isValidName("@@@ --- ???")); // special characters are allowed
        assertTrue(Name.isValidName("John_Doe*#")); // punctuation/symbols are allowed
        assertTrue(Name.isValidName(VALID_MAX_LENGTH_NAME)); // upper bound
        assertTrue(Name.isValidName("  David   Roger  ")); // extra whitespace is normalized
    }

    @Test
    public void constructor_whitespaceNormalized() {
        assertTrue(new Name("  Alice\t  Bob  ").equals(new Name("Alice Bob")));
    }

    @Test
    public void equals() {
        Name name = new Name("Valid Name");

        // same values -> returns true
        assertTrue(name.equals(new Name("Valid Name")));

        // same object -> returns true
        assertTrue(name.equals(name));

        // null -> returns false
        assertFalse(name.equals(null));

        // different types -> returns false
        assertFalse(name.equals(5.0f));

        // different values -> returns false
        assertFalse(name.equals(new Name("Other Valid Name")));
    }
}
