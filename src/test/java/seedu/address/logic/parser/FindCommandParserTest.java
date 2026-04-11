package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.person.FieldContainsKeywordsPredicate;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_nonPrefixedArg_throwsParseException() {
        assertParseFailure(parser, "Alice Bob", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_prefixWithEmptyValue_throwsParseException() {
        assertParseFailure(parser, " on/   ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_ownerIndexPrefix_throwsParseException() {
        assertParseFailure(parser, " oi/a", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new FieldContainsKeywordsPredicate(
                        Optional.of("Alice Bob"),
                        Optional.of("9435"),
                        Optional.of("example.com"),
                        Optional.of("Jurong West"),
                        List.of("friends", "school")));

        assertParseSuccess(parser, " on/Alice Bob ph/9435 em/example.com ad/Jurong West ot/friends ot/school",
                expectedFindCommand);
    }

    @Test
    public void parse_validArgsWithOwnerAndPetFields_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new FieldContainsKeywordsPredicate(
                        Optional.of("Alice"),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        List.of("friends"),
                        Optional.of("Buddy"),
                        Optional.of("Dog"),
                        Optional.of("friendly")));

        assertParseSuccess(parser, " on/Alice ot/friends pn/Buddy ps/Dog pr/friendly",
                expectedFindCommand);
    }

    @Test
    public void parse_valuesWithLongWhitespace_returnsNormalizedFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new FieldContainsKeywordsPredicate(
                        Optional.of("Alice Bob"),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        List.of("friends")));

        assertParseSuccess(parser, " on/  Alice   Bob  ot/  friends  ", expectedFindCommand);
    }

    @Test
    public void parse_ownerAndPetMultiWordTerms_returnsFindCommand() {
        FindCommand expectedFindCommand =
                new FindCommand(new FieldContainsKeywordsPredicate(
                        Optional.of("avi jon"),
                        Optional.empty(),
                        Optional.empty(),
                        Optional.empty(),
                        List.of(),
                        Optional.of("sam"),
                        Optional.empty(),
                        Optional.empty()));

        assertParseSuccess(parser, " on/avi jon pn/sam", expectedFindCommand);
    }

}
