package seedu.address.ui;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.service.Service;
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
    @FXML
    private Label services;

    /**
     * Creates a {@code SessionCard} with the given {@code SessionEntry} and index to display.
     */
    public SessionCard(SessionEntry entry, int displayedIndex) {
        super(FXML);
        this.entry = entry;
        id.setText("");
        ownerPet.setText(String.format("Session #%d", displayedIndex));
        startTime.setText("Start: " + entry.session().getStartTime());
        endTime.setText("End:   " + entry.session().getEndTime());
        services.setText("Service(s): " + formatServices(entry));
        fee.setText(String.format("Total Fee:   $%.2f", entry.session().getFee()));
    }

    private String formatServices(SessionEntry sessionEntry) {
        if (sessionEntry.session().getServices().isEmpty()) {
            return "None";
        }

        Map<String, Long> serviceCounts = sessionEntry.session().getServices().stream()
                .collect(Collectors.groupingBy(Service::getName, LinkedHashMap::new, Collectors.counting()));

        return serviceCounts.entrySet().stream()
                .map(entry -> entry.getValue() > 1
                        ? String.format("%s x%d", entry.getKey(), entry.getValue())
                        : entry.getKey())
                .collect(Collectors.joining(", "));
    }
}
