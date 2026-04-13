package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.commons.util.StringUtil.normalizeForComparison;
import static seedu.address.commons.util.StringUtil.normalizeWhitespace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.pet.Pet;
import seedu.address.model.pet.PetRemark;
import seedu.address.model.session.Session;
import seedu.address.model.tag.Tag;

/**
 * Represents an Owner in the PetLog.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final Set<Tag> tags = new HashSet<>();
    private final Set<Pet> pets = new LinkedHashSet<>();
    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags, Set<Pet> pets) {
        requireAllNonNull(name, phone, email, address, tags, pets);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
        this.pets.addAll(pets);
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns an immutable pet set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Pet> getPets() {
        return Collections.unmodifiableSet(pets);
    }

    /**
     * Returns an immutable pet list in insertion order.
     */
    public List<Pet> getPetList() {
        return Collections.unmodifiableList(new ArrayList<>(pets));
    }

    /**
     * Returns the number of pets the person has
     */
    public int getPetCount() {
        return pets.size();
    }

    /**
     * Returns true if this person already has an equivalent pet.
     */
    public boolean hasPet(Pet pet) {
        requireAllNonNull(pet);
        return pets.stream().anyMatch(existingPet ->
                normalizePetName(existingPet).equals(normalizePetName(pet))
                        && normalizeSpecies(existingPet).equals(normalizeSpecies(pet)));
    }

    /**
     * Returns a new Person with the pet's remark updated at the specified index.
     * Maintains insertion order of pets.
     * @param petIndex a 0-based pet index
     * @param newRemark the new remark for the pet
     * @return a new Person with the updated pet
     */
    public Person getNewPersonWithNewRemark(int petIndex, String newRemark) {
        List<Pet> petList = new ArrayList<>(this.getPetList());
        Set<Pet> updatedPets = new LinkedHashSet<>();
        int currentIndex = 0;
        for (Pet pet : petList) {
            if (currentIndex == petIndex) {
                // Create new pet with updated remark
                Pet tmp = new Pet(pet.getName(), pet.getSpecies(), new PetRemark(newRemark));
                for (Session session: pet.getSessions()) {
                    tmp.addSession(session);
                }
                updatedPets.add(tmp);
            } else {
                updatedPets.add(pet);
            }
            currentIndex++;
        }
        return new Person(this.name, this.phone, this.email, this.address, this.tags, updatedPets);
    }

    /**
     * Returns true if both persons have the same name, ignoring case and surrounding spaces,
     * and the same phone number.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && normalizeName(otherPerson.getName()).equals(normalizeName(getName()))
                && normalizePhone(otherPerson.getPhone()).equals(normalizePhone(getPhone()));
    }

    private String normalizeName(Name name) {
        return normalizeForComparison(name.fullName);
    }

    private String normalizePhone(Phone phone) {
        return normalizeWhitespace(phone.value);
    }

    private String normalizePetName(Pet pet) {
        return normalizeForComparison(pet.getName().value);
    }

    private String normalizeSpecies(Pet pet) {
        return normalizeForComparison(pet.getSpecies().value);
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && address.equals(otherPerson.address)
                && tags.equals(otherPerson.tags)
                && pets.equals(otherPerson.pets);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, tags, pets);
    }

    @Override
    public String toString() {
        return "Name: " + name
                + "; Phone: " + phone
                + "; Email: " + email
                + "; Address: " + address
                + "; Tags: " + formatTags()
                + "; Pets: " + formatPets();
    }

    private String formatTags() {
        if (tags.isEmpty()) {
            return "None";
        }

        return tags.stream()
                .map(tag -> tag.tagName)
                .sorted()
                .collect(Collectors.joining(", "));
    }

    private String formatPets() {
        if (pets.isEmpty()) {
            return "None";
        }

        return getPetList().stream()
                .map(pet -> String.format("%s (%s)", pet.getName(), pet.getSpecies()))
                .collect(Collectors.joining(", "));
    }

}
