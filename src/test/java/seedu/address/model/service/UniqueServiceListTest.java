package seedu.address.model.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.service.exceptions.DuplicateServiceException;
import seedu.address.model.service.exceptions.ServiceNotFoundException;

public class UniqueServiceListTest {

    private static final Service SHAMPOO = new Service("Shampoo", 30.00);
    private static final Service FUR_TRIM = new Service("Fur trim", 25.00);
    private static final Service SHAMPOO_WITH_DIFFERENT_PRICE = new Service("Shampoo", 35.00);

    private final UniqueServiceList uniqueServiceList = new UniqueServiceList();

    @Test
    public void contains_nullService_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueServiceList.contains(null));
    }

    @Test
    public void contains_serviceNotInList_returnsFalse() {
        assertFalse(uniqueServiceList.contains(SHAMPOO));
    }

    @Test
    public void contains_serviceInList_returnsTrue() {
        uniqueServiceList.add(SHAMPOO);
        assertTrue(uniqueServiceList.contains(SHAMPOO));
    }

    @Test
    public void contains_serviceWithSameIdentityFieldsInList_returnsTrue() {
        uniqueServiceList.add(SHAMPOO);
        assertTrue(uniqueServiceList.contains(SHAMPOO_WITH_DIFFERENT_PRICE));
    }

    @Test
    public void add_nullService_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueServiceList.add(null));
    }

    @Test
    public void add_duplicateService_throwsDuplicateServiceException() {
        uniqueServiceList.add(SHAMPOO);
        assertThrows(DuplicateServiceException.class, () -> uniqueServiceList.add(SHAMPOO_WITH_DIFFERENT_PRICE));
    }

    @Test
    public void setService_nullTargetService_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueServiceList.setService(null, SHAMPOO));
    }

    @Test
    public void setService_nullEditedService_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueServiceList.setService(SHAMPOO, null));
    }

    @Test
    public void setService_targetServiceNotInList_throwsServiceNotFoundException() {
        assertThrows(ServiceNotFoundException.class, () -> uniqueServiceList.setService(SHAMPOO, SHAMPOO));
    }

    @Test
    public void setService_editedServiceIsSameService_success() {
        uniqueServiceList.add(SHAMPOO);
        uniqueServiceList.setService(SHAMPOO, SHAMPOO);
        UniqueServiceList expectedUniqueServiceList = new UniqueServiceList();
        expectedUniqueServiceList.add(SHAMPOO);
        assertEquals(expectedUniqueServiceList, uniqueServiceList);
    }

    @Test
    public void setService_editedServiceHasSameIdentity_success() {
        uniqueServiceList.add(SHAMPOO);
        uniqueServiceList.setService(SHAMPOO, SHAMPOO_WITH_DIFFERENT_PRICE);
        UniqueServiceList expectedUniqueServiceList = new UniqueServiceList();
        expectedUniqueServiceList.add(SHAMPOO_WITH_DIFFERENT_PRICE);
        assertEquals(expectedUniqueServiceList, uniqueServiceList);
    }

    @Test
    public void setService_editedServiceHasDifferentIdentity_success() {
        uniqueServiceList.add(SHAMPOO);
        uniqueServiceList.setService(SHAMPOO, FUR_TRIM);
        UniqueServiceList expectedUniqueServiceList = new UniqueServiceList();
        expectedUniqueServiceList.add(FUR_TRIM);
        assertEquals(expectedUniqueServiceList, uniqueServiceList);
    }

    @Test
    public void setService_editedServiceHasNonUniqueIdentity_throwsDuplicateServiceException() {
        uniqueServiceList.add(SHAMPOO);
        uniqueServiceList.add(FUR_TRIM);
        assertThrows(DuplicateServiceException.class, ()
                -> uniqueServiceList.setService(FUR_TRIM, SHAMPOO_WITH_DIFFERENT_PRICE));
    }

    @Test
    public void remove_nullService_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueServiceList.remove(null));
    }

    @Test
    public void remove_serviceDoesNotExist_throwsServiceNotFoundException() {
        assertThrows(ServiceNotFoundException.class, () -> uniqueServiceList.remove(SHAMPOO));
    }

    @Test
    public void remove_existingService_removesService() {
        uniqueServiceList.add(SHAMPOO);
        uniqueServiceList.remove(SHAMPOO);
        UniqueServiceList expectedUniqueServiceList = new UniqueServiceList();
        assertEquals(expectedUniqueServiceList, uniqueServiceList);
    }

    @Test
    public void setServices_nullUniqueServiceList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueServiceList.setServices((UniqueServiceList) null));
    }

    @Test
    public void setServices_uniqueServiceList_replacesOwnListWithProvidedUniqueServiceList() {
        uniqueServiceList.add(SHAMPOO);
        UniqueServiceList expectedUniqueServiceList = new UniqueServiceList();
        expectedUniqueServiceList.add(FUR_TRIM);
        uniqueServiceList.setServices(expectedUniqueServiceList);
        assertEquals(expectedUniqueServiceList, uniqueServiceList);
    }

    @Test
    public void setServices_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueServiceList.setServices((List<Service>) null));
    }

    @Test
    public void setServices_list_replacesOwnListWithProvidedList() {
        uniqueServiceList.add(SHAMPOO);
        List<Service> serviceList = Collections.singletonList(FUR_TRIM);
        uniqueServiceList.setServices(serviceList);
        UniqueServiceList expectedUniqueServiceList = new UniqueServiceList();
        expectedUniqueServiceList.add(FUR_TRIM);
        assertEquals(expectedUniqueServiceList, uniqueServiceList);
    }

    @Test
    public void setServices_listWithDuplicateServices_throwsDuplicateServiceException() {
        List<Service> listWithDuplicateServices = Arrays.asList(SHAMPOO, SHAMPOO_WITH_DIFFERENT_PRICE);
        assertThrows(DuplicateServiceException.class, () -> uniqueServiceList.setServices(listWithDuplicateServices));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ()
                -> uniqueServiceList.asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void toStringMethod() {
        assertEquals(uniqueServiceList.asUnmodifiableObservableList().toString(), uniqueServiceList.toString());
    }
}
