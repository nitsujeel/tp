package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SERVICE_INDEX;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteServiceCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteServiceCommand object.
 */
public class DeleteServiceCommandParser implements Parser<DeleteServiceCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteServiceCommand
     * and returns a DeleteServiceCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format.
     */
    @Override
    public DeleteServiceCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_SERVICE_INDEX);

        try {
            if (argMultimap.getValue(PREFIX_SERVICE_INDEX).isEmpty() || !argMultimap.getPreamble().isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        DeleteServiceCommand.MESSAGE_USAGE));
            }

            argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_SERVICE_INDEX);
            Index serviceIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_SERVICE_INDEX).get());
            return new DeleteServiceCommand(serviceIndex);
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    DeleteServiceCommand.MESSAGE_USAGE), pe);
        }
    }
}
