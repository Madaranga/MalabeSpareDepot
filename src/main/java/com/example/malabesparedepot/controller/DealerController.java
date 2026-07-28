package com.example.malabesparedepot.controller;

import com.example.malabesparedepot.data.ApplicationData;
import com.example.malabesparedepot.model.Dealer;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class DealerController extends NavigationController {

    private final ApplicationData data = ApplicationData.getInstance();
    private FilteredList<Dealer> filteredDealers;

    @FXML private TextField searchField;
    @FXML private TableView<Dealer> tblDealers;
    @FXML private TableColumn<Dealer, String> colDealerId;
    @FXML private TableColumn<Dealer, String> colDealerName;
    @FXML private TableColumn<Dealer, String> colDealerPhone;
    @FXML private TableColumn<Dealer, String> colDealerLocation;
    @FXML private Label lblDealerCount;

    @FXML
    private void initialize() {
        colDealerId.setCellValueFactory(new PropertyValueFactory<>("dealerId"));
        colDealerName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDealerPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colDealerLocation.setCellValueFactory(new PropertyValueFactory<>("location"));

        filteredDealers = new FilteredList<>(data.getDealers(), dealer -> true);
        SortedList<Dealer> sortedDealers = new SortedList<>(filteredDealers);
        sortedDealers.comparatorProperty().bind(tblDealers.comparatorProperty());
        tblDealers.setItems(sortedDealers);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> applyFilter());
        filteredDealers.addListener(
                (javafx.collections.ListChangeListener<Dealer>) change -> updateCount()
        );
        updateCount();
    }

    @FXML
    private void onSearch() {
        applyFilter();
    }

    private void applyFilter() {
        String query = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase();
        filteredDealers.setPredicate(dealer -> query.isEmpty()
                || dealer.getDealerId().toLowerCase().contains(query)
                || dealer.getName().toLowerCase().contains(query)
                || dealer.getPhoneNumber().toLowerCase().contains(query)
                || dealer.getLocation().toLowerCase().contains(query));
        updateCount();
    }

    private void updateCount() {
        lblDealerCount.setText(filteredDealers.size() + " dealers");
    }

    @FXML
    protected void onBackToDashboard(ActionEvent event) {
        super.onBackToDashboard(event);
    }
}
