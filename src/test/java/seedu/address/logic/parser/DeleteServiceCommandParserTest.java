package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeleteServiceCommand;

public class DeleteServiceCommandParserTest {

    private final DeleteServiceCommandParser parser = new DeleteServiceCommandParser();

    @Test
    public void parse_validServiceIndex_returnsDeleteServiceCommand() {
        assertParseSuccess(parser, " si/1", new DeleteServiceCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteServiceCommand.MESSAGE_USAGE));
    }
}
