package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OWNER_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET_REMARK;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Updates the pet's remark attribute
 */
public class UpdatePetRemarkCommand extends Command {

    public static final String COMMAND_WORD = "update";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": update the remarks of a specified pet. "
            + "Parameters: "
            + PREFIX_OWNER_INDEX + "OWNER_INDEX "
            + PREFIX_PET_INDEX + "PET_INDEX "
            + PREFIX_PET_REMARK + "PET_REMARK\n "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_OWNER_INDEX + "1 "
            + PREFIX_PET_INDEX + "1 "
            + PREFIX_PET_REMARK + "loves to eat ice cream ";

    public static final String MESSAGE_SUCCESS = "You have updated the remark of the pet!";

    private final Index ownerIndex;

    private final Index petIndex;

    private final String newRemark;

    /**
     * Creates an UpdatePetRemarkCommand to add the specified {@code Pet}
     */
    public UpdatePetRemarkCommand(Index ownerIndex, Index petIndex, String newRemark) {
        requireNonNull(newRemark);
        this.ownerIndex = ownerIndex;
        this.petIndex = petIndex;
        this.newRemark = newRemark;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();
        //This ownerIndex is zero based
        if (ownerIndex.getZeroBased() < 0 || ownerIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException("The owner index provided is invalid.");
        }
        Person owner = lastShownList.get(ownerIndex.getZeroBased());
        //This petIndex is zero based as well
        if (petIndex.getZeroBased() < 0 || petIndex.getZeroBased() >= owner.getPetCount()) {
            throw new CommandException("The pet index provided is invalid.");
        }
        owner.updatePetRemark(petIndex.getZeroBased(), newRemark);
        model.setPerson(owner, owner);
        return new CommandResult(String.format(MESSAGE_SUCCESS));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UpdatePetRemarkCommand)) {
            return false;
        }

        UpdatePetRemarkCommand otherAddOwnerCommand = (UpdatePetRemarkCommand) other;
        return (this.ownerIndex.equals(otherAddOwnerCommand.ownerIndex)
                && this.petIndex.equals(otherAddOwnerCommand.petIndex)
                && newRemark.equals(otherAddOwnerCommand.newRemark));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("New Remark", newRemark)
                .toString();
    }

    public String getNewRemark() {
        return this.newRemark;
    }
}
