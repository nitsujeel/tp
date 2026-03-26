package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SERVICE_INDEX;

import java.util.List;

import seedu.address.commons.core.index.Index;
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
            + ": Deletes the service identified by the index number used in the displayed services list.\n"
            + "Parameters: " + PREFIX_SERVICE_INDEX + "SERVICE_INDEX\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_SERVICE_INDEX + "1";

    public static final String MESSAGE_DELETE_SERVICE_SUCCESS = "Deleted Service: %1$s";
    public static final String MESSAGE_INVALID_SERVICE_DISPLAYED_INDEX = "The service index provided is invalid";

    private final Index targetIndex;

    /**
     * Creates a DeleteServiceCommand to delete the specified {@code Service}.
     */
    public DeleteServiceCommand(Index targetIndex) {
        requireNonNull(targetIndex);
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Service> lastShownServiceList = model.getServiceList();

        if (targetIndex.getZeroBased() >= lastShownServiceList.size()) {
            throw new CommandException(MESSAGE_INVALID_SERVICE_DISPLAYED_INDEX);
        }

        Service serviceToDelete = lastShownServiceList.get(targetIndex.getZeroBased());
        model.deleteService(serviceToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_SERVICE_SUCCESS, serviceToDelete));
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
        return targetIndex.equals(otherDeleteServiceCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
