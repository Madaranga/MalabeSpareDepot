package com.example.malabesparedepot.controller;

import com.example.malabesparedepot.data.ApplicationData;
import com.example.malabesparedepot.model.Part;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class LowStockController extends NavigationController {

    private static final int LOW_STOCK_THRESHOLD = 5;

    private final ApplicationData data = ApplicationData.getInstance();
    private FilteredList<Part> lowStockParts;

    @FXML private TableView<Part> tblLowStock;
    @FXML private TableColumn<Part, String> colPartCode;
    @FXML private TableColumn<Part, String> colPartName;
    @FXML private TableColumn<Part, String> colPartCategory;
    @FXML private TableColumn<Part, Integer> colPartQty;
    @FXML private TableColumn<Part, String> colStockStatus;
    @FXML private Label lblLowStockCount;
    @FXML private Label lblThreshold;

    @FXML
    private void initialize() {
        colPartCode.setCellValueFactory(new PropertyValueFactory<>("partCode"));
        colPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPartCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colPartQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colStockStatus.setCellValueFactory(cell -> new ReadOnlyStringWrapper(
                cell.getValue().getQuantity() == 0 ? "OUT OF STOCK" : "LOW STOCK"
        ));
/////
        lowStockParts = new FilteredList<>(
                data.getParts(),
                part -> part.getQuantity() <= LOW_STOCK_THRESHOLD
        );
        tblLowStock.setItems(lowStockParts);
        tblLowStock.setPlaceholder(new Label("No low-stock parts. Inventory levels are healthy."));
        lowStockParts.addListener(
                (javafx.collections.ListChangeListener<Part>) change -> updateStatus()
        );
        lblThreshold.setText("Threshold: " + LOW_STOCK_THRESHOLD + " units or fewer");
        updateStatus();
    }

    @FXML
    private void onRefresh() {
        lowStockParts.setPredicate(null);
        lowStockParts.setPredicate(part -> part.getQuantity() <= LOW_STOCK_THRESHOLD);
        tblLowStock.refresh();
        updateStatus();
    }

    private void updateStatus() {
        lblLowStockCount.setText(lowStockParts.size() + " low-stock items");
    }

    @FXML
    protected void onBackToDashboard(ActionEvent event) {
        super.onBackToDashboard(event);
    }
}
