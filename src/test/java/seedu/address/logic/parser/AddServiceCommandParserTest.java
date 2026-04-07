package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SERVICE_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SERVICE_PRICE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddServiceCommand;
import seedu.address.model.service.Service;

public class AddServiceCommandParserTest {
    private static final String VALID_SERVICE_NAME_FUR_TRIM = "Fur trim";
    private static final String VALID_SERVICE_NAME_SHAMPOO = "Shampoo";
    private static final String VALID_SERVICE_PRICE_FUR_TRIM = "25.00";
    private static final String VALID_SERVICE_PRICE_SHAMPOO = "30.50";

    private static final String SERVICE_NAME_DESC_FUR_TRIM = " " + PREFIX_SERVICE_NAME + VALID_SERVICE_NAME_FUR_TRIM;
    private static final String SERVICE_NAME_DESC_SHAMPOO = " " + PREFIX_SERVICE_NAME + VALID_SERVICE_NAME_SHAMPOO;
    private static final String SERVICE_PRICE_DESC_FUR_TRIM = " " + PREFIX_SERVICE_PRICE + VALID_SERVICE_PRICE_FUR_TRIM;
    private static final String SERVICE_PRICE_DESC_SHAMPOO = " " + PREFIX_SERVICE_PRICE + VALID_SERVICE_PRICE_SHAMPOO;

    private static final String INVALID_SERVICE_NAME_DESC = " " + PREFIX_SERVICE_NAME + "@wash";
    private static final String INVALID_SERVICE_PRICE_DESC = " " + PREFIX_SERVICE_PRICE + "-1.00";

    private final AddServiceCommandParser parser = new AddServiceCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Service expectedService = new Service(VALID_SERVICE_NAME_FUR_TRIM,
                Double.parseDouble(VALID_SERVICE_PRICE_FUR_TRIM));

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + SERVICE_NAME_DESC_FUR_TRIM + SERVICE_PRICE_DESC_FUR_TRIM,
                new AddServiceCommand(expectedService));
    }

    @Test
    public void parse_serviceNameWithLongWhitespace_success() {
        Service expectedService = new Service(VALID_SERVICE_NAME_FUR_TRIM,
                Double.parseDouble(VALID_SERVICE_PRICE_FUR_TRIM));

        assertParseSuccess(parser, " " + PREFIX_SERVICE_NAME + "  Fur   trim " + SERVICE_PRICE_DESC_FUR_TRIM,
                new AddServiceCommand(expectedService));
    }

    @Test
    public void parse_repeatedNonServiceValue_failure() {
        String validExpectedServiceString = SERVICE_NAME_DESC_FUR_TRIM + SERVICE_PRICE_DESC_FUR_TRIM;

        // multiple names
        assertParseFailure(parser, SERVICE_NAME_DESC_SHAMPOO + validExpectedServiceString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_SERVICE_NAME));

        // multiple prices
        assertParseFailure(parser, SERVICE_PRICE_DESC_SHAMPOO + validExpectedServiceString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_SERVICE_PRICE));

        // multiple fields repeated
        assertParseFailure(parser,
                validExpectedServiceString + SERVICE_NAME_DESC_SHAMPOO + SERVICE_PRICE_DESC_SHAMPOO
                        + validExpectedServiceString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_SERVICE_NAME, PREFIX_SERVICE_PRICE));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddServiceCommand.MESSAGE_USAGE);

        // missing service name prefix
        assertParseFailure(parser, VALID_SERVICE_NAME_FUR_TRIM + SERVICE_PRICE_DESC_FUR_TRIM, expectedMessage);

        // missing service price prefix
        assertParseFailure(parser, SERVICE_NAME_DESC_FUR_TRIM + VALID_SERVICE_PRICE_FUR_TRIM, expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_SERVICE_NAME_FUR_TRIM + " " + VALID_SERVICE_PRICE_FUR_TRIM, expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid service name
        assertParseFailure(parser, INVALID_SERVICE_NAME_DESC + SERVICE_PRICE_DESC_FUR_TRIM,
                Service.MESSAGE_CONSTRAINTS);

        // invalid service price
        assertParseFailure(parser, SERVICE_NAME_DESC_FUR_TRIM + INVALID_SERVICE_PRICE_DESC,
                Service.MESSAGE_PRICE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + SERVICE_NAME_DESC_FUR_TRIM + SERVICE_PRICE_DESC_FUR_TRIM,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddServiceCommand.MESSAGE_USAGE));
    }
}
