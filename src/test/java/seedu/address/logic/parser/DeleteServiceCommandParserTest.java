package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeleteServiceCommand;

public class DeleteServiceCommandParserTest {

    private final DeleteServiceCommandParser parser = new DeleteServiceCommandParser();

    @Test
    public void parse_validServiceName_returnsDeleteServiceCommand() {
        assertParseSuccess(parser, " sn/Fur trim", new DeleteServiceCommand("Fur trim"));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteServiceCommand.MESSAGE_USAGE));
    }
}
