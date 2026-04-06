package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_END_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OWNER_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SERVICE_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_TIME;

import java.util.List;
import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddSessionCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new AddSessionCommand object.
 */
public class AddSessionCommandParser implements Parser<AddSessionCommand> {

    /**
     * Parses the given {@code String} of arguments and returns an AddSessionCommand.
     *
     * @param args User input arguments
     * @return AddSessionCommand object
     * @throws ParseException If the input does not conform to the expected format
     */
    public AddSessionCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_OWNER_INDEX, PREFIX_PET_INDEX,
                        PREFIX_START_TIME, PREFIX_END_TIME, PREFIX_SERVICE_NAME);

        // Required fields (services are optional)
        if (!arePrefixesPresent(argMultimap, PREFIX_OWNER_INDEX, PREFIX_PET_INDEX,
                PREFIX_START_TIME, PREFIX_END_TIME)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddSessionCommand.MESSAGE_USAGE));
        }

        // Only enforce duplicates for required fields
        argMultimap.verifyNoDuplicatePrefixesFor(
                PREFIX_OWNER_INDEX, PREFIX_PET_INDEX, PREFIX_START_TIME, PREFIX_END_TIME);

        Index ownerIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_OWNER_INDEX).get());
        Index petIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_PET_INDEX).get());
        String startTime = ParserUtil.parseDateTime(argMultimap.getValue(PREFIX_START_TIME).get());
        String endTime = ParserUtil.parseDateTime(argMultimap.getValue(PREFIX_END_TIME).get());

        List<String> serviceNames = argMultimap.getAllValues(PREFIX_SERVICE_NAME);
        return new AddSessionCommand(ownerIndex, petIndex, startTime, endTime, serviceNames);
    }

    /**
     * Returns true if all required prefixes are present.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
