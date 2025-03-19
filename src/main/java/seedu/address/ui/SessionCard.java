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
     * Shows a dialog to edit session details.
     */
    private void showEditSessionDialog(int index) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Edit Session");
        dialog.setHeaderText("Edit Session Details");

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(javafx.scene.control.ButtonType.OK, javafx.scene.control.ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField dateField = new TextField(session.getDate().toString());
        TextField timeField = new TextField(session.getTime().toString());
        TextField noteField = new TextField(session.hasNote() ? session.getNote().toString() : "");

        grid.add(new Label("Date (YYYY-MM-DD):"), 0, 0);
        grid.add(dateField, 1, 0);
        grid.add(new Label("Time (HH:mm):"), 0, 1);
        grid.add(timeField, 1, 1);
        grid.add(new Label("Note:"), 0, 2);
        grid.add(noteField, 1, 2);

        dialogPane.setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == javafx.scene.control.ButtonType.OK) {
                return dateField.getText() + " " + timeField.getText() + " " + noteField.getText();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            String[] inputs = result.split(" ", 3);
            if (inputs.length >= 2) {
                handleEditSession(index, inputs[0], inputs[1], inputs.length == 3 ? inputs[2] : "");
            } else {
                showError("Invalid input. Please enter at least a date and time.");
            }
        });
    }

    /**
     * Handles editing a session with provided details.
     */
    private void handleEditSession(int index, String date, String time, String note) {
        try {
            String command = String.format("%s %d d/%s tm/%s %s",
                    EditSessionCommand.COMMAND_WORD, index, date, time, note.isEmpty() ? "" : "n/" + note);

            CommandResult result = logic.execute(command);
            showSuccess("Edited session successfully: " + result.getFeedbackToUser());
        } catch (CommandException | ParseException e) {
            showError("Failed to edit session: " + e.getMessage());
        }
    }

    /**
     * Displays an error message.
     */
    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Session Edit Failed");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Displays a success message.
     */
    private void showSuccess(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Session Edited Successfully");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof SessionCard)) {
            return false;
        }

        // state check
        SessionCard card = (SessionCard) other;
        return id.getText().equals(card.id.getText())
                && session.equals(card.session);
    }
}
