package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.service.Service;
import seedu.address.model.session.Session;

/**
 * Jackson-friendly version of {@link Session}.
 */
public class JsonAdaptedSession {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Session's %s field is missing!";

    private final String startTime;
    private final String endTime;
    private final double fee;
    private final List<JsonAdaptedService> services = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedSession} with the given session details.
     */
    @JsonCreator
    public JsonAdaptedSession(@JsonProperty("startTime") String startTime,
                              @JsonProperty("endTime") String endTime,
                              @JsonProperty("fee") double fee,
                              @JsonProperty("services") List<JsonAdaptedService> services) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.fee = fee;
        if (services != null) {
            this.services.addAll(services);
        }
    }

    /**
     * Converts a given {@code Session} into this class for Jackson use.
     */
    public JsonAdaptedSession(Session source) {
        this.startTime = source.getStartTime();
        this.endTime = source.getEndTime();
        this.fee = source.getFee();
        this.services.addAll(source.getServices().stream()
                .map(JsonAdaptedService::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted session object into the model's {@code Session} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted session.
     */
    public Session toModelType() throws IllegalValueException {
        if (startTime == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "startTime"));
        }
        if (endTime == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, "endTime"));
        }
        List<Service> modelServices = new ArrayList<>();
        for (JsonAdaptedService adaptedService : services) {
            modelServices.add(adaptedService.toModelType());
        }
        try {
            return new Session(startTime, endTime, fee, modelServices);
        } catch (IllegalArgumentException e) {
            throw new IllegalValueException(e.getMessage(), e);
        }
    }
}
