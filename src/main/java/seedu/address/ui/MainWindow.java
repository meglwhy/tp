package seedu.address.ui;

import java.util.Optional;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.household.Household;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";

    private final Logger logger = LogsCenter.getLogger(getClass());

    private Stage primaryStage;
    private Logic logic;

    // Independent Ui parts residing in this Ui container
    private HouseholdListPanel householdListPanel;
    private SessionListPanel sessionListPanel;
    private ResultDisplay resultDisplay;
    private HelpWindow helpWindow;

    @FXML
    private StackPane commandBoxPlaceholder;

    @FXML
    private StackPane householdListPanelPlaceholder;

    @FXML
    private StackPane sessionListPanelPlaceholder;

    @FXML
    private StackPane resultDisplayPlaceholder;

    @FXML
    private StackPane statusbarPlaceholder;

    /**
     * Creates a {@code MainWindow} with the given {@code Stage} and {@code Logic}.
     */
    public MainWindow(Stage primaryStage, Logic logic) {
        super(FXML, primaryStage);

        // Set dependencies
        this.primaryStage = primaryStage;
        this.logic = logic;

        // Configure the UI
        setWindowDefaultSize(logic.getGuiSettings());

        helpWindow = new HelpWindow();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Fills up all the placeholders of this window.
     */
    void fillInnerParts() {
        // Create the SessionListPanel first
        sessionListPanel = new SessionListPanel(logic.getFilteredSessionList(), logic);
        sessionListPanelPlaceholder.getChildren().add(sessionListPanel.getRoot());

        logic.updateFilteredSessionList(session -> false);
        sessionListPanel.setSelectedHousehold("Select household to view sessions", null);
        sessionListPanel.showAddSessionButton(false);

        // Create the HouseholdListPanel and pass the SessionListPanel to it
        householdListPanel = new HouseholdListPanel(logic.getFilteredHouseholdList(), sessionListPanel);
        householdListPanelPlaceholder.getChildren().add(householdListPanel.getRoot());

        // Add selection listener to household list
        householdListPanel.getListView().getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        // Filter sessions to show only those belonging to the selected household
                        logic.updateFilteredSessionList(session ->
                                session.getHouseholdId().equals(newValue.getId()));
                        // Force refresh the session list panel
                        sessionListPanel.refresh();
                        // Set the household name at the top
                        sessionListPanel.setSelectedHousehold(
                                newValue.getName().toString(),
                                newValue.getId().toString()
                        );
                        // Show the addSessionButton
                        sessionListPanel.showAddSessionButton(true);
                    } else {
                        // If no household is selected, clear the session list
                        logic.updateFilteredSessionList(session -> false);
                        // Indicate that no household is selected
                        sessionListPanel.setSelectedHousehold("Select household to view sessions", null);
                        // Hide the addSessionButton
                        sessionListPanel.showAddSessionButton(false);
                    }
                });

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());

        StatusBarFooter statusBarFooter = new StatusBarFooter(logic.getHouseholdBookFilePath());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        CommandBox commandBox = new CommandBox(this::executeCommand);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());
    }

    /**
     * Sets the default size as 900x700, but overrides with saved {@code guiSettings} if available.
     */
    private void setWindowDefaultSize(GuiSettings guiSettings) {
        primaryStage.setWidth(900);
        primaryStage.setHeight(700);

        if (guiSettings.getWindowWidth() > 0 && guiSettings.getWindowHeight() > 0) {
            primaryStage.setWidth(guiSettings.getWindowWidth());
            primaryStage.setHeight(guiSettings.getWindowHeight());
        }

        if (guiSettings.getWindowCoordinates() != null) {
            primaryStage.setX(guiSettings.getWindowCoordinates().getX());
            primaryStage.setY(guiSettings.getWindowCoordinates().getY());
        }
    }

    /**
     * Displays a confirmation dialog and returns true if the user confirms.
     */
    public static boolean showClearConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Clear");
        alert.setHeaderText("WARNING: This will remove ALL data.");
        alert.setContentText("Are you sure you want to proceed? Press 'OK' to proceed, or 'Cancel' to abort.");

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * Displays a confirmation dialog before deleting a household.
     * @return true if user confirms, false otherwise.
     */
    public static boolean showDeleteConfirmationDialog(Household household) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("WARNING: You are about to delete a household.");
        alert.setContentText("Are you sure you want to delete " + household + "? This action cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * Opens the help window or focuses on it if it's already opened.
     */
    @FXML
    public void handleHelp() {
        if (!helpWindow.isShowing()) {
            helpWindow.show();
        } else {
            helpWindow.focus();
        }
    }

    void show() {
        primaryStage.show();
    }

    /**
     * Closes the application.
     */
    @FXML
    private void handleExit() {
        GuiSettings guiSettings = new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(),
                (int) primaryStage.getX(), (int) primaryStage.getY());
        logic.setGuiSettings(guiSettings);
        helpWindow.hide();
        primaryStage.hide();
    }

    /**
     * Executes the command and returns the result.
     *
     * @see seedu.address.logic.Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());

            // Extract household id from relevant commands
            if (commandText.trim().startsWith("view-household-sessions")
                    || commandText.trim().startsWith("add-session")
                    || commandText.trim().startsWith("edit-session")) {
                String[] parts = commandText.split("\\s+");
                String targetId = null;
                for (String part : parts) {
                    if (part.startsWith("id/")) {
                        targetId = part.substring(3); // remove "id/"
                        if (targetId.contains("-")) {
                            targetId = targetId.split("-")[0]; // In case of "H000001-1", just get "H000001"
                        }
                        break;
                    }
                }
                if (targetId != null) {
                    householdListPanel.selectHouseholdById(targetId);
                }
            }

            if (commandResult.isShowHelp()) {
                handleHelp();
            }

            if (commandResult.isExit()) {
                handleExit();
            }

            return commandResult;
        } catch (CommandException | ParseException e) {
            logger.info("Invalid command: " + commandText);
            resultDisplay.setFeedbackToUser(e.getMessage());
            throw e;
        }
    }
}
