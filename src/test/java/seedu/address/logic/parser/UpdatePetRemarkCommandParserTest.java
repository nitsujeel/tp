package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OWNER_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET_REMARK;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.UpdatePetRemarkCommand;
import seedu.address.model.pet.PetRemark;

public class UpdatePetRemarkCommandParserTest {

    private static final String VALID_REMARK = "Needs gentle handling";
    private static final String VALID_LONG_REMARK = "a".repeat(100);
    private static final String INVALID_LONG_REMARK = "a".repeat(101);

    private final UpdatePetRemarkCommandParser parser = new UpdatePetRemarkCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        assertParseSuccess(parser::parse,
                " oi/1 pi/2 pr/" + VALID_REMARK,
                new UpdatePetRemarkCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON, VALID_REMARK));

        assertParseSuccess(parser::parse,
                " oi/1 pi/1 pr/" + VALID_LONG_REMARK,
                new UpdatePetRemarkCommand(INDEX_FIRST_PERSON, INDEX_FIRST_PERSON, VALID_LONG_REMARK));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, UpdatePetRemarkCommand.MESSAGE_USAGE);

        assertParseFailure(parser::parse, " pi/1 pr/" + VALID_REMARK, expectedMessage);
        assertParseFailure(parser::parse, " oi/1 pr/" + VALID_REMARK, expectedMessage);
        assertParseFailure(parser::parse, " oi/1 pi/1", expectedMessage);
        assertParseFailure(parser::parse, " 1 1 " + VALID_REMARK, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser::parse, " oi/0 pi/1 pr/" + VALID_REMARK, MESSAGE_INVALID_INDEX);
        assertParseFailure(parser::parse, " oi/1 pi/zero pr/" + VALID_REMARK, MESSAGE_INVALID_INDEX);
        assertParseFailure(parser::parse, " oi/1 pi/1 pr/" + INVALID_LONG_REMARK,
                PetRemark.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_repeatedPrefixes_failure() {
        String validArgs = " oi/1 pi/1 pr/" + VALID_REMARK;

        assertParseFailure(parser::parse, " oi/2" + validArgs,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_OWNER_INDEX));
        assertParseFailure(parser::parse, " pi/2" + validArgs,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PET_INDEX));
        assertParseFailure(parser::parse, " pr/another" + validArgs,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PET_REMARK));
    }
}
