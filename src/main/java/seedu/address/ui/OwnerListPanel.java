package seedu.address.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;

/**
 * Panel containing the list of owners.
 */
public class OwnerListPanel extends UiPart<Region> {
    private static final String FXML = "OwnerListPanel.fxml";

    @FXML
    private ListView<Person> personListView;

    /**
     * Creates a {@code OwnerListPanel} with the given {@code ObservableList}.
     */
    public OwnerListPanel(ObservableList<Person> personList) {
        super(FXML);
        personListView.setItems(personList);
        personListView.setCellFactory(listView -> new OwnerListViewCell());
    }

    /**
     * Scrolls the owner list to the final row if the list is non-empty.
     */
    public void scrollToLastOwner() {
        int lastOwnerIndex = personListView.getItems().size() - 1;
        if (lastOwnerIndex >= 0) {
            personListView.scrollTo(lastOwnerIndex);
        }
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Person} using a {@code OwnerCard}.
     */
    class OwnerListViewCell extends ListCell<Person> {
        OwnerListViewCell() {
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }

        @Override
        protected void updateItem(Person person, boolean empty) {
            super.updateItem(person, empty);

            if (empty || person == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic((Region) new OwnerCard(person, getIndex() + 1).getRoot());
            }
        }

        @Override
        protected double computePrefHeight(double width) {
            if (getGraphic() instanceof Region cardRoot) {
                double availableWidth = Math.max(0, width - snappedLeftInset() - snappedRightInset());
                cardRoot.setPrefWidth(availableWidth);
                return snappedTopInset() + cardRoot.prefHeight(availableWidth) + snappedBottomInset();
            }
            return super.computePrefHeight(width);
        }

        @Override
        protected void layoutChildren() {
            if (getGraphic() instanceof Region cardRoot) {
                double availableWidth = Math.max(0, getWidth() - snappedLeftInset() - snappedRightInset());
                cardRoot.setPrefWidth(availableWidth);
            }
            super.layoutChildren();
        }
    }

}
