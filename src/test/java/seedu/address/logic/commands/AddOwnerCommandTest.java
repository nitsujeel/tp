package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.service.Service;
import seedu.address.model.session.SessionEntry;
import seedu.address.testutil.PersonBuilder;

public class AddOwnerCommandTest {

    @Test
    public void constructor_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddOwnerCommand(null));
    }

    @Test
    public void execute_personAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Person validPerson = new PersonBuilder().build();

        CommandResult commandResult = new AddOwnerCommand(validPerson).execute(modelStub);

        assertEquals(String.format(AddOwnerCommand.MESSAGE_SUCCESS, Messages.format(validPerson)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validPerson), modelStub.personsAdded);
    }

    @Test
    public void execute_phoneContainsNonNumericCharacters_showsWarning() throws Exception {
        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded();
        Person validPersonWithNonNumericPhone = new PersonBuilder()
                .withPhone("65-1234-5678")
                .build();

        CommandResult commandResult = new AddOwnerCommand(validPersonWithNonNumericPhone).execute(modelStub);

        assertEquals(String.format(AddOwnerCommand.MESSAGE_SUCCESS_WITH_PHONE_WARNING,
                        Messages.format(validPersonWithNonNumericPhone)),
                commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(validPersonWithNonNumericPhone), modelStub.personsAdded);
    }

    @Test
    public void execute_partialDuplicate_showsWarning() throws Exception {
        Person existingPerson = new PersonBuilder()
                .withName("John Doe")
                .withPhone("12345678")
                .withEmail("john@example.com")
                .withAddress("123 Clementi Road")
                .build();

        Person partialDuplicate = new PersonBuilder()
                .withName("  john   doe ")
                .withPhone("1234 5678")
                .withEmail("other@example.com")
                .withAddress("456 Jurong West Ave 1")
                .build();

        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded(existingPerson);
        CommandResult commandResult = new AddOwnerCommand(partialDuplicate).execute(modelStub);
        System.out.println(commandResult.getFeedbackToUser());
        String expectedMessage = String.format(AddOwnerCommand.MESSAGE_SUCCESS, Messages.format(partialDuplicate))
                + "\n"
                + AddOwnerCommand.MESSAGE_PHONE_WARNING
                + "\n"
                + String.format(AddOwnerCommand.MESSAGE_PARTIAL_DUPLICATE_WARNING, "name and phone");

        assertEquals(expectedMessage, commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(partialDuplicate), modelStub.personsAdded);
    }

    @Test
    public void execute_partialDuplicateWithNonNumericPhone_showsBothWarnings() throws Exception {
        Person existingPerson = new PersonBuilder()
                .withName("John Doe")
                .withPhone("65-1234-5678")
                .withEmail("john@example.com")
                .withAddress("123 Clementi Road")
                .build();

        Person partialDuplicate = new PersonBuilder()
                .withName("john doe")
                .withPhone("65-1234-5678")
                .withEmail("other@example.com")
                .withAddress("456 Jurong West Ave 1")
                .build();

        ModelStubAcceptingPersonAdded modelStub = new ModelStubAcceptingPersonAdded(existingPerson);
        CommandResult commandResult = new AddOwnerCommand(partialDuplicate).execute(modelStub);

        String expectedMessage = String.format(AddOwnerCommand.MESSAGE_SUCCESS, Messages.format(partialDuplicate))
                + "\n"
                + AddOwnerCommand.MESSAGE_PHONE_WARNING
                + "\n"
                + String.format(AddOwnerCommand.MESSAGE_PARTIAL_DUPLICATE_WARNING, "name and phone");

        assertEquals(expectedMessage, commandResult.getFeedbackToUser());
        assertEquals(Arrays.asList(partialDuplicate), modelStub.personsAdded);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Person validPerson = new PersonBuilder().build();
        AddOwnerCommand addOwnerCommand = new AddOwnerCommand(validPerson);
        ModelStub modelStub = new ModelStubAcceptingPersonAdded(validPerson);

        assertThrows(CommandException.class,
                AddOwnerCommand.MESSAGE_DUPLICATE_PERSON, () -> addOwnerCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        Person alice = new PersonBuilder().withName("Alice").build();
        Person bob = new PersonBuilder().withName("Bob").build();
        AddOwnerCommand addAliceCommand = new AddOwnerCommand(alice);
        AddOwnerCommand addBobCommand = new AddOwnerCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddOwnerCommand addAliceCommandCopy = new AddOwnerCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different person -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    @Test
    public void toStringMethod() {
        AddOwnerCommand addOwnerCommand = new AddOwnerCommand(ALICE);
        String expected = AddOwnerCommand.class.getCanonicalName() + "{toAdd=" + ALICE + "}";
        assertEquals(expected, addOwnerCommand.toString());
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasService(Service service) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteService(Service target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addService(Service service) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Service> getServiceList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateDisplayedSessions(java.util.List<Person> persons) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<SessionEntry> getSessionList() {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single person.
     */
    private class ModelStubWithPerson extends ModelStub {
        private final Person person;

        ModelStubWithPerson(Person person) {
            requireNonNull(person);
            this.person = person;
        }

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return this.person.isSamePerson(person);
        }
    }

    /**
     * A Model stub that always accept the person being added.
     */
    private class ModelStubAcceptingPersonAdded extends ModelStub {
        final ArrayList<Person> personsAdded = new ArrayList<>();
        final AddressBook addressBook = new AddressBook();

        ModelStubAcceptingPersonAdded(Person... existingPersons) {
            for (Person existingPerson : existingPersons) {
                addressBook.addPerson(existingPerson);
            }
        }

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return addressBook.hasPerson(person);
        }

        @Override
        public void addPerson(Person person) {
            requireNonNull(person);
            personsAdded.add(person);
            addressBook.addPerson(person);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return addressBook;
        }
    }

}
