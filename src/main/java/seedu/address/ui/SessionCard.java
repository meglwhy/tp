package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.EditSessionCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.session.Session;

/**
 * An UI component that displays information of a {@code Session}.
 */
public class SessionCard extends UiPart<Region> {

    private static final String FXML = "SessionCard.fxml";

    public final Session session;

    @FXML
    private Label id;
    @FXML
    private Label householdId;
    @FXML
    private Label date;
    @FXML
    private Label time;
    @FXML
    private Label note;
    @FXML
    private Button editSessionButton;

    private final Logic logic;
    private final Runnable refreshCallback;

    /**
     * Constructs a {@code SessionCard} with the specified session, displayed index, and logic instance.
     *
     * <p>This constructor initializes the card to display the details of the given {@code Session} object
     * at the specified index. It sets up the session's date, time, and note (if present). The constructor also
     * configures the action for the "Edit Session" button to open the edit dialog for that session.</p>
     *
     * @param session The {@code Session} object whose details are to be displayed on the card.
     * @param displayedIndex The index at which the session appears in the list, used to display its position.
     * @param logic The {@code Logic} instance used to manage and process session-related operations.
     */
    public SessionCard(Session session, int displayedIndex, Logic logic, Runnable refreshCallback) {
        super(FXML);
        assert session != null : "Session must not be null";
        assert logic != null : "Logic must not be null";
        assert refreshCallback != null : "Refresh callback must not be null";
        this.session = session;
        this.logic = logic;
        this.refreshCallback = refreshCallback;
        householdId.setText("Household ID: " + session.getHouseholdId().toString());
        id.setText("Session " + displayedIndex + " ");
        date.setText("Date: " + session.getDate().toString());
        time.setText("Time: " + session.getTime().toString());
        if (session.hasNote()) {
            note.setText("Note: " + session.getNote().toString());
        } else {
            note.setVisible(false);
        }

        editSessionButton.setOnAction(event -> showEditSessionDialog(displayedIndex));
    }

    /**
     * Shows a dialog to edit session details (date/time) and add a note.
     */
    private void showEditSessionDialog(int sessionIndex) {
        // We need the actual ID from the session's HouseholdId + the 1-based index:
        String householdIdStr = session.getHouseholdId().toString();
        int index = sessionIndex; // This is the 1-based index from outside

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Edit / Add Note");
        dialog.setHeaderText("Update Session Information");

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(
                javafx.scene.control.ButtonType.OK,
                javafx.scene.control.ButtonType.CANCEL
        );

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Household ID:"), 0, 0);

        String originalDate = session.getDate().toString();
        String originalTime = session.getTime().toString();
        String originalNote = session.hasNote() ? session.getNote().toString() : "";

        Label householdIdValue = new Label(householdIdStr);

        TextField dateField = new TextField(originalDate);
        TextField timeField = new TextField(originalTime);
        TextField noteField = new TextField(originalNote);

        grid.add(householdIdValue, 1, 0);
        grid.add(new Label("Date (YYYY-MM-DD):"), 0, 1);
        grid.add(dateField, 1, 1);
        grid.add(new Label("Time (HH:mm):"), 0, 2);
        grid.add(timeField, 1, 2);
        grid.add(new Label("Note:"), 0, 3);
        grid.add(noteField, 1, 3);

        dialogPane.setContent(grid);

        // When user clicks OK, we get the new values
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == javafx.scene.control.ButtonType.OK) {
                // If user changed date/time, run edit-s
                String newDate = dateField.getText().trim();
                String newTime = timeField.getText().trim();
                String newNote = noteField.getText().trim();

                handleSessionUpdate(householdIdStr, index, originalDate, originalTime, originalNote,
                        newDate, newTime, newNote);
            }
            return null;
        });

        dialog.showAndWait();
    }

    /**
     * Checks what changed and issues appropriate edit-s command.
     * Now uses the enhanced edit-s command that can handle both date/time and note updates.
     */
    private void handleSessionUpdate(String householdIdStr, int index,
                                     String oldDate, String oldTime, String oldNote,
                                     String newDate, String newTime, String newNote) {
        boolean dateOrTimeChanged = !newDate.equals(oldDate) || !newTime.equals(oldTime);
        boolean noteChanged = !newNote.equals(oldNote);

        // If either date/time or note changed, use edit-s command
        if (dateOrTimeChanged || noteChanged) {
            String editCommand;
            // If note changed, include it in the command
            if (noteChanged) {
                editCommand = String.format(
                        "%s id/%s-%d d/%s tm/%s n/%s",
                        EditSessionCommand.COMMAND_WORD, householdIdStr, index, newDate, newTime, newNote
                );
            } else {
                // Only date/time changed
                editCommand = String.format(
                        "%s id/%s-%d d/%s tm/%s",
                        EditSessionCommand.COMMAND_WORD, householdIdStr, index, newDate, newTime
                );
            }
            try {
                CommandResult result = logic.execute(editCommand);
                refreshCallback.run();
                if (dateOrTimeChanged && noteChanged) {
                    showInfoDialog("Session Updated",
                            "Successfully updated date, time, and note:\n" + result.getFeedbackToUser());
                } else if (dateOrTimeChanged) {
                    showInfoDialog("Session Updated",
                            "Successfully updated date/time:\n" + result.getFeedbackToUser());
                } else {
                    showInfoDialog("Note Updated",
                            "Successfully updated note:\n" + result.getFeedbackToUser());
                }
            } catch (CommandException | ParseException e) {
                showErrorDialog("Failed to Update Session", e.getMessage());
            }
        } else {
            showInfoDialog("No Changes Detected", "No new date/time or note entered.");
        }
    }

    /** Shows an informational dialog. */
    private void showInfoDialog(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /** Shows an error dialog. */
    private void showErrorDialog(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof SessionCard)) {
            return false;
        }

        SessionCard card = (SessionCard) other;
        return id.getText().equals(card.id.getText())
                && session.equals(card.session);
    }
}
