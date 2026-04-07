package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OWNER_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET_REMARK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SPECIES;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddPetCommand;
import seedu.address.model.pet.Pet;
import seedu.address.model.pet.PetName;
import seedu.address.model.pet.PetRemark;
import seedu.address.model.pet.Species;

public class AddPetCommandParserTest {

    private static final String VALID_MIN_NAME = "A";
    private static final String VALID_MAX_NAME = "ABCDEFGHIJKLMNO";
    private static final String VALID_MIN_SPECIES = "C";
    private static final String VALID_MAX_SPECIES = "ABCDEFGHIJKLMNO";
    private static final String VALID_MAX_REMARK = "a".repeat(300);
    private static final String INVALID_ZERO_INDEX = "0";
    private static final String INVALID_NON_NUMERIC_INDEX = "one";
    private static final String INVALID_LONG_NAME = "ABCDEFGHIJKLMNOP";
    private static final String INVALID_LONG_SPECIES = "ABCDEFGHIJKLMNOP";
    private static final String INVALID_LONG_REMARK = "a".repeat(301);

    private final AddPetCommandParser parser = new AddPetCommandParser();

    @Test
    public void parse_validArgs_returnsAddPetCommand() {
        // Required fields only, using lower boundary values.
        assertParseSuccess(parser,
                " oi/1 pn/" + VALID_MIN_NAME + " ps/" + VALID_MIN_SPECIES,
                new AddPetCommand(INDEX_FIRST_PERSON,
                        new Pet(new PetName(VALID_MIN_NAME), new Species(VALID_MIN_SPECIES), new PetRemark(""))));

        // Optional remark present, using upper boundary values.
        assertParseSuccess(parser,
                " oi/1 pn/" + VALID_MAX_NAME + " ps/" + VALID_MAX_SPECIES + " pr/" + VALID_MAX_REMARK,
                new AddPetCommand(INDEX_FIRST_PERSON,
                        new Pet(new PetName(VALID_MAX_NAME), new Species(VALID_MAX_SPECIES),
                                new PetRemark(VALID_MAX_REMARK))));
    }

    @Test
    public void parse_validArgsWithLongWhitespace_returnsNormalizedAddPetCommand() {
        assertParseSuccess(parser,
                " oi/1 pn/  Mary   Jane  ps/  Sea   Lion  pr/  Very\t  calm  ",
                new AddPetCommand(INDEX_FIRST_PERSON,
                        new Pet(new PetName("Mary Jane"), new Species("Sea Lion"),
                                new PetRemark("Very calm"))));
    }

    @Test
    public void parse_missingCompulsoryPrefixes_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPetCommand.MESSAGE_USAGE);

        assertParseFailure(parser, " pn/" + VALID_MIN_NAME + " ps/" + VALID_MIN_SPECIES, expectedMessage);
        assertParseFailure(parser, " oi/1 ps/" + VALID_MIN_SPECIES, expectedMessage);
        assertParseFailure(parser, " oi/1 pn/" + VALID_MIN_NAME, expectedMessage);
        assertParseFailure(parser, " 1 " + VALID_MIN_NAME + " " + VALID_MIN_SPECIES, expectedMessage);
    }

    @Test
    public void parse_repeatedPrefixes_failure() {
        String validArgs = " oi/1 pn/" + VALID_MIN_NAME + " ps/" + VALID_MIN_SPECIES + " pr/ok";

        assertParseFailure(parser, " oi/2" + validArgs,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_OWNER_INDEX));
        assertParseFailure(parser, " pn/B" + validArgs,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PET_NAME));
        assertParseFailure(parser, " ps/Dog" + validArgs,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_SPECIES));
        assertParseFailure(parser, " pr/again" + validArgs,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PET_REMARK));
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, " oi/" + INVALID_ZERO_INDEX + " pn/" + VALID_MIN_NAME + " ps/" + VALID_MIN_SPECIES,
                MESSAGE_INVALID_INDEX);
        assertParseFailure(parser,
                " oi/" + INVALID_NON_NUMERIC_INDEX + " pn/" + VALID_MIN_NAME + " ps/" + VALID_MIN_SPECIES,
                MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, " oi/1 pn/" + INVALID_LONG_NAME + " ps/" + VALID_MIN_SPECIES,
                PetName.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, " oi/1 pn/" + VALID_MIN_NAME + " ps/" + INVALID_LONG_SPECIES,
                Species.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser,
                " oi/1 pn/" + VALID_MIN_NAME + " ps/" + VALID_MIN_SPECIES + " pr/" + INVALID_LONG_REMARK,
                PetRemark.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_nonEmptyPreamble_failure() {
        assertParseFailure(parser,
                " stray oi/1 pn/" + VALID_MIN_NAME + " ps/" + VALID_MIN_SPECIES,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddPetCommand.MESSAGE_USAGE));
    }
}
