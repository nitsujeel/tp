package seedu.address.model.pet;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class PetNameTest {

    private static final String VALID_MAX_LENGTH_NAME = "ABCDEFGHIJKLMNO";
    private static final String INVALID_TOO_LONG_NAME = "ABCDEFGHIJKLMNOP";

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new PetName(null));
    }

    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new PetName(""));
    }

    @Test
    public void isValidName() {
        assertThrows(NullPointerException.class, () -> PetName.isValidName(null));

        assertFalse(PetName.isValidName(""));
        assertFalse(PetName.isValidName(" "));
        assertFalse(PetName.isValidName("1Buddy"));
        assertFalse(PetName.isValidName("Buddy!"));
        assertFalse(PetName.isValidName(INVALID_TOO_LONG_NAME));

        assertTrue(PetName.isValidName("A"));
        assertTrue(PetName.isValidName("Buddy"));
        assertTrue(PetName.isValidName("O'Malley"));
        assertTrue(PetName.isValidName("Mary-Jane"));
        assertTrue(PetName.isValidName(VALID_MAX_LENGTH_NAME));
        assertTrue(PetName.isValidName("  Mary   Jane  "));
    }

    @Test
    public void constructor_whitespaceNormalized() {
        PetName withExtraWhitespace = new PetName("  Mary   Jane ");
        PetName normalized = new PetName("Mary Jane");

        assertTrue(withExtraWhitespace.equals(normalized));
    }

    @Test
    public void equals() {
        PetName petName = new PetName("Buddy");

        assertTrue(petName.equals(new PetName("Buddy")));
        assertTrue(petName.equals(petName));
        assertFalse(petName.equals(null));
        assertFalse(petName.equals(5));
        assertFalse(petName.equals(new PetName("Bella")));
    }
}
