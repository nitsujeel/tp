package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_END_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OWNER_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SERVICE_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_TIME;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.service.Service;

/**
 * Adds a session for a specific owner and pet with a start and end time.
 */
public class AddSessionCommand extends Command {

    public static final String COMMAND_WORD = "addsession";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a session. "
            + "Parameters: "
            + PREFIX_OWNER_INDEX + "OWNER_INDEX "
            + PREFIX_PET_INDEX + "PET_INDEX "
            + PREFIX_START_TIME + "START_TIME "
            + PREFIX_END_TIME + "END_TIME "
            + "[" + PREFIX_SERVICE_NAME + "SERVICE_NAME]...\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_OWNER_INDEX + "1 "
            + PREFIX_PET_INDEX + "1 "
            + PREFIX_START_TIME + "2026-03-25 10:00 "
            + PREFIX_END_TIME + "2026-03-25 11:00 "
            + PREFIX_SERVICE_NAME + "Shampoo "
            + PREFIX_SERVICE_NAME + "Fur trim";

    public static final String MESSAGE_INVALID_OWNER_INDEX = "Invalid owner index";
    public static final String MESSAGE_INVALID_PET_INDEX = "Invalid pet index";
    public static final String MESSAGE_UNKNOWN_SERVICE = "Unknown service: %1$s";

    private final Index ownerIndex;
    private final Index petIndex;
    private final String startTime;
    private final String endTime;
    private final List<String> serviceNames;

    /**
     * Creates an AddSessionCommand.
     *
     * @param ownerIndex Index of the owner in the list
     * @param petIndex Index of the pet in the list
     * @param startTime Start time of the session
     * @param endTime End time of the session
     */
    public AddSessionCommand(Index ownerIndex, Index petIndex,
                             String startTime, String endTime, List<String> serviceNames) {
        requireNonNull(ownerIndex);
        requireNonNull(petIndex);
        requireNonNull(startTime);
        requireNonNull(endTime);
        requireNonNull(serviceNames);
        this.ownerIndex = ownerIndex;
        this.petIndex = petIndex;
        this.startTime = startTime;
        this.endTime = endTime;
        this.serviceNames = List.copyOf(serviceNames);
    }

    /**
     * Executes the command to add a session.
     *
     * @param model Model used to access and modify data
     * @return Result of the command execution
     * @throws CommandException If execution fails
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (ownerIndex.getZeroBased() >= model.getFilteredPersonList().size()) {
            throw new CommandException("Invalid owner index");
        }

        if (petIndex.getZeroBased() >= model.getFilteredPetList().size()) {
            throw new CommandException("Invalid pet index");
        }

        var owner = model.getFilteredPersonList().get(ownerIndex.getZeroBased());
        var pet = model.getFilteredPetList().get(petIndex.getZeroBased());

        double totalFee = calculateTotalFee(model.getServiceList());

        return new CommandResult(String.format(
                "Session added for %s's pet %s from %s to %s. Total fee: $%.2f",
                owner.getName(),
                pet.getName(),
                startTime,
                endTime,
                totalFee
        ));
    }

    /**
     * Calculates the total fee for all services in this session.
     *
     * @param availServices Services currently stored in the model   the reference object with which to compare.
     * @return Total fee for the selected services
     * @throws CommandException If any requested service does not exist
     */
    private double calculateTotalFee(List<Service> availServices) throws CommandException {
        double totalFee = 0;

        for (String serviceName : serviceNames) {
            Service matchedService = findServiceByName(availServices, serviceName);
            if (matchedService == null) {
                throw new CommandException(String.format(MESSAGE_UNKNOWN_SERVICE, serviceName));
            }
            totalFee += matchedService.getCost();
        }
        return totalFee;
    }

    /**
     * Finds a service by name from the available services list.
     */
    private Service findServiceByName(List<Service> availServices, String serviceName) {
        for (Service service : availServices) {
            if (service.getName().equalsIgnoreCase(serviceName)) {
                return service;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof AddSessionCommand)) {
            return false;
        }
        AddSessionCommand otherCommand = (AddSessionCommand) other;
        return ownerIndex.equals(otherCommand.ownerIndex)
                && petIndex.equals(otherCommand.petIndex)
                && startTime.equals(otherCommand.startTime)
                && endTime.equals(otherCommand.endTime)
                && serviceNames.equals(otherCommand.serviceNames);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("ownerIndex", ownerIndex)
                .add("petIndex", petIndex)
                .add("startTime", startTime)
                .add("endTime", endTime)
                .add("serviceNames", serviceNames)
                .toString();
    }
}
