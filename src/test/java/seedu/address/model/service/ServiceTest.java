package seedu.address.model.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class ServiceTest {
    private static final String VALID_MAX_LENGTH_SERVICE_NAME = "A".repeat(30);
    private static final String INVALID_TOO_LONG_SERVICE_NAME = "A".repeat(31);
    private static final String VALID_MAX_SERVICE_PRICE = "10000.00";
    private static final String INVALID_TOO_HIGH_SERVICE_PRICE = "10000.01";

    @Test
    public void constructor_nullName_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Service(null, 10.00));
    }

    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Service(INVALID_TOO_LONG_SERVICE_NAME, 10.00));
    }

    @Test
    public void constructor_invalidPrice_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Service("Shampoo", -1.00));
        assertThrows(IllegalArgumentException.class, () -> new Service("Shampoo", 10.999));
        assertThrows(IllegalArgumentException.class, () -> new Service("Shampoo", 10000.01));
    }

    @Test
    public void isValidServiceName() {
        assertThrows(NullPointerException.class, () -> Service.isValidServiceName(null));

        assertFalse(Service.isValidServiceName(""));
        assertFalse(Service.isValidServiceName(" "));
        assertFalse(Service.isValidServiceName(INVALID_TOO_LONG_SERVICE_NAME));
        assertTrue(Service.isValidServiceName(" fur trim"));
        assertTrue(Service.isValidServiceName("fur trim "));
        assertTrue(Service.isValidServiceName("fur  trim"));

        assertTrue(Service.isValidServiceName("Shampoo"));
        assertTrue(Service.isValidServiceName("Fur trim"));
        assertTrue(Service.isValidServiceName("Walk 2"));
        assertTrue(Service.isValidServiceName("^"));
        assertTrue(Service.isValidServiceName("fur*trim"));
        assertTrue(Service.isValidServiceName(VALID_MAX_LENGTH_SERVICE_NAME));
        assertTrue(Service.isValidServiceName("  Fur   trim  "));
    }

    @Test
    public void isValidServicePrice_string() {
        assertThrows(NullPointerException.class, () -> Service.isValidServicePrice((String) null));

        assertFalse(Service.isValidServicePrice(""));
        assertFalse(Service.isValidServicePrice("-1"));
        assertFalse(Service.isValidServicePrice(INVALID_TOO_HIGH_SERVICE_PRICE));
        assertFalse(Service.isValidServicePrice("10001"));
        assertFalse(Service.isValidServicePrice("12.345"));
        assertFalse(Service.isValidServicePrice("abc"));
        assertFalse(Service.isValidServicePrice("12a"));

        assertTrue(Service.isValidServicePrice("0"));
        assertTrue(Service.isValidServicePrice("10000"));
        assertTrue(Service.isValidServicePrice(VALID_MAX_SERVICE_PRICE));
        assertTrue(Service.isValidServicePrice("12"));
        assertTrue(Service.isValidServicePrice("12.3"));
        assertTrue(Service.isValidServicePrice("12.34"));
        assertTrue(Service.isValidServicePrice("  12.34  "));
    }

    @Test
    public void isValidServicePrice_double() {
        assertFalse(Service.isValidServicePrice(-1.00));
        assertFalse(Service.isValidServicePrice(10000.01));
        assertFalse(Service.isValidServicePrice(12.345));
        assertFalse(Service.isValidServicePrice(Double.NaN));
        assertFalse(Service.isValidServicePrice(Double.POSITIVE_INFINITY));

        assertTrue(Service.isValidServicePrice(0.00));
        assertTrue(Service.isValidServicePrice(10000.00));
        assertTrue(Service.isValidServicePrice(12.00));
        assertTrue(Service.isValidServicePrice(12.30));
        assertTrue(Service.isValidServicePrice(12.34));
    }

    @Test
    public void isSameService() {
        Service service = new Service("Shampoo", 30.00);

        assertTrue(service.isSameService(service));
        assertFalse(service.isSameService(null));
        assertTrue(service.isSameService(new Service("Shampoo", 35.00)));
        assertTrue(service.isSameService(new Service("  shamPoo  ", 35.00)));
        assertFalse(service.isSameService(new Service("Walk", 30.00)));
    }

    @Test
    public void equals() {
        Service service = new Service("Shampoo", 30.00);

        assertTrue(service.equals(new Service("Shampoo", 30.00)));
        assertTrue(service.equals(service));
        assertFalse(service.equals(null));
        assertFalse(service.equals(5));
        assertFalse(service.equals(new Service("Shampoo", 31.00)));
        assertFalse(service.equals(new Service("Walk", 30.00)));
    }

    @Test
    public void toStringMethod() {
        Service service = new Service("Shampoo", 30.0);
        String expected = "Name: Shampoo; Price: $30.00";

        assertEquals(expected, service.toString());
    }
}
