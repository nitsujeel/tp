package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class PhoneTest {
    private static final String VALID_MAX_LENGTH_PHONE = "1".repeat(30);
    private static final String INVALID_TOO_LONG_PHONE = "1".repeat(31);

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Phone(null));
    }

    @Test
    public void constructor_invalidPhone_throwsIllegalArgumentException() {
        String invalidPhone = "";
        assertThrows(IllegalArgumentException.class, () -> new Phone(invalidPhone));
    }

    @Test
    public void isValidPhone() {
        // null phone number
        assertThrows(NullPointerException.class, () -> Phone.isValidPhone(null));

        // invalid phone numbers
        assertFalse(Phone.isValidPhone("")); // empty string
        assertFalse(Phone.isValidPhone(" ")); // spaces only
        assertFalse(Phone.isValidPhone("1")); // less than 2 characters
        assertFalse(Phone.isValidPhone(INVALID_TOO_LONG_PHONE)); // exceeds 30 characters
        assertFalse(Phone.isValidPhone("  " + INVALID_TOO_LONG_PHONE + "  ")); // still exceeds limit after trim

        // valid phone numbers
        assertTrue(Phone.isValidPhone("12")); // exactly 2 characters
        assertTrue(Phone.isValidPhone("93121534"));
        assertTrue(Phone.isValidPhone("phone")); // letters are allowed
        assertTrue(Phone.isValidPhone("9011p041")); // alphanumeric values are allowed
        assertTrue(Phone.isValidPhone("9312/1534")); // slash is allowed
        assertTrue(Phone.isValidPhone(VALID_MAX_LENGTH_PHONE)); // upper bound
        assertTrue(Phone.isValidPhone("+651234567890")); // with +
        assertTrue(Phone.isValidPhone("65-1234-5678")); // with -
        assertTrue(Phone.isValidPhone("1234 5678")); // with space
        assertTrue(Phone.isValidPhone("  1234   5678  ")); // extra whitespace is normalized
    }

    @Test
    public void constructor_whitespaceNormalized() {
        Phone withExtraWhitespace = new Phone("  1234   5678 ");
        Phone normalized = new Phone("1234 5678");
        assertTrue(withExtraWhitespace.equals(normalized));
    }

    @Test
    public void hasOnlyDigits() {
        assertTrue(new Phone("123456").hasOnlyDigits());
        assertFalse(new Phone("123 456").hasOnlyDigits());
        assertFalse(new Phone("123-456").hasOnlyDigits());
        assertFalse(new Phone("phone").hasOnlyDigits());
    }

    @Test
    public void equals() {
        Phone phone = new Phone("999");

        // same values -> returns true
        assertTrue(phone.equals(new Phone("999")));

        // same object -> returns true
        assertTrue(phone.equals(phone));

        // null -> returns false
        assertFalse(phone.equals(null));

        // different types -> returns false
        assertFalse(phone.equals(5.0f));

        // different values -> returns false
        assertFalse(phone.equals(new Phone("995")));
    }
}
