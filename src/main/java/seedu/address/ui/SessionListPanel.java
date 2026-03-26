package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.session.Session;

/**
 * Panel containing the list of sessions for the currently selected pet.
 */
public class SessionListPanel extends UiPart<Region> {

    private static final String FXML = "SessionListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(SessionListPanel.class);

    @FXML
    private Label sessionPanelTitle;

    @FXML
    private ListView<Session> sessionListView;

    /**
     * Creates a {@code SessionListPanel} bound to the given observable session list.
     * The panel updates automatically whenever the list changes.
     */
    public SessionListPanel(ObservableList<Session> sessionList) {
        super(FXML);
        sessionListView.setItems(sessionList);
        sessionListView.setCellFactory(listView -> new SessionListViewCell());
        Label placeholder = new Label("No sessions to display.\nUse addsession to add a session.");
        placeholder.getStyleClass().add("cell_small_label");
        sessionListView.setPlaceholder(placeholder);
    }

    /**
     * Updates the title shown at the top of the session panel.
     */
    public void setTitle(String title) {
        sessionPanelTitle.setText(title);
    }

    /**
     * Custom {@code ListCell} that displays a {@code Session} using a {@code SessionCard}.
     */
    class SessionListViewCell extends ListCell<Session> {
        @Override
        protected void updateItem(Session session, boolean empty) {
            super.updateItem(session, empty);
            if (empty || session == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new SessionCard(session, getIndex() + 1).getRoot());
            }
        }
    }
}
