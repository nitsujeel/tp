package seedu.address.model.service;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.commons.util.StringUtil.normalizeForComparison;
import static seedu.address.commons.util.StringUtil.normalizeWhitespace;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Objects;

/**
 * Represents a service in PetLog.
 * Guarantees: immutable; name is valid as declared in {@link #isValidServiceName(String)}
 */
public class Service {

    public static final String MESSAGE_CONSTRAINTS = "Service name must be 1 to 30 characters.";
    public static final int MIN_NAME_LENGTH = 1;
    public static final int MAX_NAME_LENGTH = 30;
    public static final String MESSAGE_PRICE_CONSTRAINTS = "Service price must be a number from 0 to 10000 inclusive, "
            + "with up to 2 decimal places. Only digits and '.' are allowed.";
    public static final String PRICE_VALIDATION_REGEX = "\\d+(?:\\.\\d{1,2})?";
    public static final double MIN_PRICE = 0.0;
    public static final double MAX_PRICE = 10000.0;

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
        String normalizedServiceName = normalizeWhitespace(serviceName);
        checkArgument(isValidServiceName(normalizedServiceName), MESSAGE_CONSTRAINTS);
        checkArgument(isValidServicePrice(servicePrice), MESSAGE_PRICE_CONSTRAINTS);
        this.serviceName = normalizedServiceName;
        this.servicePrice = roundTo2Dp(servicePrice);
    }

    /**
     * Returns true if a given string is a valid service name.
     */
    public static boolean isValidServiceName(String test) {
        requireNonNull(test);
        int normalizedLength = normalizeWhitespace(test).length();
        return normalizedLength >= MIN_NAME_LENGTH && normalizedLength <= MAX_NAME_LENGTH;
    }

    /**
     * Returns true if a given string is a valid service price.
     */
    public static boolean isValidServicePrice(String test) {
        requireNonNull(test);
        String normalizedPrice = normalizeWhitespace(test);
        if (!normalizedPrice.matches(PRICE_VALIDATION_REGEX)) {
            return false;
        }

        BigDecimal price = new BigDecimal(normalizedPrice);
        return price.compareTo(BigDecimal.valueOf(MIN_PRICE)) >= 0
                && price.compareTo(BigDecimal.valueOf(MAX_PRICE)) <= 0;
    }

    /**
     * Returns true if a given number is a valid service price.
     */
    public static boolean isValidServicePrice(double test) {
        return Double.isFinite(test)
                && test >= MIN_PRICE
                && test <= MAX_PRICE
                && Math.abs(test - roundTo2Dp(test)) < 1e-9;
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
                && hasSameName(otherService.getName());
    }

    /**
     * Returns true if this service has the same name as {@code otherServiceName}, ignoring case
     * and collapsing long whitespace into a single space.
     */
    public boolean hasSameName(String otherServiceName) {
        requireNonNull(otherServiceName);
        return normalizeForComparison(getName()).equals(normalizeForComparison(otherServiceName));
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceName, servicePrice);
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return String.format(Locale.ROOT, "Name: %s; Price: $%.2f", serviceName, servicePrice);
    }
}
