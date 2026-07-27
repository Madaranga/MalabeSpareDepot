package com.example.malabesparedepot.controller;

import com.example.malabesparedepot.data.ApplicationData;
import com.example.malabesparedepot.model.CartItem;
import com.example.malabesparedepot.model.Dealer;
import com.example.malabesparedepot.model.Part;
import com.example.malabesparedepot.util.LoggerUtil;
import com.example.malabesparedepot.util.PriceCalculator;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class SalesController extends NavigationController {

    private final ApplicationData data = ApplicationData.getInstance();
    private FilteredList<Part> filteredParts;

    @FXML private TextField txtSearch;
    @FXML private TextField txtCartQty;
    @FXML private TableView<Part> tblAvailableParts;
    @FXML private TableColumn<Part, String> colPartCode;
    @FXML private TableColumn<Part, String> colPartName;
    @FXML private TableColumn<Part, String> colPartBrand;
    @FXML private TableColumn<Part, Double> colPartPrice;
    @FXML private TableColumn<Part, Integer> colPartStock;
    @FXML private TableView<CartItem> tblCart;
    @FXML private TableColumn<CartItem, String> colCartCode;
    @FXML private TableColumn<CartItem, String> colCartName;
    @FXML private TableColumn<CartItem, Integer> colCartQty;
    @FXML private TableColumn<CartItem, Double> colCartSubtotal;
    @FXML private ComboBox<Dealer> cmbDealer;
    @FXML private Label lblTotal;
    @FXML private Label lblStatus;

    @FXML
    private void initialize() {
        configureTables();
        cmbDealer.setItems(data.getSalesDealers());
        cmbDealer.getSelectionModel().selectFirst();

        filteredParts = new FilteredList<>(data.getParts(), part -> true);
        SortedList<Part> sortedParts = new SortedList<>(filteredParts);
        sortedParts.comparatorProperty().bind(tblAvailableParts.comparatorProperty());
        tblAvailableParts.setItems(sortedParts);
        tblCart.setItems(data.getCartItems());

        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> applySearch());
        data.getCartItems().addListener(
                (javafx.collections.ListChangeListener<CartItem>) change -> updateTotal()
        );
        tblAvailableParts.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, selected) -> {
                    if (selected != null && txtCartQty.getText().isBlank()) {
                        txtCartQty.setText("1");
                    }
                }
        );
        updateTotal();
    }

    private void configureTables() {
        colPartCode.setCellValueFactory(new PropertyValueFactory<>("partCode"));
        colPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPartBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colPartStock.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colCartCode.setCellValueFactory(new PropertyValueFactory<>("partCode"));
        colCartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCartQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colCartSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
    }

    @FXML
    private void onSearch() {
        applySearch();
    }

    private void applySearch() {
        String query = txtSearch.getText() == null ? "" : txtSearch.getText().trim().toLowerCase();
        filteredParts.setPredicate(part -> query.isEmpty()
                || part.getPartCode().toLowerCase().contains(query)
                || part.getName().toLowerCase().contains(query)
                || part.getBrand().toLowerCase().contains(query)
                || part.getCategory().toLowerCase().contains(query));
    }

    @FXML
    private void onAddToCart() {
        Part part = tblAvailableParts.getSelectionModel().getSelectedItem();
        if (part == null) {
            showAlert(Alert.AlertType.WARNING, "No Part Selected",
                    "Select an available part before adding it to the cart.");
            return;
        }

        int requestedQuantity;
        try {
            requestedQuantity = Integer.parseInt(txtCartQty.getText().trim());
            if (requestedQuantity <= 0) {
                throw new NumberFormatException("Quantity is not positive");
            }
        } catch (NumberFormatException exception) {
            showAlert(Alert.AlertType.ERROR, "Invalid Quantity",
                    "Quantity must be a positive whole number.");
            return;
        }

        CartItem existing = findCartItem(part);
        int existingQuantity = existing == null ? 0 : existing.getQuantity();
        if (existingQuantity + requestedQuantity > part.getQuantity()) {
            showAlert(Alert.AlertType.WARNING, "Insufficient Stock",
                    "Only " + part.getQuantity() + " units are available; "
                            + existingQuantity + " are already in the cart.");
            return;
        }

        if (existing == null) {
            data.getCartItems().add(new CartItem(part, requestedQuantity));
        } else {
            existing.setQuantity(existingQuantity + requestedQuantity);
            data.getCartItems().set(data.getCartItems().indexOf(existing), existing);
        }
        tblCart.refresh();
        txtCartQty.clear();
        setStatus("Added " + part.getName() + " to the cart.");
        updateTotal();
    }

    @FXML
    private void onRemoveFromCart() {
        CartItem selected = tblCart.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Cart Item Selected",
                    "Select a cart row to remove.");
            return;
        }
        data.getCartItems().remove(selected);
        setStatus("Removed " + selected.getName() + " from the cart.");
        updateTotal();
    }

    @FXML
    private void onClearCart() {
        data.getCartItems().clear();
        setStatus("Cart cleared.");
        updateTotal();
    }

    @FXML
    private void onCheckout() {
        if (data.getCartItems().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Empty Cart", "Your shopping cart is empty.");
            return;
        }
        Dealer dealer = cmbDealer.getValue();
        if (dealer == null) {
            showAlert(Alert.AlertType.WARNING, "No Dealer Selected", "Select a dealer.");
            return;
        }

        for (CartItem item : data.getCartItems()) {
            Part part = data.findPartByCode(item.getPartCode());
            if (part == null || item.getQuantity() > part.getQuantity()) {
                showAlert(Alert.AlertType.ERROR, "Stock Changed",
                        item.getName() + " no longer has enough stock. Review the cart.");
                return;
            }
        }

        double finalTotal = PriceCalculator.calculateFinalTotal(data.getCartItems());
        for (CartItem item : data.getCartItems()) {
            Part part = item.getPart();
            part.setQuantity(part.getQuantity() - item.getQuantity());
            data.touchPart(part);
            LoggerUtil.logAction("PURCHASE", item.getPartCode(), item.getQuantity());
        }

        data.getCartItems().clear();
        tblAvailableParts.refresh();
        updateTotal();
        setStatus("Checkout completed for " + dealer.getName() + ".");
        showAlert(Alert.AlertType.INFORMATION, "Transaction Approved",
                String.format("Successfully purchased items for %s.%nFinal paid amount: Rs. %.2f",
                        dealer.getName(), finalTotal));
    }

    private CartItem findCartItem(Part part) {
        for (CartItem item : data.getCartItems()) {
            if (item.getPart() == part) {
                return item;
            }
        }
        return null;
    }

    private void updateTotal() {
        lblTotal.setText(String.format(
                "Total: Rs. %.2f",
                PriceCalculator.calculateFinalTotal(data.getCartItems())
        ));
    }

    private void setStatus(String message) {
        lblStatus.setText(message);
    }

    @FXML
    protected void onBackToDashboard(ActionEvent event) {
        super.onBackToDashboard(event);
    }
}
