package seedu.address.model.pet;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class PetRemarkTest {

    private static final String VALID_MAX_LENGTH_REMARK = "a".repeat(PetRemark.MAX_LENGTH);
    private static final String INVALID_TOO_LONG_REMARK = "a".repeat(PetRemark.MAX_LENGTH + 1);

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new PetRemark(null));
    }

    @Test
    public void constructor_invalidRemark_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new PetRemark(INVALID_TOO_LONG_REMARK));
    }

    @Test
    public void isValidRemark() {
        assertThrows(NullPointerException.class, () -> PetRemark.isValidRemark(null));

        assertTrue(PetRemark.isValidRemark(""));
        assertTrue(PetRemark.isValidRemark("Friendly and energetic"));
        assertTrue(PetRemark.isValidRemark(VALID_MAX_LENGTH_REMARK));

        assertFalse(PetRemark.isValidRemark(INVALID_TOO_LONG_REMARK));
    }

    @Test
    public void constructor_whitespaceNormalized() {
        PetRemark withExtraWhitespace = new PetRemark("  Very\t  friendly   pet ");
        PetRemark normalized = new PetRemark("Very friendly pet");

        assertTrue(withExtraWhitespace.equals(normalized));
    }

    @Test
    public void equals() {
        PetRemark petRemark = new PetRemark("Loves treats");

        assertTrue(petRemark.equals(new PetRemark("Loves treats")));
        assertTrue(petRemark.equals(petRemark));
        assertFalse(petRemark.equals(null));
        assertFalse(petRemark.equals(5));
        assertFalse(petRemark.equals(new PetRemark("Needs extra care")));
    }
}
