package seedu.address.ui;

import java.util.List;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.commands.AddOwnerCommand;
import seedu.address.logic.commands.AddPetCommand;
import seedu.address.logic.commands.AddServiceCommand;
import seedu.address.logic.commands.AddSessionCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.UpdatePetRemarkCommand;

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Stage> {

    public static final String USERGUIDE_URL = "https://ay2526s2-cs2103t-w14-1.github.io/tp/UserGuide.html";
    public static final String HELP_MESSAGE = "For more detailed guide, refer to the user guide.";

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    /** Each entry is [commandWord, fullUsageText]. */
    private static final List<String[]> COMMAND_ENTRIES = List.of(
            new String[]{HelpCommand.COMMAND_WORD, HelpCommand.MESSAGE_USAGE},
            new String[]{AddOwnerCommand.COMMAND_WORD, AddOwnerCommand.MESSAGE_USAGE},
            new String[]{EditCommand.COMMAND_WORD, EditCommand.MESSAGE_USAGE},
            new String[]{AddPetCommand.COMMAND_WORD, AddPetCommand.MESSAGE_USAGE},
            new String[]{UpdatePetRemarkCommand.COMMAND_WORD, UpdatePetRemarkCommand.MESSAGE_USAGE},
            new String[]{AddServiceCommand.COMMAND_WORD, AddServiceCommand.MESSAGE_USAGE},
            new String[]{AddSessionCommand.COMMAND_WORD, AddSessionCommand.MESSAGE_USAGE},
            new String[]{DeleteCommand.COMMAND_WORD, DeleteCommand.MESSAGE_USAGE},
            new String[]{FindCommand.COMMAND_WORD, FindCommand.MESSAGE_USAGE},
            new String[]{ListCommand.COMMAND_WORD, ListCommand.MESSAGE_USAGE},
            new String[]{ClearCommand.COMMAND_WORD, ClearCommand.MESSAGE_USAGE},
            new String[]{ExitCommand.COMMAND_WORD, ExitCommand.MESSAGE_USAGE}
    );

    @FXML
    private Button copyButton;

    @FXML
    private Label helpMessage;

    @FXML
    private Accordion commandsAccordion;

    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public HelpWindow(Stage root) {
        super(FXML, root);
        helpMessage.setText(HELP_MESSAGE);
        buildCommandsAccordion();
    }

    /**
     * Creates a new HelpWindow.
     */
    public HelpWindow() {
        this(new Stage());
    }

    private void buildCommandsAccordion() {
        for (String[] entry : COMMAND_ENTRIES) {
            String commandWord = entry[0];
            String usageText = entry[1];

            Label contentLabel = new Label(usageText);
            contentLabel.setWrapText(true);
            contentLabel.getStyleClass().add("command-content-label");

            VBox contentBox = new VBox(contentLabel);
            contentBox.getStyleClass().add("command-content-box");

            TitledPane pane = new TitledPane(commandWord, contentBox);
            pane.getStyleClass().add("command-pane");
            commandsAccordion.getPanes().add(pane);
        }
    }

    /**
     * Shows the help window.
     * @throws IllegalStateException
     *     <ul>
     *         <li>
     *             if this method is called on a thread other than the JavaFX Application Thread.
     *         </li>
     *         <li>
     *             if this method is called during animation or layout processing.
     *         </li>
     *         <li>
     *             if this method is called on the primary stage.
     *         </li>
     *         <li>
     *             if {@code dialogStage} is already showing.
     *         </li>
     *     </ul>
     */
    public void show() {
        logger.fine("Showing help page about the application.");
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /**
     * Returns true if the help window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the help window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Focuses on the help window.
     */
    public void focus() {
        getRoot().requestFocus();
    }

    /**
     * Copies the URL to the user guide to the clipboard.
     */
    @FXML
    private void copyUrl() {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent url = new ClipboardContent();
        url.putString(USERGUIDE_URL);
        clipboard.setContent(url);
    }
}
