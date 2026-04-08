package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OWNER_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SERVICE_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SESSION_INDEX;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_OWNER_INDEX, PREFIX_PET_INDEX,
                PREFIX_SESSION_INDEX, PREFIX_SERVICE_NAME);

        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_OWNER_INDEX, PREFIX_PET_INDEX,
                PREFIX_SESSION_INDEX, PREFIX_SERVICE_NAME);

        boolean hasOwnerIndex = argMultimap.getValue(PREFIX_OWNER_INDEX).isPresent();
        boolean hasServiceName = argMultimap.getValue(PREFIX_SERVICE_NAME).isPresent();
        boolean hasPetIndex = argMultimap.getValue(PREFIX_PET_INDEX).isPresent();
        boolean hasSessionIndex = argMultimap.getValue(PREFIX_SESSION_INDEX).isPresent();

        if (hasServiceName) {
            if (hasOwnerIndex || hasPetIndex || hasSessionIndex) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
            }
            String serviceName = ParserUtil.parseServiceName(argMultimap.getValue(PREFIX_SERVICE_NAME).get());
            return new DeleteCommand(serviceName);
        }

        if (!hasOwnerIndex) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        Index ownerIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_OWNER_INDEX).get());

        if (hasSessionIndex && !hasPetIndex) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }

        if (hasPetIndex) {
            Index petIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_PET_INDEX).get());
            if (hasSessionIndex) {
                Index sessionIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_SESSION_INDEX).get());
                return new DeleteCommand(ownerIndex, petIndex, sessionIndex);
            }
            return new DeleteCommand(ownerIndex, petIndex);
        }

        return new DeleteCommand(ownerIndex);
    }

}
