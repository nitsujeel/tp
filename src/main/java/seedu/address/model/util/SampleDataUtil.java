package seedu.address.model.util;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.pet.Pet;
import seedu.address.model.pet.PetName;
import seedu.address.model.pet.PetRemark;
import seedu.address.model.pet.Species;
import seedu.address.model.service.Service;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Person(new Name("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@example.com"),
                new Address("Blk 30 Geylang Street 29, #06-40"),
                getTagSet("friends"),
                new LinkedHashSet<>(List.of(
                        new Pet(new PetName("Buddy"), new Species("Dog"),
                                new PetRemark("Loyal and friendly")),
                        new Pet(new PetName("Luna"), new Species("Rabbit"),
                                new PetRemark("Loves leafy treats"))))),
            new Person(new Name("Bernice Yu"), new Phone("99272758"), new Email("berniceyu@example.com"),
                new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"),
                getTagSet("colleagues", "friends"),
                new LinkedHashSet<>(List.of(
                        new Pet(new PetName("Mittens"), new Species("Cat"),
                                new PetRemark("Likes to scratch furniture")),
                        new Pet(new PetName("Pebble"), new Species("Turtle"),
                                new PetRemark("Enjoys basking by the window"))))),
            new Person(new Name("Charlotte Oliveiro"), new Phone("93210283"), new Email("charlotte@example.com"),
                new Address("Blk 11 Ang Mo Kio Street 74, #11-04"),
                getTagSet("neighbours"),
                new LinkedHashSet<>(List.of(
                        new Pet(new PetName("Kiwi"), new Species("Bird"),
                                new PetRemark("Sings in the morning"))))),
            new Person(new Name("David Li"), new Phone("91031282"), new Email("lidavid@example.com"),
                new Address("Blk 436 Serangoon Gardens Street 26, #16-43"),
                getTagSet("family"),
                new LinkedHashSet<>(List.of(
                        new Pet(new PetName("Goldy"), new Species("Fish"),
                                new PetRemark("Calm swimmer")),
                        new Pet(new PetName("Nibbles"), new Species("Hamster"),
                                new PetRemark("Most active at night"))))),
            new Person(new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@example.com"),
                new Address("Blk 47 Tampines Street 20, #17-35"),
                getTagSet("classmates"),
                new LinkedHashSet<>(List.of(
                        new Pet(new PetName("Mochi"), new Species("Rabbit"),
                                new PetRemark("Very gentle with children"))))),
            new Person(new Name("Roy Balakrishnan"), new Phone("92624417"), new Email("royb@example.com"),
                new Address("Blk 45 Aljunied Street 85, #11-31"),
                getTagSet("colleagues"),
                new LinkedHashSet<>(List.of(
                        new Pet(new PetName("Bruno"), new Species("Dog"),
                                new PetRemark("Needs a long evening walk")),
                        new Pet(new PetName("Pip"), new Species("Bird"),
                                new PetRemark("Likes sunflower seeds")))))
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        for (Service sampleService : getSampleServices()) {
            sampleAb.addService(sampleService);
        }
        return sampleAb;
    }

    public static Service[] getSampleServices() {
        return new Service[] {
            new Service("Base service charge", 20),
            new Service("Shampoo", 30),
            new Service("Fur trim", 25),
            new Service("Nail trim", 10),
            new Service("Walk", 15)
        };
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}
