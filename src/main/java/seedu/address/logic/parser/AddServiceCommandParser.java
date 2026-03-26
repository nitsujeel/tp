package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SERVICE_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SERVICE_PRICE;

import java.util.stream.Stream;

import seedu.address.logic.commands.AddServiceCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.service.Service;

/**
 * Parses input arguments and creates a new AddServiceCommand object.
 */
public class AddServiceCommandParser implements Parser<AddServiceCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddServiceCommand
     * and returns an AddServiceCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format.
     */
    @Override
    public AddServiceCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_SERVICE_NAME, PREFIX_SERVICE_PRICE);

        if (!arePrefixesPresent(argMultimap, PREFIX_SERVICE_NAME, PREFIX_SERVICE_PRICE)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddServiceCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_SERVICE_NAME, PREFIX_SERVICE_PRICE);
        String serviceName = ParserUtil.parseServiceName(argMultimap.getValue(PREFIX_SERVICE_NAME).get());
        double servicePrice = ParserUtil.parseServicePrice(argMultimap.getValue(PREFIX_SERVICE_PRICE).get());
        Service newService = new Service(serviceName, servicePrice);

        return new AddServiceCommand(newService);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
