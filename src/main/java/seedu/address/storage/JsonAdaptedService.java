package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.service.Service;

/**
 * Jackson-friendly version of {@link Service}.
 */
public class JsonAdaptedService {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Service's %s field is missing!";

    private final String name;
    private final double cost;

    /**
     * Constructs a {@code JsonAdaptedService} with the given service details.
     */
    @JsonCreator
    public JsonAdaptedService(@JsonProperty("name") String name, @JsonProperty("cost") double cost) {
        this.name = name;
        this.cost = cost;
    }

    /**
     * Converts a given {@code Service} into this class for Jackson use.
     */
    public JsonAdaptedService(Service source) {
        name = source.getName();
        cost = source.getCost();
    }

    /**
     * Converts this Jackson-friendly adapted service object into the model's {@code Service} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted service.
     */
    public Service toModelType() throws IllegalValueException {
        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Service.class.getSimpleName()));
        }
        if (!Service.isValidServiceName(name)) {
            throw new IllegalValueException(Service.MESSAGE_CONSTRAINTS);
        }
        if (!Service.isValidServicePrice(cost)) {
            throw new IllegalValueException(Service.MESSAGE_PRICE_CONSTRAINTS);
        }

        return new Service(name, cost);
    }

}
