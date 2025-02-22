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

    public HouseholdListPanel(ObservableList<Household> householdList) {
        super(FXML);
        householdListView.setItems(householdList);
        householdListView.setCellFactory(listView -> new HouseholdListViewCell());
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
} 