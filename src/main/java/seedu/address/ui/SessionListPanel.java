package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.AddSessionCommand;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.session.Session;

/**
 * Panel containing the list of sessions.
 */
public class SessionListPanel extends UiPart<Region> {
    private static final String FXML = "SessionListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(SessionListPanel.class);

    @FXML
    private VBox sessionListContainer;

    @FXML
    private Label selectedHouseholdLabel;

    @FXML
    private Button addSessionButton;

    @FXML
    private ListView<Session> sessionListView;

    private final Logic logic;
    private String selectedHouseholdId = null;


    /**
     * Creates a {@code SessionListPanel} with the given {@code ObservableList} and {@code Logic}.
     */
    public SessionListPanel(ObservableList<Session> sessionList, Logic logic) {
        super(FXML);
        assert sessionList != null : "Session list must not be null";
        assert logic != null : "Logic must not be null";
        this.logic = logic;

        // Sort sessions by date (descending), then time (descending)
        SortedList<Session> sortedSessions = new SortedList<>(sessionList, (
                s1, s2) -> {
                    int dateCompare = s2.getDate().compareTo(s1.getDate()); // Newest date first
                    if (dateCompare != 0) {
                        return dateCompare;
                    }
                    return s2.getTime().compareTo(s1.getTime()); // Newest time first
                });

        sessionListView.setItems(sortedSessions);
        sessionListView.setCellFactory(listView -> new SessionListViewCell());

        addSessionButton.setVisible(false);
        addSessionButton.setOnAction(event -> showAddSessionDialog());
    }

    /**
     * Lets other classes set which household is selected.
     * This updates the top label and toggles the add-session button.
     */
    public void setSelectedHousehold(String householdName, String householdId) {
        selectedHouseholdLabel.setText(householdName);
        this.selectedHouseholdId = householdId;
        addSessionButton.setVisible(true);
    }

    /**
     * Shows a dialog to enter session details and handles adding a new session.
     */
    private void showAddSessionDialog() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Add Session");
        dialog.setHeaderText("Enter Session Details");

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getButtonTypes().addAll(javafx.scene.control.ButtonType.OK, javafx.scene.control.ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField idField = new TextField();
        idField.setPromptText("e.g., H000001");

        if (selectedHouseholdId != null) {
            idField.setText(selectedHouseholdId);
            assert !selectedHouseholdId.isEmpty() : "selectedHouseholdId should not be empty";
        }

        TextField dateField = new TextField();
        TextField timeField = new TextField();

        grid.add(new Label("Household ID:"), 0, 0);
        grid.add(idField, 1, 0);
        grid.add(new Label("Date (YYYY-MM-DD):"), 0, 1);
        grid.add(dateField, 1, 1);
        grid.add(new Label("Time (HH:mm):"), 0, 2);
        grid.add(timeField, 1, 2);

        dialogPane.setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == javafx.scene.control.ButtonType.OK) {
                return idField.getText() + " " + dateField.getText() + " " + timeField.getText();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            String[] inputs = result.split(" ");
            if (inputs.length == 3) {
                handleAddSession(inputs[0], inputs[1], inputs[2]);
            } else {
                showError("Invalid input. Please enter all details correctly.");
            }
        });
    }

    /**
     * Handles adding a new session with provided details.
     */
    private void handleAddSession(String householdId, String date, String time) {
        try {
            // Ensure the command format matches what CLI expects
            String command = String.format("%s id/%s d/%s tm/%s",
                    AddSessionCommand.COMMAND_WORD, householdId, date, time);

            CommandResult result = logic.execute(command);
            logger.info("Added new session: " + result.getFeedbackToUser());
            refresh();
            assert sessionListView.getItems() != null : "Session list should be refreshed and not null";
        } catch (CommandException | ParseException e) {
            showError("Failed to add session: " + e.getMessage());
        }
    }

    /**
     * Displays an error message.
     */
    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Session Addition Failed");
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Session} using a {@code SessionCard}.
     */
    class SessionListViewCell extends ListCell<Session> {
        @Override
        protected void updateItem(Session session, boolean empty) {
            super.updateItem(session, empty);

            if (empty || session == null) {
                setGraphic(null);
                setText(null);
                setStyle("-fx-background-color: white");
            } else {
                setGraphic(new SessionCard(session, getIndex() + 1,
                        logic, SessionListPanel.this::refresh).getRoot());
                if (getIndex() % 2 == 0) {
                    setStyle("-fx-background-color: #FFFFFF;"); // white
                } else {
                    setStyle("-fx-background-color: #FFF5E1;"); // light orange
                }
            }
        }
    }

    /**
     * Forces a refresh of the session list view.
     */
    public void refresh() {
        sessionListView.refresh();
    }

    /**
     * Controls the visibility of the addSessionButton.
     * @param visible true to show the button, false to hide it.
     */
    public void showAddSessionButton(boolean visible) {
        addSessionButton.setVisible(visible);
    }
}
