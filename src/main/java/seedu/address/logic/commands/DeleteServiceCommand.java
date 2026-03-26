package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SERVICE_NAME;

import java.util.List;
import java.util.Optional;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.service.Service;

/**
 * Deletes a service identified using its displayed index from the available services list.
 */
public class DeleteServiceCommand extends Command {

    public static final String COMMAND_WORD = "deleteservice";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the service identified by the service name.\n"
            + "Parameters: " + PREFIX_SERVICE_NAME + "SERVICE_NAME\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_SERVICE_NAME + "Fur trim";

    public static final String MESSAGE_DELETE_SERVICE_SUCCESS = "Deleted Service: %1$s";
    public static final String MESSAGE_INVALID_SERVICE_NAME = "The service name provided is invalid";

    private final String targetServiceName;

    /**
     * Creates a DeleteServiceCommand to delete the specified {@code Service}.
     */
    public DeleteServiceCommand(String targetServiceName) {
        requireNonNull(targetServiceName);
        this.targetServiceName = targetServiceName;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Service> lastShownServiceList = model.getServiceList();

        Optional<Service> serviceToDelete = lastShownServiceList.stream()
                .filter(service -> service.getName().equals(targetServiceName))
                .findFirst();

        if (serviceToDelete.isEmpty()) {
            throw new CommandException(MESSAGE_INVALID_SERVICE_NAME);
        }

        model.deleteService(serviceToDelete.get());
        return new CommandResult(String.format(MESSAGE_DELETE_SERVICE_SUCCESS, serviceToDelete.get()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteServiceCommand)) {
            return false;
        }

        DeleteServiceCommand otherDeleteServiceCommand = (DeleteServiceCommand) other;
        return targetServiceName.equals(otherDeleteServiceCommand.targetServiceName);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetServiceName", targetServiceName)
                .toString();
    }
}
