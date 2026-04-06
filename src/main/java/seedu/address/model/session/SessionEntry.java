package seedu.address.model.session;

/**
 * A display-layer record bundling a {@code Session} with its owner and pet context.
 *
 * @param session   The session data.
 * @param ownerName The name of the owner the session belongs to.
 * @param petName   The name of the pet the session belongs to.
 */
public record SessionEntry(Session session, String ownerName, String petName) {}
