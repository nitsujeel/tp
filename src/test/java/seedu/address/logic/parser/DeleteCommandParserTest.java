package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OWNER_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SERVICE_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SESSION_INDEX;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.model.service.Service;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the DeleteCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the DeleteCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class DeleteCommandParserTest {

    private DeleteCommandParser parser = new DeleteCommandParser();

    @Test
    public void parse_validOwnerIndex_returnsDeleteCommand() {
        assertParseSuccess(parser, " oi/1", new DeleteCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_validOwnerAndPetIndex_returnsDeleteCommand() {
        assertParseSuccess(parser, " oi/1 pi/2", new DeleteCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON));
    }

    @Test
    public void parse_validOwnerPetAndSessionIndex_returnsDeleteCommand() {
        assertParseSuccess(parser, " oi/1 pi/2 si/1",
                new DeleteCommand(INDEX_FIRST_PERSON, INDEX_SECOND_PERSON, INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_validServiceName_returnsDeleteCommand() {
        assertParseSuccess(parser, " sn/Fur trim", new DeleteCommand("Fur trim"));
    }

    @Test
    public void parse_validServiceNameWithLongWhitespace_returnsDeleteCommand() {
        assertParseSuccess(parser, " sn/  Fur   trim ", new DeleteCommand("Fur trim"));
    }

    @Test
    public void parse_missingRequiredPrefixes_throwsParseException() {
        assertParseFailure(parser, " pi/1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        assertParseFailure(parser, " si/1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_sessionWithoutPetPrefix_throwsParseException() {
        assertParseFailure(parser, " oi/1 si/1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_ownerAndServicePrefixesTogether_throwsParseException() {
        assertParseFailure(parser, " oi/1 sn/Fur trim",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_repeatedPrefixes_throwsParseException() {
        assertParseFailure(parser, " oi/1 oi/2",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_OWNER_INDEX));
        assertParseFailure(parser, " oi/1 pi/1 pi/2",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PET_INDEX));
        assertParseFailure(parser, " oi/1 pi/1 si/1 si/2",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_SESSION_INDEX));
        assertParseFailure(parser, " sn/Fur trim sn/Shampoo",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_SERVICE_NAME));
    }

    @Test
    public void parse_invalidIndices_throwsParseException() {
        assertParseFailure(parser, " oi/0", MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, " oi/1 pi/0", MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, " oi/1 pi/1 si/0", MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_invalidServiceName_throwsParseException() {
        assertParseFailure(parser, " sn/" + "A".repeat(31), Service.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_nonEmptyPreamble_throwsParseException() {
        assertParseFailure(parser, " stray oi/1 " + PREFIX_PET_INDEX + "1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a", String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
    }
}
