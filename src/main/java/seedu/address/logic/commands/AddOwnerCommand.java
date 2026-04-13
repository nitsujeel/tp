package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OWNER_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Adds an Owner to PetLog.
 */
public class AddOwnerCommand extends Command {

    public static final String COMMAND_WORD = "addowner";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an owner to the address book.\n"
            + "Parameters: "
            + PREFIX_OWNER_NAME + "OWNER_NAME "
            + PREFIX_PHONE + "PHONE_NUMBER "
            + PREFIX_EMAIL + "EMAIL "
            + PREFIX_ADDRESS + "ADDRESS "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_OWNER_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_EMAIL + "johnd@example.com "
            + PREFIX_ADDRESS + "311, Clementi Ave 2, #02-25 "
            + PREFIX_TAG + "friends "
            + PREFIX_TAG + "VIP";

    public static final String MESSAGE_SUCCESS = "Added owner: %1$s";
    public static final String MESSAGE_PHONE_WARNING =
            "Warning: Phone contains non-numeric characters. Use editowner to amend if necessary.";
    public static final String MESSAGE_SUCCESS_WITH_PHONE_WARNING = "Added owner: %1$s\n"
            + "Warning: Phone contains non-numeric characters. Use editowner to amend if necessary.";
    public static final String MESSAGE_PARTIAL_DUPLICATE_WARNING =
            "Warning: %1$s match existing owner records. Use editowner to amend if necessary.";
    public static final String MESSAGE_DUPLICATE_PERSON = "Owner already exists.";
    private static final int NUMBER_OF_IDENTITY_FIELDS = 4;

    private final Person toAdd;

    /**
     * Creates an AddOwnerCommand to add the specified {@code Person}
     */
    public AddOwnerCommand(Person person) {
        requireNonNull(person);
        toAdd = person;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        boolean isFullDuplicate = model.getAddressBook().getPersonList().stream()
                .anyMatch(existing -> existing.getMatchingIdentityFields(toAdd).size() == 4);

        if (isFullDuplicate) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }

        Set<String> matchedFields = collectPartialMatchFields(model);
        model.addPerson(toAdd);

        boolean hasPhoneWarning = !toAdd.getPhone().hasOnlyDigits();
        boolean hasPartialWarning = !matchedFields.isEmpty();

        String message;

        if (hasPhoneWarning && !hasPartialWarning) {
            message = String.format(MESSAGE_SUCCESS_WITH_PHONE_WARNING, Messages.format(toAdd));
        } else if (!hasPhoneWarning && hasPartialWarning) {
            message = String.format(MESSAGE_SUCCESS, Messages.format(toAdd))
                    + "\n"
                    + String.format(MESSAGE_PARTIAL_DUPLICATE_WARNING,
                    formatMatchedFields(matchedFields));
        } else if (hasPhoneWarning && hasPartialWarning) {
            message = String.format(MESSAGE_SUCCESS, Messages.format(toAdd))
                    + "\n"
                    + MESSAGE_PHONE_WARNING
                    + "\n"
                    + String.format(MESSAGE_PARTIAL_DUPLICATE_WARNING,
                    formatMatchedFields(matchedFields));
        } else {
            message = String.format(MESSAGE_SUCCESS, Messages.format(toAdd));
        }

        return new CommandResult(message);
    }

    private Set<String> collectPartialMatchFields(Model model) {
        Set<String> matchedFields = new LinkedHashSet<>();

        for (Person existingPerson : model.getAddressBook().getPersonList()) {
            List<String> matchingFields = existingPerson.getMatchingIdentityFields(toAdd);

            if (matchingFields.size() >= 2 && matchingFields.size() < NUMBER_OF_IDENTITY_FIELDS) {
                matchedFields.addAll(matchingFields);
            }
        }

        return matchedFields;
    }

    private void appendWarning(StringBuilder feedback, String warning) {
        feedback.append("\n").append(warning);
    }

    private String formatMatchedFields(Set<String> matchedFields) {
        List<String> ordered = new ArrayList<>();

        if (matchedFields.contains("name")) {
            ordered.add("name");
        }
        if (matchedFields.contains("phone")) {
            ordered.add("phone");
        }
        if (matchedFields.contains("email")) {
            ordered.add("email");
        }
        if (matchedFields.contains("address")) {
            ordered.add("address");
        }

        if (ordered.size() == 1) {
            return ordered.get(0);
        }
        if (ordered.size() == 2) {
            return ordered.get(0) + " and " + ordered.get(1);
        }

        String allButLast = String.join(", ", ordered.subList(0, ordered.size() - 1));
        return allButLast + ", and " + ordered.get(ordered.size() - 1);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddOwnerCommand)) {
            return false;
        }

        AddOwnerCommand otherAddOwnerCommand = (AddOwnerCommand) other;
        return toAdd.equals(otherAddOwnerCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
