package seedu.address.ui;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Stage> {

    public static final String USERGUIDE_URL = "https://ay2425s2-cs2103t-f10-2.github.io/tp/UserGuide.html";
    public static final String HELP_MESSAGE =
            "1. add n/NAME a/ADDRESS p/CONTACT\n"
                    + "2. edit id/HOUSEHOLD_ID [n/NAME] [a/ADDRESS] [p/CONTACT] [t/TAG]...\n"
                    + "3. view-s id/HOUSEHOLD_ID\n"
                    + "4. add-s id/HOUSEHOLD_ID d/DATE (in YYYY-MM-DD) tm/TIME (in HH:mm)\n"
                    + "5. edit-s id/HOUSEHOLD_ID-SESSION_INDEX [d/DATE] [tm/TIME] [n/Follow up]\n"
                    + "6. view-full-s id/HOUSEHOLD_ID-SESSION_INDEX\n"
                    + "7. delete-s id/HOUSEHOLD_ID-SESSION_INDEX\n"
                    + "8. list\n"
                    + "9. find KEYWORD [MORE_KEYWORDS]\n"
                    + "10. delete id/HOUSEHOLD_ID\n"
                    + "11. clear\n"
                    + "12. exit\n\n"
                    + "For more information, refer to the User-Guide (link in dialog box) " + USERGUIDE_URL;

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

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
        assert !helpMessage.getText().isEmpty() : "Help message should not be empty";
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
