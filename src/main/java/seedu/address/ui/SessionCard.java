package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.logic.Logic;
import seedu.address.logic.commands.EditSessionCommand;
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
        time.setText("Time: " + session.