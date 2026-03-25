package seedu.address.model.service;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a service in PetLog.
 * Guarantees: immutable; name is valid as declared in {@link #isValidServiceName(String)}
 */
public class Service {

    public static final String MESSAGE_CONSTRAINTS = "Service names should be alphanumeric";
    public static final String VALIDATION_REGEX = "\\p{Alnum}+";

    public final String serviceName;
    public final double servicePrice;

    /**
     * Constructs a {@code Service}.
     *
     * @param serviceName A valid service name.
     * @param servicePrice Price of the service. Rounds doubles to 2 deimal places.
     */
    public Service(String serviceName, double servicePrice) {
        requireNonNull(serviceName);
        checkArgument(isValidServiceName(serviceName), MESSAGE_CONSTRAINTS);
        this.serviceName = serviceName;
        this.servicePrice = Math.round(servicePrice * 100.0) / 100.0;
    }

    /**
     * Returns true if a given string is a valid service name.
     */
    public static boolean isValidServiceName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Service)) {
            return false;
        }

        Service otherService = (Service) other;
        return serviceName.equals(otherService.serviceName)
                && servicePrice == otherService.servicePrice;
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return "[Service: " + serviceName + ", Price: " + servicePrice + ']';
    }
}

