package seedu.address.model.session;

/**
 * A display-layer record bundling a {@code Session} with its owner and pet context.
 *
 * @param session The session data.
 * @param ownerName The name of the owner the session belongs to.
 * @param petName The name of the pet the session belongs to.
 * @param ownerIndex The 1-based owner index in the currently displayed owner list.
 * @param petIndex The 1-based pet index in that owner's pet list.
 * @param sessionIndex The 1-based session index within the pet's own session list.
 */
public record SessionEntry(
        Session session,
        String ownerName,
        String petName,
        int ownerIndex,
        int petIndex,
        int sessionIndex
) {}
