package seedu.address.ui;

import java.util.Optional;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.AddSessionCommand;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.EditSessionCommand;
import seedu.address.logic.commands.ViewFullSessionCommand;
import seedu.address.logic.commands.ViewHouseholdSessionsCommand;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.CliSyntax;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.household.Household;
import seedu.address.model.session.Session;

/**
 * The Main Window. Provides the basic application layout containing
 * a menu bar and space where other JavaFX elements can be placed.
 */
public class MainWindow extends UiPart<Stage> {

    private static final String FXML = "MainWindow.fxml";
    private static final Logger logger = LogsCenter.getLogger(MainWindow.class);

    private final Stage primaryStage;
    private final Logic logic;

    // Independent UI parts
    private HouseholdListPanel householdListPanel;
    private SessionListPanel sessionListPanel;
    private ResultDisplay resultDisplay;
    private final HelpWindow helpWindow;

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

        this.primaryStage = primaryStage;
        this.logic = logic;
        this.helpWindow = new HelpWindow();

        configureWindowSize(logic.getGuiSettings());
    }

    /**
     * Sets up and shows all the main UI components in the primary stage.
     */
    void fillInnerParts() {
        sessionListPanel = new SessionListPanel(logic.getFilteredSessionList(), logic, null);
        sessionListPanelPlaceholder.getChildren().add(sessionListPanel.getRoot());

        logic.updateFilteredSessionList(session -> false);
        assert logic.getFilteredSessionList().isEmpty()
                : "After filtering out all sessions, the session list must be empty.";

        sessionListPanel.setSelectedHousehold("Select household to view sessions", null);
        sessionListPanel.showAddSessionButton(false);

        householdListPanel = new HouseholdListPanel(logic.getFilteredHouseholdList(), sessionListPanel);
        householdListPanelPlaceholder.getChildren().add(householdListPanel.getRoot());

        setHouseholdSelectionListener();

        resultDisplay = new ResultDisplay();
        resultDisplayPlaceholder.getChildren().add(resultDisplay.getRoot());
        sessionListPanel.setResultDisplay(resultDisplay);

        StatusBarFooter statusBarFooter = new StatusBarFooter(logic.getHouseholdBookFilePath());
        statusbarPlaceholder.getChildren().add(statusBarFooter.getRoot());

        CommandBox commandBox = new CommandBox(this::executeCommand);
        commandBoxPlaceholder.getChildren().add(commandBox.getRoot());
    }

    /**
     * Adds a listener that updates the session list based on which household is selected.
     */
    private void setHouseholdSelectionListener() {
        householdListPanel.getListView().getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        logic.updateFilteredSessionList(session ->
                                session.getHouseholdId().equals(newValue.getId()));
                        sessionListPanel.refresh();

                        sessionListPanel.setSelectedHousehold(
                                newValue.getName().toString(),
                                newValue.getId().toString()
                        );
                        sessionListPanel.showAddSessionButton(true);
                    } else {
                        logic.updateFilteredSessionList(session -> false);
                        sessionListPanel.setSelectedHousehold("Select household to view sessions", null);
                        sessionListPanel.showAddSessionButton(false);
                    }
                });
    }

    /**
     * Sets a default window size and position, but respects saved GUI settings if available.
     */
    private void configureWindowSize(GuiSettings guiSettings) {
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

    public Stage getPrimaryStage() {
        return primaryStage;
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

    /**
     * Displays a pop-up window containing detailed information of a session.
     */
    private void showSessionPopup(String sessionDetails) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(primaryStage);
        alert.setTitle("Session Details");
        alert.setHeaderText("Full Session Information");
        alert.setContentText(sessionDetails);

        // Make the dialog auto-size based on content
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    /**
     * Displays a clear confirmation dialog and returns true if the user confirms.
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
     * Executes the command and returns the result.
     * @see seedu.address.logic.Logic#execute(String)
     */
    private CommandResult executeCommand(String commandText) throws CommandException, ParseException {
        try {
            CommandResult commandResult = logic.execute(commandText);
            logger.info("Result: " + commandResult.getFeedbackToUser());
            resultDisplay.setFeedbackToUser(commandResult.getFeedbackToUser());

            ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(commandText, CliSyntax.PREFIX_ID);
            postProcessCommand(commandText, argMultimap);

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

    /**
     * Handles additional UI updates after executing the command (e.g., auto-select household, show session details).
     */
    private void postProcessCommand(String commandText, ArgumentMultimap argMultimap) {
        autoSelectHouseholdIfApplicable(commandText);
        if (commandText.trim().startsWith(ViewFullSessionCommand.COMMAND_WORD)) {
            handleViewFullSession(argMultimap);
        }
    }

    /**
     * Selects the relevant household if the command text indicates it (view, add, edit).
     */
    private void autoSelectHouseholdIfApplicable(String commandText) {
        String trimmed = commandText.trim();
        if (trimmed.startsWith(ViewHouseholdSessionsCommand.COMMAND_WORD)
                || trimmed.startsWith(AddSessionCommand.COMMAND_WORD)
                || trimmed.startsWith(EditSessionCommand.COMMAND_WORD)) {

            String targetId = extractHouseholdId(commandText);
            if (targetId != null) {
                householdListPanel.selectHouseholdById(targetId);
            }
        }
    }

    /**
     * Extracts the household ID (e.g. "H000001") from a command text by scanning for "id/..."
     */
    private String extractHouseholdId(String commandText) {
        String[] parts = commandText.split("\\s+");
        for (String part : parts) {
            if (part.startsWith("id/")) {
                String idValue = part.substring(3); // remove "id/"
                if (idValue.contains("-")) {
                    return idValue.split("-")[0];
                }
                return idValue;
            }
        }
        return null;
    }

    /**
     * If the command is "view-full-session", parse householdId-sessionIndex and show the session popup.
     */
    private void handleViewFullSession(ArgumentMultimap argMultimap) {
        argMultimap.getValue(CliSyntax.PREFIX_ID).ifPresent(idValue -> {
            String[] parts = idValue.trim().split("-", 2);
            if (parts.length != 2) {
                return;
            }

            String householdIdStr = parts[0];
            try {
                int sessionIndex = Integer.parseInt(parts[1]);
                householdListPanel.selectHouseholdById(householdIdStr);
                sessionListPanel.selectSessionByIndex(sessionIndex - 1);

                logic.getHouseholdBook()
                        .getHouseholdById(
                                seedu.address.model.household.HouseholdId.fromString(householdIdStr))
                        .ifPresent(household -> {
                            if (sessionIndex < 1 || sessionIndex > household.getSessions().size()) {
                                return;
                            }
                            Session session =
                                    household.getSessions().get(sessionIndex - 1);

                            String fullDetails = String.format(
                                    "Household ID: %s\nDate: %s\nTime: %s\nNote: %s",
                                    session.getHouseholdId(),
                                    session.getDate(),
                                    session.getTime(),
                                    session.hasNote() ? session.getNote() : "-"
                            );
                            showSessionPopup(fullDetails);
                        });

            } catch (NumberFormatException nfe) {
                // If parts[1] isn't a valid integer, do nothing or log if desired
            }
        });
    }

}
