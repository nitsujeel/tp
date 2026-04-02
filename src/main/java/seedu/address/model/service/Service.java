package seedu.address.model.service;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Objects;

/**
 * Represents a service in PetLog.
 * Guarantees: immutable; name is valid as declared in {@link #isValidServiceName(String)}
 */
public class Service {

    public static final String MESSAGE_CONSTRAINTS = "Service names should only contain 1-30 alphanumeric characters "
            + "or spaces";
    public static final String VALIDATION_REGEX = "(?=.{1,30}$)\\p{Alnum}+(?: \\p{Alnum}+)*";
    public static final String MESSAGE_PRICE_CONSTRAINTS = "Service price should be a non-negative number with up to "
            + "2 decimal places";
    public static final String PRICE_VALIDATION_REGEX = "\\d+(?:\\.\\d{1,2})?";

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
        checkArgument(isValidServicePrice(servicePrice), MESSAGE_PRICE_CONSTRAINTS);
        this.serviceName = serviceName;
        this.servicePrice = roundTo2Dp(servicePrice);
    }

    /**
     * Returns true if a given string is a valid service name.
     */
    public static boolean isValidServiceName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Returns true if a given string is a valid service price.
     */
    public static boolean isValidServicePrice(String test) {
        return test.matches(PRICE_VALIDATION_REGEX);
    }

    /**
     * Returns true if a given number is a valid service price.
     */
    public static boolean isValidServicePrice(double test) {
        return Double.isFinite(test) && test >= 0 && Math.abs(test - roundTo2Dp(test)) < 1e-9;
    }

    private static double roundTo2Dp(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public String getName() {
        return this.serviceName;
    }

    public double getCost() {
        return this.servicePrice;
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
                && Double.compare(servicePrice, otherService.servicePrice) == 0;
    }

    /**
     * Returns true if both services have the same name.
     * This defines a weaker notion of equality between two services.
     */
    public boolean isSameService(Service otherService) {
        if (otherService == this) {
            return true;
        }

        return otherService != null
                && otherService.getName().equalsIgnoreCase(getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceName, servicePrice);
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return "[Service: " + serviceName + ", Price: " + servicePrice + ']';
    }
}
