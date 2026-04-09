package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Person;
import seedu.address.model.pet.Pet;
import seedu.address.model.service.Service;
import seedu.address.model.session.Session;
import seedu.address.model.session.SessionEntry;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Person> filteredPersons;
    private final ObservableList<SessionEntry> displayedSessions = FXCollections.observableArrayList();
    private final ObservableList<SessionEntry> unmodifiableDisplayedSessions =
            FXCollections.unmodifiableObservableList(displayedSessions);

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredPersons = new FilteredList<>(this.addressBook.getPersonList());
        updateDisplayedSessions(this.addressBook.getPersonList());
    }

    /**
     * Initializes an empty ModelManager.
     */
    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
        requireNonNull(userPrefs);
        this.userPrefs.resetData(userPrefs);
    }

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    @Override
    public Path getAddressBookFilePath() {
        return userPrefs.getAddressBookFilePath();
    }

    @Override
    public void setAddressBookFilePath(Path addressBookFilePath) {
        requireNonNull(addressBookFilePath);
        userPrefs.setAddressBookFilePath(addressBookFilePath);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasPerson(Person person) {
        requireNonNull(person);
        return addressBook.hasPerson(person);
    }

    @Override
    public void deletePerson(Person target) {
        addressBook.removePerson(target);
    }

    @Override
    public void addPerson(Person person) {
        addressBook.addPerson(person);
        updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    public void setPerson(Person target, Person editedPerson) {
        requireAllNonNull(target, editedPerson);

        addressBook.setPerson(target, editedPerson);
    }

    @Override
    public boolean hasService(Service service) {
        requireNonNull(service);
        return addressBook.hasService(service);
    }

    @Override
    public void deleteService(Service target) {
        addressBook.removeService(target);
    }

    @Override
    public void addService(Service service) {
        addressBook.addService(service);
    }

    @Override
    public ObservableList<Service> getServiceList() {
        return addressBook.getServiceList();
    }

    //=========== Session Display =============================================================

    @Override
    public void updateDisplayedSessions(List<Person> persons) {
        requireNonNull(persons);
        List<SessionEntry> entries = new ArrayList<>();
        for (int ownerIndex = 0; ownerIndex < persons.size(); ownerIndex++) {
            Person owner = persons.get(ownerIndex);
            entries.addAll(getSessionEntriesForOwner(owner, ownerIndex));
        }
        displayedSessions.setAll(entries);
    }

    private List<SessionEntry> getSessionEntriesForOwner(Person owner, int zeroBasedOwnerIndex) {
        List<SessionEntry> entries = new ArrayList<>();
        List<Pet> pets = owner.getPetList();
        for (int petIndex = 0; petIndex < pets.size(); petIndex++) {
            Pet pet = pets.get(petIndex);
            entries.addAll(getSessionEntriesForPet(owner, pet, zeroBasedOwnerIndex, petIndex));
        }
        return entries;
    }

    private List<SessionEntry> getSessionEntriesForPet(
            Person owner, Pet pet, int zeroBasedOwnerIndex, int zeroBasedPetIndex) {
        List<SessionEntry> entries = new ArrayList<>();
        List<Session> sessions = pet.getSessions();
        for (int i = 0; i < sessions.size(); i++) {
            entries.add(createSessionEntry(owner, pet, sessions.get(i), zeroBasedOwnerIndex, zeroBasedPetIndex, i));
        }
        return entries;
    }

    private SessionEntry createSessionEntry(
            Person owner, Pet pet, Session session, int zeroBasedOwnerIndex, int zeroBasedPetIndex,
            int zeroBasedSessionIndex) {
        return new SessionEntry(
                session,
                owner.getName().fullName,
                pet.getName().value,
                zeroBasedOwnerIndex + 1,
                zeroBasedPetIndex + 1,
                zeroBasedSessionIndex + 1
        );
    }

    @Override
    public ObservableList<SessionEntry> getSessionList() {
        return unmodifiableDisplayedSessions;
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view of the list of {@code Person} backed by the internal list of
     * {@code versionedAddressBook}
     */
    @Override
    public ObservableList<Person> getFilteredPersonList() {
        return filteredPersons;
    }

    @Override
    public void updateFilteredPersonList(Predicate<Person> predicate) {
        requireNonNull(predicate);
        filteredPersons.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager)) {
            return false;
        }

        ModelManager otherModelManager = (ModelManager) other;
        return addressBook.equals(otherModelManager.addressBook)
                && userPrefs.equals(otherModelManager.userPrefs)
                && filteredPersons.equals(otherModelManager.filteredPersons);
    }

}
