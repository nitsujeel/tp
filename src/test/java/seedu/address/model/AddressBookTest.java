package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.service.Service;
import seedu.address.model.service.exceptions.DuplicateServiceException;
import seedu.address.testutil.PersonBuilder;

public class AddressBookTest {

    private final AddressBook addressBook = new AddressBook();

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), addressBook.getPersonList());
        assertEquals(Collections.emptyList(), addressBook.getServiceList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.resetData(null));
    }

    @Test
    public void resetData_withValidReadOnlyAddressBook_replacesData() {
        AddressBook newData = getTypicalAddressBook();
        addressBook.resetData(newData);
        assertEquals(newData, addressBook);
    }

    @Test
    public void resetData_withDuplicatePersons_throwsDuplicatePersonException() {
        // Two persons with the same identity fields
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        List<Person> newPersons = Arrays.asList(ALICE, editedAlice);
        AddressBookStub newData = new AddressBookStub(newPersons);

        assertThrows(DuplicatePersonException.class, () -> addressBook.resetData(newData));
    }

    @Test
    public void resetData_withDuplicateServices_throwsDuplicateServiceException() {
        List<Person> persons = Collections.emptyList();
        Service shampoo = new Service("Shampoo", 30.00);
        Service duplicateShampoo = new Service("Shampoo", 35.00);
        List<Service> newServices = Arrays.asList(shampoo, duplicateShampoo);
        AddressBookStub newData = new AddressBookStub(persons, newServices);

        assertThrows(DuplicateServiceException.class, () -> addressBook.resetData(newData));
    }

    @Test
    public void resetData_withDuplicateServicesIgnoringCaseAndWhitespace_throwsDuplicateServiceException() {
        List<Person> persons = Collections.emptyList();
        Service shampoo = new Service("Shampoo", 30.00);
        Service duplicateShampoo = new Service("  shamPoo  ", 35.00);
        List<Service> newServices = Arrays.asList(shampoo, duplicateShampoo);
        AddressBookStub newData = new AddressBookStub(persons, newServices);

        assertThrows(DuplicateServiceException.class, () -> addressBook.resetData(newData));
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(addressBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        addressBook.addPerson(ALICE);
        assertTrue(addressBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personWithSameIdentityFieldsInAddressBook_returnsTrue() {
        addressBook.addPerson(ALICE);
        Person editedAlice = new PersonBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(addressBook.hasPerson(editedAlice));
    }

    @Test
    public void hasService_nullService_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> addressBook.hasService(null));
    }

    @Test
    public void hasService_serviceNotInAddressBook_returnsFalse() {
        assertFalse(addressBook.hasService(new Service("Shampoo", 30.00)));
    }

    @Test
    public void hasService_serviceInAddressBook_returnsTrue() {
        Service shampoo = new Service("Shampoo", 30.00);
        addressBook.addService(shampoo);
        assertTrue(addressBook.hasService(shampoo));
    }

    @Test
    public void hasService_serviceWithSameIdentityFieldsInAddressBook_returnsTrue() {
        Service shampoo = new Service("Shampoo", 30.00);
        addressBook.addService(shampoo);
        Service editedShampoo = new Service("Shampoo", 35.00);
        assertTrue(addressBook.hasService(editedShampoo));
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> addressBook.getPersonList().remove(0));
    }

    @Test
    public void getServiceList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> addressBook.getServiceList().remove(0));
    }

    @Test
    public void toStringMethod() {
        String expected = AddressBook.class.getCanonicalName()
                + "{persons=" + addressBook.getPersonList() + ", services=" + addressBook.getServiceList() + "}";
        assertEquals(expected, addressBook.toString());
    }

    /**
     * A stub ReadOnlyAddressBook whose persons list can violate interface constraints.
     */
    private static class AddressBookStub implements ReadOnlyAddressBook {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();
        private final ObservableList<Service> services = FXCollections.observableArrayList();

        AddressBookStub(Collection<Person> persons) {
            this.persons.setAll(persons);
        }

        AddressBookStub(Collection<Person> persons, Collection<Service> services) {
            this.persons.setAll(persons);
            this.services.setAll(services);
        }

        @Override
        public ObservableList<Person> getPersonList() {
            return persons;
        }

        @Override
        public ObservableList<Service> getServiceList() {
            return services;
        }
    }

}
