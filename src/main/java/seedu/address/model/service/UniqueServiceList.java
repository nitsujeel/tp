package seedu.address.model.service;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.service.exceptions.DuplicateServiceException;
import seedu.address.model.service.exceptions.ServiceNotFoundException;


/**
 * A list of services that enforces uniqueness between its elements and does not allow nulls.
 * A service is considered unique by comparing using {@code Service#isSameService(Service)}. As such, adding and
 * updating of services uses Service#isSameService(Service) for equality so as to ensure that the service being added
 * or updated is unique in terms of identity in the UniqueServiceList. However, the removal of a service uses
 * Service#equals(Object) so as to ensure that the service with exactly the same fields will be removed.
 *
 * Supports a minimal set of list operations.
 *
 * @see Service#isSameService(Service)
 */
public class UniqueServiceList implements Iterable<Service> {

    private final ObservableList<Service> internalList = FXCollections.observableArrayList();
    private final ObservableList<Service> internalUnmodifiableList =
            FXCollections.unmodifiableObservableList(internalList);

    /**
     * Returns true if the list contains an equivalent service as the given argument.
     */
    public boolean contains(Service toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameService);
    }

    /**
     * Adds a service to the list.
     * The service must not already exist in the list.
     */
    public void add(Service toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateServiceException();
        }
        internalList.add(toAdd);
    }

    /**
     * Replaces the service {@code target} in the list with {@code editedService}.
     * {@code target} must exist in the list.
     * The service identity of {@code editedService} must not be the same as another existing service in the list.
     */
    public void setService(Service target, Service editedService) {
        requireAllNonNull(target, editedService);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new ServiceNotFoundException();
        }

        if (!target.isSameService(editedService) && contains(editedService)) {
            throw new DuplicateServiceException();
        }

        internalList.set(index, editedService);
    }

    /**
     * Removes the equivalent service from the list.
     * The service must exist in the list.
     */
    public void remove(Service toRemove) {
        requireNonNull(toRemove);
        if (!internalList.remove(toRemove)) {
            throw new ServiceNotFoundException();
        }
    }

    public void setServices(UniqueServiceList replacement) {
        requireNonNull(replacement);
        internalList.setAll(replacement.internalList);
    }

    /**
     * Replaces the contents of this list with {@code services}.
     * {@code services} must not contain duplicate services.
     */
    public void setServices(List<Service> services) {
        requireAllNonNull(services);
        if (!servicesAreUnique(services)) {
            throw new DuplicateServiceException();
        }

        internalList.setAll(services);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Service> asUnmodifiableObservableList() {
        return internalUnmodifiableList;
    }

    @Override
    public Iterator<Service> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UniqueServiceList)) {
            return false;
        }

        UniqueServiceList otherUniqueServiceList = (UniqueServiceList) other;
        return internalList.equals(otherUniqueServiceList.internalList);
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public String toString() {
        return internalList.toString();
    }

    /**
     * Returns true if {@code services} contains only unique services.
     */
    private boolean servicesAreUnique(List<Service> services) {
        for (int i = 0; i < services.size() - 1; i++) {
            for (int j = i + 1; j < services.size(); j++) {
                if (services.get(i).isSameService(services.get(j))) {
                    return false;
                }
            }
        }
        return true;
    }
}
