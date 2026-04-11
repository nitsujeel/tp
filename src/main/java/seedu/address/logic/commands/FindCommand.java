package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OWNER_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET_REMARK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SPECIES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.List;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.FieldContainsKeywordsPredicate;
import seedu.address.model.person.Person;

/**
 * Finds and lists all owners in PetLog whose specified fields contain the given prefixed search strings.
 * Matching is case-insensitive and OR-based across all provided search terms.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all owners whose specified fields contain "
            + "the provided search strings (case-insensitive), and displays them as a list with index numbers.\n"
            + "Parameters: "
            + "[" + PREFIX_OWNER_NAME + "OWNER_NAME] "
            + "[" + PREFIX_PHONE + "PHONE_NUMBER] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_TAG + "OWNER_TAG]... "
            + "[" + PREFIX_PET_NAME + "PET_NAME] "
            + "[" + PREFIX_SPECIES + "SPECIES] "
            + "[" + PREFIX_PET_REMARK + "REMARKS]\n"
            + "At least one prefix must be provided.\n"
            + "Search is OR-based across all provided terms.\n"
            + "Each whitespace-separated word is treated as a separate search term.\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_OWNER_NAME + "avi jon "
            + PREFIX_PET_NAME + "sam";

    private final FieldContainsKeywordsPredicate predicate;

    /**
     * Creates a FindCommand with owner-field and pet-field filters.
     */
    public FindCommand(FieldContainsKeywordsPredicate predicate) {
        requireNonNull(predicate);
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        model.updateDisplayedSessions(model.getFilteredPersonList());
        List<Person> matched = model.getFilteredPersonList();
        int count = matched.size();

        if (count == 0) {
            return new CommandResult("No owners found.");
        }

        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, count));
        for (int i = 0; i < count; i++) {
            Person person = matched.get(i);
            sb.append(String.format("%n%d. %s — matched: %s",
                    i + 1, person.getName().fullName, predicate.describeMatch(person)));
        }

        return new CommandResult(sb.toString());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FindCommand)) {
            return false;
        }

        FindCommand otherFindCommand = (FindCommand) other;
        return predicate.equals(otherFindCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
