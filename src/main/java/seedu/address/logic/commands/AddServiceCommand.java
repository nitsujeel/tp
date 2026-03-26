package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SERVICE_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SERVICE_PRICE;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.service.Service;

/**
 * Adds a service to the address book.
 */
public class AddServiceCommand extends Command {

    public static final String COMMAND_WORD = "addservice";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a service to the list of available services. "
            + "Parameters: "
            + PREFIX_SERVICE_NAME + "SERVICE_NAME "
            + PREFIX_SERVICE_PRICE + "SERVICE_PRICE\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_SERVICE_NAME + "Fur trim "
            + PREFIX_SERVICE_PRICE + "25.00";

    public static final String MESSAGE_SUCCESS = "New service added: %1$s";
    public static final String MESSAGE_DUPLICATE_SERVICE = "This service already exists in PetLog";

    private final Service toAdd;

    /**
     * Creates an AddServiceCommand to add the specified {@code Service}.
     */
    public AddServiceCommand(Service service) {
        requireNonNull(service);
        toAdd = service;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasService(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_SERVICE);
        }

        model.addService(toAdd);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddServiceCommand)) {
            return false;
        }

        AddServiceCommand otherAddServiceCommand = (AddServiceCommand) other;
        return toAdd.equals(otherAddServiceCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAdd)
                .toString();
    }
}
