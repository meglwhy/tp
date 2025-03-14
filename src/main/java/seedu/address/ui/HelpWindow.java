package seedu.address.ui;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Stage> {

    public static final String USERGUIDE_URL = "https://ay2425s2-cs2103t-f10-2.github.io/tp/index.html";
    public static final String HELP_MESSAGE =
            "add-household: Adds a household to the household book. Parameters: n/NAME a/ADDRESS p/CONTACT\n"
                    + "Example: add-household n/Smith Family a/123 Main St p/98765432\n\n"
                    + "edit-household: Edits the details of the household identified by the household ID. Existing values will be overwritten by the input values.\n"
                    + "Parameters: id/HOUSEHOLD_ID [n/NAME] [a/ADDRESS] [p/CONTACT] [t/TAG]...\n"
                    + "Example: edit-household id/H1 p/91234567 t/priority t/followup\n\n"
                    + "add-session: Adds a session to a household. Parameters: id/HOUSEHOLD_ID d/DATE (in YYYY-MM-DD) tm/TIME (in HH:mm)\n"
                    + "Example: add-session id/H1 d/2025-03-15 tm/14:30\n\n"
                    + "edit-session: Edits the details of the session identified by the index number. Existing values will be overwritten by the input values.\n"
                    + "Parameters: INDEX (must be a positive integer) [d/DATE] [tm/TIME] [n/NOTE]\n"
                    + "Example: edit-session 1 d/2025-03-16 tm/15:00\n\n"
                    + "list-households: Lists all households\n\n"
                    + "find-household: Finds all households whose names, addresses, or tags contain any of the specified keywords (case-insensitive) and displays them as a list.\n"
                    + "Parameters: KEYWORD [MORE_KEYWORDS]...\n"
                    + "Example: find-household smith\n\n"
                    + "delete-household: Deletes the household identified by the household ID.\n"
                    + "Parameters: id/HOUSEHOLD_ID\n"
                    + "Example: delete-household id/H1\n\n"
                    + "clear: Removes ALL data. Requires confirmation before proceeding.\n\n"
                    + "exit: Exits the application gracefully.\n\n"
                    + "For more information, refer to the User-Guide (link in dialog box) " + USERGUIDE_URL;

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    @FXML
    private Button copyButton;

    @FXML
    private Label helpMessage;

    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public HelpWindow(Stage root) {
        super(FXML, root);
        helpMessage.setText(HELP_MESSAGE);
    }

    /**
     * Creates a new HelpWindow.
     */
    public HelpWindow() {
        this(new Stage());
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
