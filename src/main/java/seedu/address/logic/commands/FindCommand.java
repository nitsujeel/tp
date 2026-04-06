package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OWNER_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OWNER_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET_REMARK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SPECIES;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.FieldContainsKeywordsPredicate;
import seedu.address.model.person.Person;

/**
 * Finds and lists all owners in PetLog whose specified fields contain the given prefixed search strings.
 * Matching is case-insensitive.
 */
public class FindCommand extends Command {

    public static final String COMMAND_WORD = "find";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all owners whose specified fields contain "
            + "the provided search strings (case-insensitive), and displays them as a list with index numbers.\n"
            + "Parameters: "
            + "[" + PREFIX_OWNER_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_TAG + "TAG]... "
            + "[" + PREFIX_OWNER_INDEX + "OWNER_INDEX] "
            + "[" + PREFIX_PET_NAME + "PET_NAME] "
            + "[" + PREFIX_SPECIES + "SPECIES] "
            + "[" + PREFIX_PET_REMARK + "REMARKS]\n"
            + "At least one prefix must be provided.\n"
            + "If pet prefixes are provided, listed owners must have at least one pet matching all pet criteria.\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_OWNER_NAME + "john "
            + PREFIX_PHONE + "9876 "
            + PREFIX_TAG + "friend "
            + PREFIX_PET_NAME + "snowball";

    private final FieldContainsKeywordsPredicate predicate;
    private final Optional<Index> ownerIndex;

    /**
     * Creates a FindCommand without an owner index filter.
     */
    public FindCommand(FieldContainsKeywordsPredicate predicate) {
        this(predicate, Optional.empty());
    }

    /**
     * Creates a FindCommand with optional owner index, owner-field and pet-field filters.
     */
    public FindCommand(FieldContainsKeywordsPredicate predicate, Optional<Index> ownerIndex) {
        requireNonNull(predicate);
        requireNonNull(ownerIndex);
        this.predicate = predicate;
        this.ownerIndex = ownerIndex;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        List<Person> baselineOwners = List.copyOf(model.getFilteredPersonList());
        model.updateFilteredPersonList(person -> predicate.test(person) && matchesOwnerIndex(person, baselineOwners));
        model.updateDisplayedSessions(model.getFilteredPersonList());
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    private boolean matchesOwnerIndex(Person person, List<Person> baselineOwners) {
        if (ownerIndex.isEmpty()) {
            return true;
        }

        int zeroBasedIndex = ownerIndex.get().getZeroBased();
        return zeroBasedIndex < baselineOwners.size() && baselineOwners.get(zeroBasedIndex).equals(person);
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
        return predicate.equals(otherFindCommand.predicate) && ownerIndex.equals(otherFindCommand.ownerIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .add("ownerIndex", ownerIndex)
                .toString();
    }
}
