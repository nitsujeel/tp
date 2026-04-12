package seedu.address.model.pet;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.commons.util.StringUtil.normalizeForComparison;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import seedu.address.model.session.Session;

/**
 * Represents a Pet in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Pet {

    // Identity fields
    private final PetName name;
    private final Species species;

    // Data fields
    private PetRemark remark;

    // Session list — mutable, excluded from equals/hashCode to preserve Set membership stability
    private final List<Session> sessions = new ArrayList<>();

    /**
     * Every field must be present and not null.
     */
    public Pet(PetName name, Species species, PetRemark remark) {
        requireAllNonNull(name, species, remark);
        this.name = name;
        this.species = species;
        this.remark = remark;
    }

    public PetName getName() {
        return name;
    }

    public Species getSpecies() {
        return species;
    }

    public PetRemark getRemark() {
        return remark;
    }

    /**
     * Returns an unmodifiable view of this pet's sessions.
     */
    public List<Session> getSessions() {
        return Collections.unmodifiableList(sessions);
    }

    /**
     * Adds a session to this pet's session list.
     */
    public void addSession(Session session) {
        requireNonNull(session);
        sessions.add(session);
        sessions.sort(Comparator
                .comparing(Session::getStartDateTime)
                .thenComparing(Session::getEndDateTime));
    }

    /**
     * Removes the session at the given 0-based index from this pet's session list.
     */
    public void removeSession(int sessionIndex) {
        sessions.remove(sessionIndex);
    }

    /**
     * Returns true if the given session overlaps with any existing session for this pet.
     */
    public boolean hasOverlappingSession(Session session) {
        requireNonNull(session);
        return sessions.stream().anyMatch(existingSession -> existingSession.overlapsWith(session));
    }

    /**
     * Returns true if both pets have the same name and species.
     * This defines a weaker notion of equality between two pets.
     */
    public boolean isSamePet(Pet otherPet) {
        if (otherPet == this) {
            return true;
        }

        return otherPet != null
                && normalizeForComparison(otherPet.getName().value).equals(normalizeForComparison(getName().value))
                && normalizeForComparison(otherPet.getSpecies().value)
                .equals(normalizeForComparison(getSpecies().value));
    }

    /**
     * Returns true if both pets have the same identity and data fields.
     * This defines a stronger notion of equality between two pets.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Pet)) {
            return false;
        }

        Pet otherPet = (Pet) other;
        return name.equals(otherPet.name)
                && species.equals(otherPet.species)
                && remark.equals(otherPet.remark);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, species, remark);
    }

    @Override
    public String toString() {
        String readableRemark = remark.toString().isBlank() ? "None" : remark.toString();
        return "Name: " + name
                + "; Species: " + species
                + "; Remark: " + readableRemark;
    }

}
