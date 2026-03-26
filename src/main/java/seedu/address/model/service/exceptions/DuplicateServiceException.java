package seedu.address.model.service.exceptions;


/**
 * Signals that the operation will result in duplicate Services (Services are considered duplicates if they have the
 * same identity).
 */
public class DuplicateServiceException extends RuntimeException {
    public DuplicateServiceException() {
        super("Operation would result in duplicate services");
    }
}
