package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class TagTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Tag(null));
    }

    @Test
    public void constructor_invalidTagName_throwsIllegalArgumentException() {
        String invalidTagName = "";
        assertThrows(IllegalArgumentException.class, () -> new Tag(invalidTagName));
    }

    @Test
    public void isValidTagName() {
        // null tag name
        assertThrows(NullPointerException.class, () -> Tag.isValidTagName(null));

        // invalid tag names
        assertFalse(Tag.isValidTagName("")); // empty
        assertFalse(Tag.isValidTagName("tag_name")); // underscore
        assertFalse(Tag.isValidTagName("tag-name")); // hyphen
        assertFalse(Tag.isValidTagName("123456789012345678901")); // 21 chars

        // valid tag names
        assertTrue(Tag.isValidTagName("a")); // 1 char
        assertTrue(Tag.isValidTagName("abc123")); // alphanumeric
        assertTrue(Tag.isValidTagName("12345678901234567890")); // 20 chars
        assertTrue(Tag.isValidTagName("  abc123  ")); // leading/trailing whitespace is normalized
    }

    @Test
    public void equals_caseInsensitiveAndWhitespaceInsensitive() {
        Tag friend = new Tag("Friend");
        Tag friendLower = new Tag("friend");
        Tag friendWithWhitespace = new Tag("  friend ");

        assertTrue(friend.equals(friendLower));
        assertTrue(friend.equals(friendWithWhitespace));
        assertFalse(friend.equals(new Tag("family")));
    }

}
