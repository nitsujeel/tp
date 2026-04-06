package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.session.SessionEntry;

/**
 * An UI component that displays information of a {@code Session}.
 */
public class SessionCard extends UiPart<Region> {

    private static final String FXML = "SessionCard.fxml";

    public final SessionEntry entry;

    @FXML
    private HBox cardPane;
    @FXML
    private Label id;
    @FXML
    private Label ownerPet;
    @FXML
    private Label startTime;
    @FXML
    private Label endTime;
    @FXML
    private Label fee;

    /**
     * Creates a {@code SessionCard} with the given {@code SessionEntry} and index to display.
     */
    public SessionCard(SessionEntry entry, int displayedIndex) {
        super(FXML);
        this.entry = entry;
        id.setText(displayedIndex + ". ");
        ownerPet.setText(entry.ownerName() + " — " + entry.petName());
        startTime.setText("Start: " + entry.session().getStartTime());
        endTime.setText("End:   " + entry.session().getEndTime());
        fee.setText(String.format("Fee:   $%.2f", entry.session().getFee()));
    }
}
