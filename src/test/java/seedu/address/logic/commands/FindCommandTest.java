package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_PERSONS_LISTED_OVERVIEW;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.FieldContainsKeywordsPredicate;

/**
 * Contains integration tests (interaction with the Model) for {@code FindCommand}.
 */
public class FindCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        FieldContainsKeywordsPredicate firstPredicate =
                new FieldContainsKeywordsPredicate(
                        Optional.of("first"), Optional.empty(), Optional.empty(), Optional.empty(),
                        Collections.emptyList());
        FieldContainsKeywordsPredicate secondPredicate =
                new FieldContainsKeywordsPredicate(
                        Optional.of("second"), Optional.empty(), Optional.empty(), Optional.empty(),
                        Collections.emptyList());

        FindCommand findFirstCommand = new FindCommand(firstPredicate);
        FindCommand findSecondCommand = new FindCommand(secondPredicate);

        // same object -> returns true
        assertTrue(findFirstCommand.equals(findFirstCommand));

        // same values -> returns true
        FindCommand findFirstCommandCopy = new FindCommand(firstPredicate);
        assertTrue(findFirstCommand.equals(findFirstCommandCopy));

        // different types -> returns false
        assertFalse(findFirstCommand.equals(1));

        // null -> returns false
        assertFalse(findFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(findFirstCommand.equals(findSecondCommand));
    }

    @Test
    public void execute_nonMatchingFilters_noPersonFound() {
        String expectedMessage = "No owners found.";
        FieldContainsKeywordsPredicate predicate = new FieldContainsKeywordsPredicate(
                Optional.of("zzz"), Optional.of("999"), Optional.empty(), Optional.empty(), Collections.emptyList());
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_multipleFieldFiltersOrSearch_onePersonFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 1)
                + String.format("%n1. Carl Kurz — matched: name, phone");
        FieldContainsKeywordsPredicate predicate = new FieldContainsKeywordsPredicate(
                Optional.of("Carl"), Optional.of("9535"), Optional.empty(), Optional.empty(), List.of());
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(List.of(CARL), model.getFilteredPersonList());
    }

    @Test
    public void execute_ownerAndPetFiltersOrSearch_twoPersonsFound() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2)
                + String.format("%n1. Benson Meier — matched: name")
                + String.format("%n2. Carl Kurz — matched: pet \"Goldy\" (name), pet \"Goldy\" (species),"
                        + " pet \"Goldy\" (remark)");
        FieldContainsKeywordsPredicate predicate = new FieldContainsKeywordsPredicate(
                Optional.of("Benson"), Optional.empty(), Optional.empty(), Optional.empty(), List.of(),
                Optional.of("Goldy"), Optional.of("Fish"), Optional.of("Calm"));
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(List.of(BENSON, CARL), model.getFilteredPersonList());
    }

    @Test
    public void execute_eachWordInOwnerNameSearchedSeparately_orSearch() {
        String expectedMessage = String.format(MESSAGE_PERSONS_LISTED_OVERVIEW, 2)
                + String.format("%n1. Carl Kurz — matched: name")
                + String.format("%n2. Daniel Meier — matched: name");
        FieldContainsKeywordsPredicate predicate = new FieldContainsKeywordsPredicate(
                Optional.of("car iel"), Optional.empty(), Optional.empty(), Optional.empty(), List.of(),
                Optional.empty(), Optional.empty(), Optional.empty());
        FindCommand command = new FindCommand(predicate);
        expectedModel.updateFilteredPersonList(predicate);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(List.of(CARL, DANIEL), model.getFilteredPersonList());
    }

    @Test
    public void toStringMethod() {
        FieldContainsKeywordsPredicate predicate = new FieldContainsKeywordsPredicate(
                Optional.of("keyword"), Optional.empty(), Optional.empty(), Optional.empty(), List.of());
        FindCommand findCommand = new FindCommand(predicate);
        String expected = FindCommand.class.getCanonicalName()
                + "{predicate=" + predicate + "}";
        assertEquals(expected, findCommand.toString());
    }
}
