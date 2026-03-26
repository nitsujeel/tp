package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_OWNER_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PET_REMARK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SPECIES;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.pet.Pet;

/**
 * Adds a pet to the address book.
 */
public class AddPetCommand extends Command {

    public static final String COMMAND_WORD = "addpet";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a pet to the address book. "
            + "Parameters: "
            + PREFIX_OWNER_INDEX + "OWNER_INDEX "
            + PREFIX_PET_NAME + "PET_NAME "
            + PREFIX_SPECIES + "SPECIES "
            + "[" + PREFIX_PET_REMARK + "REMARKS]\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_OWNER_INDEX + "1 "
            + PREFIX_PET_NAME + "Snowball "
            + PREFIX_SPECIES + "Cat "
            + PREFIX_PET_REMARK + "Feed hourly";

    public static final String MESSAGE_SUCCESS = "New pet added: %1$s";
    public static final String MESSAGE_DUPLICATE_PET = "This pet already exists in the address book";

    private final Index ownerIndex;
    private final Pet toAdd;

    /**
     * Creates an AddPetCommand to add the specified {@code Pet}
     */
    public AddPetCommand(Index ownerIndex, Pet pet) {
        requireNonNull(ownerIndex);
        requireNonNull(pet);
        this.ownerIndex = ownerIndex;
        toAdd = pet;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Person> lastShownList = model.getFilteredPersonList();
        if (ownerIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException("The owner index provided is invalid.");
        }

        Person owner = lastShownList.get(ownerIndex.getZeroBased());

        if (owner.hasPet(toAdd)) {
            throw new CommandException(MESSAGE_DUPLICATE_PET);
        }

        Set<Pet> updatedPets = new LinkedHashSet<>(owner.getPets());
        updatedPets.add(toAdd);
        Person updatedOwner = new Person(owner.getName(), owner.getPhone(), owner.getEmail(),
                owner.getAddress(), owner.getTags(), updatedPets);

        model.setPerson(owner, updatedOwner);
        return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddPetCommand)) {
            return false;
        }

        AddPetCommand otherAddPetCommand = (AddPetCommand) other;
        return ownerIndex.equals(otherAddPetCommand.ownerIndex)
                && toAdd.equals(otherAddPetCommand.toAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("ownerIndex", ownerIndex)
                .add("toAdd", toAdd)
                .toString();
    }
}
