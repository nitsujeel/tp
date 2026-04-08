package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.pet.PetName;
import seedu.address.model.pet.Species;
import seedu.address.model.service.Service;
import seedu.address.model.tag.Tag;

public class ParserUtilTest {
    private static final String INVALID_NAME = "A".repeat(51);
    private static final String INVALID_PHONE = "1";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_TOO_LONG_ADDRESS = "A".repeat(101);
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "A".repeat(21);
    private static final String INVALID_PET_NAME = " ";
    private static final String INVALID_TOO_LONG_PET_NAME = "A".repeat(31);
    private static final String INVALID_SPECIES = " ";
    private static final String INVALID_TOO_LONG_SPECIES = "A".repeat(31);
    private static final String INVALID_SERVICE_NAME = "A".repeat(31);
    private static final String INVALID_SERVICE_PRICE = "-5.00";
    private static final String INVALID_SERVICE_PRICE_NON_DIGIT = "12a";
    private static final String INVALID_SERVICE_PRICE_TOO_HIGH = "10000.01";
    private static final String INVALID_SERVICE_PRICE_MORE_THAN_TWO_DP = "20.123";
    private static final String INVALID_DATE_TIME = "2026-02-30 10:00";
    private static final String INVALID_DATE_TIME_FORMAT = "2026/03/25 10:00";

    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_PHONE = "123456";
    private static final String VALID_ADDRESS = "123 Main Street #0505";
    private static final String VALID_EMAIL = "rachel@example.com";
    private static final String VALID_TAG_1 = "friend";
    private static final String VALID_TAG_2 = "neighbour";
    private static final String VALID_PET_NAME = "Buddy";
    private static final String VALID_SPECIES = "Dog";
    private static final String VALID_SERVICE_NAME = "Fur trim";
    private static final String VALID_SERVICE_NAME_WITH_SPECIAL_CHARACTERS = "@wash!*";
    private static final String VALID_SERVICE_PRICE = "25.50";
    private static final String VALID_MAX_SERVICE_PRICE = "10000.00";
    private static final String VALID_DATE_TIME = "2026-03-25 10:00";

    private static final String WHITESPACE = " \t\r\n";

    @Test
    public void parseIndex_invalidInput_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseIndex("10 a"));
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_INVALID_INDEX, ()
            -> ParserUtil.parseIndex(Long.toString(Integer.MAX_VALUE + 1)));
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST_PERSON, ParserUtil.parseIndex("  1  "));
    }

    @Test
    public void parseName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseName((String) null));
    }

    @Test
    public void parseName_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseName(INVALID_NAME));
    }

    @Test
    public void parseName_validValueWithoutWhitespace_returnsName() throws Exception {
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(VALID_NAME));
    }

    @Test
    public void parseName_validValueWithSpecialCharacters_returnsName() throws Exception {
        String nameWithSpecialCharacters = "R@chel #1 (VIP)";
        Name expectedName = new Name(nameWithSpecialCharacters);
        assertEquals(expectedName, ParserUtil.parseName(nameWithSpecialCharacters));
    }

    @Test
    public void parseName_validValueWithWhitespace_returnsTrimmedName() throws Exception {
        String nameWithWhitespace = WHITESPACE + VALID_NAME + WHITESPACE;
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(nameWithWhitespace));
    }

    @Test
    public void parseName_validValueWithLongWhitespace_returnsNormalizedName() throws Exception {
        Name expectedName = new Name("Rachel Walker");
        assertEquals(expectedName, ParserUtil.parseName(" Rachel   \tWalker "));
    }

    @Test
    public void parsePhone_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parsePhone((String) null));
    }

    @Test
    public void parsePhone_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePhone(INVALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithoutWhitespace_returnsPhone() throws Exception {
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(VALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithSpecialCharacters_returnsPhone() throws Exception {
        String phoneWithSpecialCharacters = "abc-123/45";
        Phone expectedPhone = new Phone(phoneWithSpecialCharacters);
        assertEquals(expectedPhone, ParserUtil.parsePhone(phoneWithSpecialCharacters));
    }

    @Test
    public void parsePhone_validValueWithWhitespace_returnsTrimmedPhone() throws Exception {
        String phoneWithWhitespace = WHITESPACE + VALID_PHONE + WHITESPACE;
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(phoneWithWhitespace));
    }

    @Test
    public void parsePhone_validValueWithLongWhitespace_returnsNormalizedPhone() throws Exception {
        Phone expectedPhone = new Phone("123 456");
        assertEquals(expectedPhone, ParserUtil.parsePhone("123 \t 456"));
    }

    @Test
    public void parseAddress_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseAddress((String) null));
    }

    @Test
    public void parseAddress_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseAddress(INVALID_ADDRESS));
    }

    @Test
    public void parseAddress_tooLongValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseAddress(INVALID_TOO_LONG_ADDRESS));
    }

    @Test
    public void parseAddress_validValueWithoutWhitespace_returnsAddress() throws Exception {
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress, ParserUtil.parseAddress(VALID_ADDRESS));
    }

    @Test
    public void parseAddress_validValueWithSpecialCharacters_returnsAddress() throws Exception {
        String addressWithSpecialCharacters = "Unit #05-01 @ Pet-Hub / Block A";
        Address expectedAddress = new Address(addressWithSpecialCharacters);
        assertEquals(expectedAddress, ParserUtil.parseAddress(addressWithSpecialCharacters));
    }

    @Test
    public void parseAddress_validValueWithWhitespace_returnsTrimmedAddress() throws Exception {
        String addressWithWhitespace = WHITESPACE + VALID_ADDRESS + WHITESPACE;
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress, ParserUtil.parseAddress(addressWithWhitespace));
    }

    @Test
    public void parseAddress_validValueWithLongWhitespace_returnsNormalizedAddress() throws Exception {
        Address expectedAddress = new Address("123 Main Street #0505");
        assertEquals(expectedAddress, ParserUtil.parseAddress("123  Main\tStreet   #0505"));
    }

    @Test
    public void parseEmail_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseEmail((String) null));
    }

    @Test
    public void parseEmail_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseEmail(INVALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithoutWhitespace_returnsEmail() throws Exception {
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(VALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithWhitespace_returnsTrimmedEmail() throws Exception {
        String emailWithWhitespace = WHITESPACE + VALID_EMAIL + WHITESPACE;
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(emailWithWhitespace));
    }

    @Test
    public void parseTag_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTag(null));
    }

    @Test
    public void parseTag_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTag(INVALID_TAG));
    }

    @Test
    public void parseTag_validValueWithoutWhitespace_returnsTag() throws Exception {
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(VALID_TAG_1));
    }

    @Test
    public void parseTag_validValueWithSpecialCharacters_returnsTag() throws Exception {
        String tagWithSpecialCharacters = "#friend!";
        Tag expectedTag = new Tag(tagWithSpecialCharacters);
        assertEquals(expectedTag, ParserUtil.parseTag(tagWithSpecialCharacters));
    }

    @Test
    public void parseTag_validValueWithWhitespace_returnsTrimmedTag() throws Exception {
        String tagWithWhitespace = WHITESPACE + VALID_TAG_1 + WHITESPACE;
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(tagWithWhitespace));
    }

    @Test
    public void parseTags_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTags(null));
    }

    @Test
    public void parseTags_collectionWithInvalidTags_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, INVALID_TAG)));
    }

    @Test
    public void parseTags_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseTags(Collections.emptyList()).isEmpty());
    }

    @Test
    public void parseTags_collectionWithValidTags_returnsTagSet() throws Exception {
        Set<Tag> actualTagSet = ParserUtil.parseTags(Arrays.asList(VALID_TAG_1, VALID_TAG_2));
        Set<Tag> expectedTagSet = new HashSet<Tag>(Arrays.asList(new Tag(VALID_TAG_1), new Tag(VALID_TAG_2)));

        assertEquals(expectedTagSet, actualTagSet);
    }

    @Test
    public void parsePetName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parsePetName(null));
    }

    @Test
    public void parsePetName_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePetName(INVALID_PET_NAME));
    }

    @Test
    public void parsePetName_tooLongValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePetName(INVALID_TOO_LONG_PET_NAME));
    }

    @Test
    public void parsePetName_validValueWithoutWhitespace_returnsPetName() throws Exception {
        PetName expectedPetName = new PetName(VALID_PET_NAME);
        assertEquals(expectedPetName, ParserUtil.parsePetName(VALID_PET_NAME));
    }

    @Test
    public void parsePetName_validValueWithSpecialCharacters_returnsPetName() throws Exception {
        String petNameWithSpecialCharacters = "@Buddy#1!";
        PetName expectedPetName = new PetName(petNameWithSpecialCharacters);
        assertEquals(expectedPetName, ParserUtil.parsePetName(petNameWithSpecialCharacters));
    }

    @Test
    public void parsePetName_validValueWithWhitespace_returnsTrimmedPetName() throws Exception {
        String petNameWithWhitespace = WHITESPACE + VALID_PET_NAME + WHITESPACE;
        PetName expectedPetName = new PetName(VALID_PET_NAME);
        assertEquals(expectedPetName, ParserUtil.parsePetName(petNameWithWhitespace));
    }

    @Test
    public void parsePetName_validValueWithLongWhitespace_returnsNormalizedPetName() throws Exception {
        PetName expectedPetName = new PetName("Mary Jane");
        assertEquals(expectedPetName, ParserUtil.parsePetName(" Mary   \t Jane "));
    }

    @Test
    public void parseSpecies_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseSpecies(null));
    }

    @Test
    public void parseSpecies_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseSpecies(INVALID_SPECIES));
    }

    @Test
    public void parseSpecies_tooLongValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseSpecies(INVALID_TOO_LONG_SPECIES));
    }

    @Test
    public void parseSpecies_validValueWithoutWhitespace_returnsSpecies() throws Exception {
        Species expectedSpecies = new Species(VALID_SPECIES);
        assertEquals(expectedSpecies, ParserUtil.parseSpecies(VALID_SPECIES));
    }

    @Test
    public void parseSpecies_validValueWithSpecialCharacters_returnsSpecies() throws Exception {
        String speciesWithSpecialCharacters = "D0g-@Home";
        Species expectedSpecies = new Species(speciesWithSpecialCharacters);
        assertEquals(expectedSpecies, ParserUtil.parseSpecies(speciesWithSpecialCharacters));
    }

    @Test
    public void parseSpecies_validValueWithWhitespace_returnsTrimmedSpecies() throws Exception {
        String speciesWithWhitespace = WHITESPACE + VALID_SPECIES + WHITESPACE;
        Species expectedSpecies = new Species(VALID_SPECIES);
        assertEquals(expectedSpecies, ParserUtil.parseSpecies(speciesWithWhitespace));
    }

    @Test
    public void parseSpecies_validValueWithLongWhitespace_returnsNormalizedSpecies() throws Exception {
        Species expectedSpecies = new Species("Sea Lion");
        assertEquals(expectedSpecies, ParserUtil.parseSpecies(" Sea   \t Lion "));
    }

    @Test
    public void parseServiceName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseServiceName(null));
    }

    @Test
    public void parseServiceName_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseServiceName(INVALID_SERVICE_NAME));
    }

    @Test
    public void parseServiceName_validValueWithWhitespace_returnsTrimmedServiceName() throws Exception {
        String serviceNameWithWhitespace = WHITESPACE + VALID_SERVICE_NAME + WHITESPACE;
        assertEquals(VALID_SERVICE_NAME, ParserUtil.parseServiceName(serviceNameWithWhitespace));
    }

    @Test
    public void parseServiceName_validValueWithSpecialCharacters_returnsServiceName() throws Exception {
        assertEquals(VALID_SERVICE_NAME_WITH_SPECIAL_CHARACTERS,
                ParserUtil.parseServiceName(VALID_SERVICE_NAME_WITH_SPECIAL_CHARACTERS));
    }

    @Test
    public void parseServiceName_validValueWithLongWhitespace_returnsNormalizedServiceName() throws Exception {
        assertEquals(VALID_SERVICE_NAME, ParserUtil.parseServiceName(" Fur   \t trim "));
    }

    @Test
    public void parseServicePrice_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseServicePrice(null));
    }

    @Test
    public void parseServicePrice_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, Service.MESSAGE_PRICE_CONSTRAINTS, ()
                -> ParserUtil.parseServicePrice(INVALID_SERVICE_PRICE));
    }

    @Test
    public void parseServicePrice_nonDigitValue_throwsParseException() {
        assertThrows(ParseException.class, Service.MESSAGE_PRICE_CONSTRAINTS, ()
                -> ParserUtil.parseServicePrice(INVALID_SERVICE_PRICE_NON_DIGIT));
    }

    @Test
    public void parseServicePrice_tooHighValue_throwsParseException() {
        assertThrows(ParseException.class, Service.MESSAGE_PRICE_CONSTRAINTS, ()
                -> ParserUtil.parseServicePrice(INVALID_SERVICE_PRICE_TOO_HIGH));
    }

    @Test
    public void parseServicePrice_moreThanTwoDp_throwsParseException() {
        assertThrows(ParseException.class, Service.MESSAGE_PRICE_CONSTRAINTS, ()
                -> ParserUtil.parseServicePrice(INVALID_SERVICE_PRICE_MORE_THAN_TWO_DP));
    }

    @Test
    public void parseServicePrice_validValueWithWhitespace_returnsServicePrice() throws Exception {
        String servicePriceWithWhitespace = WHITESPACE + VALID_SERVICE_PRICE + WHITESPACE;
        assertEquals(25.50, ParserUtil.parseServicePrice(servicePriceWithWhitespace), 1e-9);
    }

    @Test
    public void parseServicePrice_validUpperBound_returnsServicePrice() throws Exception {
        assertEquals(10000.00, ParserUtil.parseServicePrice(VALID_MAX_SERVICE_PRICE), 1e-9);
    }

    @Test
    public void parseDateTime_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseDateTime(null));
    }

    @Test
    public void parseDateTime_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseDateTime(INVALID_DATE_TIME));
        assertThrows(ParseException.class, () -> ParserUtil.parseDateTime(INVALID_DATE_TIME_FORMAT));
    }

    @Test
    public void parseDateTime_validValueWithWhitespace_returnsTrimmedDateTime() throws Exception {
        String dateTimeWithWhitespace = WHITESPACE + VALID_DATE_TIME + WHITESPACE;
        assertEquals(VALID_DATE_TIME, ParserUtil.parseDateTime(dateTimeWithWhitespace));
    }

    @Test
    public void parseDateTime_validValueWithLongWhitespace_returnsNormalizedDateTime() throws Exception {
        assertEquals(VALID_DATE_TIME, ParserUtil.parseDateTime("2026-03-25   \t 10:00"));
    }
}
