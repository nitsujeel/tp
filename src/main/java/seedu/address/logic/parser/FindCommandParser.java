package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OWNER_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET_REMARK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SPECIES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import seedu.address.commons.util.StringUtil;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.FieldContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_OWNER_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS,
                        PREFIX_TAG, PREFIX_PET_NAME, PREFIX_SPECIES, PREFIX_PET_REMARK);

        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_OWNER_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS,
                PREFIX_PET_NAME, PREFIX_SPECIES, PREFIX_PET_REMARK);

        Optional<String> ownerNameKeyword = parseOptionalSearchString(argMultimap, PREFIX_OWNER_NAME);
        Optional<String> phoneKeyword = parseOptionalSearchString(argMultimap, PREFIX_PHONE);
        Optional<String> emailKeyword = parseOptionalSearchString(argMultimap, PREFIX_EMAIL);
        Optional<String> addressKeyword = parseOptionalSearchString(argMultimap, PREFIX_ADDRESS);
        List<String> tagKeywords = parseTagSearchStrings(argMultimap.getAllValues(PREFIX_TAG));
        Optional<String> petNameKeyword = parseOptionalSearchString(argMultimap, PREFIX_PET_NAME);
        Optional<String> speciesKeyword = parseOptionalSearchString(argMultimap, PREFIX_SPECIES);
        Optional<String> petRemarkKeyword = parseOptionalSearchString(argMultimap, PREFIX_PET_REMARK);

        if (ownerNameKeyword.isEmpty() && phoneKeyword.isEmpty() && emailKeyword.isEmpty() && addressKeyword.isEmpty()
                && tagKeywords.isEmpty() && petNameKeyword.isEmpty()
                && speciesKeyword.isEmpty() && petRemarkKeyword.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        return new FindCommand(new FieldContainsKeywordsPredicate(ownerNameKeyword, phoneKeyword, emailKeyword,
                addressKeyword, tagKeywords, petNameKeyword, speciesKeyword, petRemarkKeyword));
    }

    private static Optional<String> parseOptionalSearchString(ArgumentMultimap argMultimap, Prefix prefix)
            throws ParseException {
        Optional<String> value = argMultimap.getValue(prefix).map(StringUtil::normalizeWhitespace);

        if (value.isPresent() && value.get().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        return value;
    }

    private static List<String> parseTagSearchStrings(List<String> rawTagSearchStrings) throws ParseException {
        List<String> parsedTagSearchStrings = new ArrayList<>();
        for (String rawTagSearchString : rawTagSearchStrings) {
            String normalizedTagSearchString = StringUtil.normalizeWhitespace(rawTagSearchString);
            if (normalizedTagSearchString.isEmpty()) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
            }
            parsedTagSearchStrings.add(normalizedTagSearchString);
        }
        return parsedTagSearchStrings;
    }

}
