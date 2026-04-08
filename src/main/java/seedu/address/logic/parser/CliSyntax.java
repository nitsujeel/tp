package seedu.address.logic.parser;

/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    /* Owner-related */
    public static final Prefix PREFIX_OWNER_INDEX = new Prefix("oi/");
    public static final Prefix PREFIX_OWNER_NAME = new Prefix("on/");

    /* Pet-related */
    public static final Prefix PREFIX_PET_INDEX = new Prefix("pi/");
    public static final Prefix PREFIX_PET_NAME = new Prefix("pn/");
    public static final Prefix PREFIX_PET_REMARK = new Prefix("pr/");
    public static final Prefix PREFIX_SPECIES = new Prefix("ps/");

    /* Contact details */
    public static final Prefix PREFIX_ADDRESS = new Prefix("ad/");
    public static final Prefix PREFIX_EMAIL = new Prefix("em/");
    public static final Prefix PREFIX_PHONE = new Prefix("ph/");

    /* Session-related */
    public static final Prefix PREFIX_SESSION_INDEX = new Prefix("si/");
    public static final Prefix PREFIX_END_TIME = new Prefix("et/");
    public static final Prefix PREFIX_START_TIME = new Prefix("st/");

    /* Service-related */
    public static final Prefix PREFIX_SERVICE_NAME = new Prefix("sn/");
    public static final Prefix PREFIX_SERVICE_PRICE = new Prefix("sp/");

    /* Tags */
    public static final Prefix PREFIX_ADD_TAG = new Prefix("at/");
    public static final Prefix PREFIX_TAG = new Prefix("ot/");
    public static final Prefix PREFIX_REMOVE_TAG = new Prefix("rt/");

}
