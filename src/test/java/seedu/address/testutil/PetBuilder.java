package seedu.address.testutil;

import seedu.address.model.pet.Pet;
import seedu.address.model.pet.PetName;
import seedu.address.model.pet.PetRemark;
import seedu.address.model.pet.Species;

/**
 * A utility class to help with building Pet objects.
 */
public class PetBuilder {

    public static final String DEFAULT_PET_NAME = "Buddy";
    public static final String DEFAULT_PET_REMARK = "Friendly";
    public static final String DEFAULT_SPECIES = "Dog";

    private PetName petName;
    private PetRemark petRemark;
    private Species species;

    /**
     * Creates a {@code PetBuilder} with the default details.
     */
    public PetBuilder() {
        petName = new PetName(DEFAULT_PET_NAME);
        petRemark = new PetRemark(DEFAULT_PET_REMARK);
        species = new Species(DEFAULT_SPECIES);
    }

    /**
     * Initializes the PetBuilder with the data of {@code petToCopy}.
     */
    public PetBuilder(Pet petToCopy) {
        petName = petToCopy.getName();
        petRemark = petToCopy.getRemark();
        species = petToCopy.getSpecies();
    }

    /**
     * Sets the {@code PetName} of the {@code Pet} that we are building.
     */
    public PetBuilder withName(String petName) {
        this.petName = new PetName(petName);
        return this;
    }

    /**
     * Sets the {@code PetRemark} of the {@code Pet} that we are building.
     */
    public PetBuilder withPetRemark(String petRemark) {
        this.petRemark = new PetRemark(petRemark);
        return this;
    }

    /**
     * Sets the {@code Species} of the {@code Pet} that we are building.
     */
    public PetBuilder withSpecies(String species) {
        this.species = new Species(species);
        return this;
    }

    public Pet build() {
        return new Pet(petName, species, petRemark);
    }

}
