package seedu.address.storage;

import static seedu.address.storage.JsonAdaptedService.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.service.Service;

public class JsonAdaptedServiceTest {
    private static final String INVALID_SERVICE_NAME = "@wash";

    private static final String VALID_SERVICE_NAME = "Shampoo";
    private static final double VALID_SERVICE_COST = 30.00;

    @Test
    public void toModelType_validServiceDetails_returnsService() throws Exception {
        JsonAdaptedService service = new JsonAdaptedService(VALID_SERVICE_NAME, VALID_SERVICE_COST);
        Service expectedService = new Service(VALID_SERVICE_NAME, VALID_SERVICE_COST);
        org.junit.jupiter.api.Assertions.assertEquals(expectedService, service.toModelType());
    }

    @Test
    public void toModelType_nameWithLongWhitespace_returnsNormalizedService() throws Exception {
        JsonAdaptedService service = new JsonAdaptedService("  Fur   trim ", VALID_SERVICE_COST);
        Service expectedService = new Service("Fur trim", VALID_SERVICE_COST);
        org.junit.jupiter.api.Assertions.assertEquals(expectedService, service.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedService service = new JsonAdaptedService(INVALID_SERVICE_NAME, VALID_SERVICE_COST);
        assertThrows(IllegalValueException.class, Service.MESSAGE_CONSTRAINTS, service::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedService service = new JsonAdaptedService(null, VALID_SERVICE_COST);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Service.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, service::toModelType);
    }

    @Test
    public void toModelType_invalidCost_throwsIllegalValueException() {
        JsonAdaptedService negativeCostService = new JsonAdaptedService(VALID_SERVICE_NAME, -1.00);
        JsonAdaptedService moreThanTwoDpService = new JsonAdaptedService(VALID_SERVICE_NAME, 12.345);
        assertThrows(IllegalValueException.class, Service.MESSAGE_PRICE_CONSTRAINTS, negativeCostService::toModelType);
        assertThrows(IllegalValueException.class, Service.MESSAGE_PRICE_CONSTRAINTS,
                moreThanTwoDpService::toModelType);
    }
}
