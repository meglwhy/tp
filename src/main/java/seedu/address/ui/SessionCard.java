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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
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
    private HBox cardPanel;
    @FXML
    private Label id;
    @FXML
    private Label date;
    @FXML
    private Label time;
    @FXML
    private Label note;
    @FXML
    private Button editSessionButton;

    private final Logic logic;

    public SessionCard(Session session, int displayedIndex, Logic logic) {
        super(FXML);
        this.session = session;
        this.logic = logic;
        id.setText(displayedIndex + ". ");
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

        // Original values
        String originalDate = session.getDate().toString();
        String originalTime = session.getTime().toString();
        String originalNote = session.hasNote() ? session.getNote().toString() : "";

        TextField dateField = new TextField(originalDate);
        TextField timeField = new TextField(originalTime);
        TextField noteField = new TextField(originalNote);

        grid.add(new Label("Date (YYYY-MM-DD):"), 0, 0);
        grid.add(dateField, 1, 0);
        grid.add(new Label("Time (HH:mm):"), 0, 1);
        grid.add(timeField, 1, 1);
        grid.add(new Label("Note:"), 0, 2);
        grid.add(noteField, 1, 2);

        dialogPane.setContent(grid);

        // When user clicks OK, we get the new values
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == javafx.scene.control.ButtonType.OK) {
                // If user changed date/time, run edit-session
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
     * Checks what changed and issues either or both commands:
     * - {@code edit-session id/HOUSEHOLD_ID-INDEX d/DATE t/TIME}
     * - {@code add-note    id/HOUSEHOLD_ID-INDEX n/NOTE}
     */
    private void handleSessionUpdate(String householdIdStr, int index,
                                     String oldDate, String oldTime, String oldNote,
                                     String newDate, String newTime, String newNote) {
        boolean dateOrTimeChanged = !newDate.equals(oldDate) || !newTime.equals(oldTime);
        boolean noteChanged = !newNote.equals(oldNote);

        // 1) If date/time changed, run "edit-session id/H000006-2 d/DATE t/TIME"
        if (dateOrTimeChanged) {
            String editCommand = String.format(
                    "edit-session id/%s-%d d/%s t/%s",
                    householdIdStr, index, newDate, newTime
            );
            try {
                CommandResult result = logic.execute(editCommand);
                showInfoDialog("Session Updated", "Successfully updated date/time:\n" + result.getFeedbackToUser());
            } catch (CommandException | ParseException e) {
                showErrorDialog("Failed to Edit Session", e.getMessage());
            }
        }

        // 2) If note changed, run "add-note id/H000006-2 n/NOTE"
        if (noteChanged) {
            String noteCommand = String.format(
                    "add-note id/%s-%d n/%s",
                    householdIdStr, index, newNote
            );
            try {
                CommandResult result = logic.execute(noteCommand);
                showInfoDialog("Note Added/Updated", result.getFeedbackToUser());
            } catch (CommandException | ParseException e) {
                showErrorDialog("Failed to Add Note", e.getMessage());
            }
        }

        // If neither changed, do nothing (no commands).
        if (!dateOrTimeChanged && !noteChanged) {
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
