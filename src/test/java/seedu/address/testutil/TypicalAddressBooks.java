package seedu.address.testutil;

import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.person.Person;
import seedu.address.model.pet.Pet;
import seedu.address.model.pet.PetName;
import seedu.address.model.pet.PetRemark;
import seedu.address.model.pet.Species;
import seedu.address.model.service.Service;

/**
 * Contains reusable address book fixtures for tests.
 */
public class TypicalAddressBooks {

    private TypicalAddressBooks() {}

    /**
     * Returns a typical address book for tests with:
     * - services preloaded
     * - at least one owner with multiple pets
     * - at least one owner with no pets
     */
    public static AddressBook getTypicalPetLog() {
        AddressBook addressBook = new AddressBook();
        for (Person person : getTypicalOwners()) {
            addressBook.addPerson(person);
        }
        for (Service service : getTypicalServices()) {
            addressBook.addService(service);
        }
        return addressBook;
    }

    private static List<Person> getTypicalOwners() {
        return List.of(
                new PersonBuilder()
                        .withName("Alex Yeoh")
                        .withPhone("87438807")
                        .withEmail("alexyeoh@example.com")
                        .withAddress("Blk 30 Geylang Street 29, #06-40")
                        .withTags("friends")
                        .withPets(
                                new Pet(new PetName("Buddy"), new Species("Dog"),
                                        new PetRemark("Loyal and friendly")),
                                new Pet(new PetName("Luna"), new Species("Rabbit"),
                                        new PetRemark("Loves leafy treats")))
                        .build(),
                new PersonBuilder()
                        .withName("Bernice Yu")
                        .withPhone("99272758")
                        .withEmail("berniceyu@example.com")
                        .withAddress("Blk 30 Lorong 3 Serangoon Gardens, #07-18")
                        .withTags("colleagues", "friends")
                        .withPets(new Pet(new PetName("Mittens"), new Species("Cat"),
                                new PetRemark("Likes to scratch furniture")))
                        .build(),
                new PersonBuilder()
                        .withName("Charlotte Oliveiro")
                        .withPhone("93210283")
                        .withEmail("charlotte@example.com")
                        .withAddress("Blk 11 Ang Mo Kio Street 74, #11-04")
                        .withTags("neighbours")
                        .build(),
                new PersonBuilder()
                        .withName("David Li")
                        .withPhone("91031282")
                        .withEmail("lidavid@example.com")
                        .withAddress("Blk 436 Serangoon Gardens Street 26, #16-43")
                        .withTags("family")
                        .withPets(
                                new Pet(new PetName("Goldy"), new Species("Fish"),
                                        new PetRemark("Calm swimmer")),
                                new Pet(new PetName("Nibbles"), new Species("Hamster"),
                                        new PetRemark("Most active at night")),
                                new Pet(new PetName("Pip"), new Species("Bird"),
                                        new PetRemark("Enjoys sunflower seeds")))
                        .build(),
                new PersonBuilder()
                        .withName("Irfan Ibrahim")
                        .withPhone("92492021")
                        .withEmail("irfan@example.com")
                        .withAddress("Blk 47 Tampines Street 20, #17-35")
                        .withTags("classmates")
                        .withPets(new Pet(new PetName("Mochi"), new Species("Rabbit"),
                                new PetRemark("Very gentle with children")))
                        .build(),
                new PersonBuilder()
                        .withName("Roy Balakrishnan")
                        .withPhone("92624417")
                        .withEmail("royb@example.com")
                        .withAddress("Blk 45 Aljunied Street 85, #11-31")
                        .withTags("colleagues")
                        .withPets(new Pet(new PetName("Bruno"), new Species("Dog"),
                                new PetRemark("Needs a long evening walk")))
                        .build());
    }

    private static List<Service> getTypicalServices() {
        return List.of(
                new Service("Base service charge", 20),
                new Service("Shampoo", 30),
                new Service("Fur trim", 25),
                new Service("Nail trim", 10),
                new Service("Walk", 15));
    }
}
