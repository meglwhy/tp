package seedu.address.ui;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.household.Household;

/**
 * Panel containing the list of households.
 */
public class HouseholdListPanel extends UiPart<Region> {
    private static final String FXML = "HouseholdListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(HouseholdListPanel.class);

    @FXML
    private ListView<Household> householdListView;

    private final SessionListPanel sessionListPanel;

    /**
     * Constructs a {@code HouseholdListPanel} with the given list of households.
     *
     * <p>This constructor initializes the panel with the provided {@code ObservableList} of {@code Household} objects.
     * It sets up the list view and its cell factory to display each household using the
     * {@code HouseholdListViewCell}.</p>
     *
     * @param householdList The list of households to be displayed in the panel.
     */
    public HouseholdListPanel(ObservableList<Household> householdList, SessionListPanel sessionListPanel) {
        super(FXML);
        this.sessionListPanel = sessionListPanel;
        householdListView.setItems(householdList);
        householdListView.setCellFactory(listView -> new HouseholdListViewCell());

        // Add selection listener to control the visibility of the addSessionButton
        householdListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        sessionListPanel.showAddSessionButton(true);
                    } else {
                        sessionListPanel.showAddSessionButton(false);
                    }
                });
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code Household} using a {@code HouseholdCard}.
     */
    class HouseholdListViewCell extends ListCell<Household> {
        @Override
        protected void updateItem(Household household, boolean empty) {
            super.updateItem(household, empty);

            if (empty || household == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(new HouseholdCard(household, getIndex() + 1).getRoot());
            }
        }
    }

    public ListView<Household> getListView() {
        return householdListView;
    }

}
