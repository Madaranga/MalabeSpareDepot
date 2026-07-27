package com.example.malabesparedepot.controller;

import com.example.malabesparedepot.data.ApplicationData;
import com.example.malabesparedepot.model.CartItem;
import com.example.malabesparedepot.model.Part;
import com.example.malabesparedepot.util.ImageUtil;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class DashboardController extends NavigationController {

    private static final String ALL_CATEGORIES = "All Categories";

    private final ApplicationData data = ApplicationData.getInstance();
    private FilteredList<Part> filteredParts;

    @FXML private TextField searchField;
    @FXML private ComboBox<String> categoryFilter;
    @FXML private TableView<Part> tblDashboard;
    @FXML private TableColumn<Part, String> colDashCode;
    @FXML private TableColumn<Part, String> colDashName;
    @FXML private TableColumn<Part, String> colDashBrand;
    @FXML private TableColumn<Part, Double> colDashPrice;
    @FXML private TableColumn<Part, Integer> colDashQty;
    @FXML private TableColumn<Part, String> colDashCategory;
    @FXML private TableColumn<Part, String> colDashImage;
    @FXML private TableColumn<Part, Part> colDashAction;
    @FXML private Label lblTotalValue;
    @FXML private Label lblTotalParts;
    @FXML private Label lblCartCount;

    @FXML
    private void initialize() {
        configureColumns();

        categoryFilter.setItems(FXCollections.observableArrayList(ALL_CATEGORIES));
        categoryFilter.getItems().addAll(data.getCategories());
        categoryFilter.getSelectionModel().selectFirst();

        filteredParts = new FilteredList<>(data.getParts(), part -> true);
        SortedList<Part> sortedParts = new SortedList<>(filteredParts);
        sortedParts.comparatorProperty().bind(tblDashboard.comparatorProperty());
        tblDashboard.setItems(sortedParts);


        searchField.textProperty().addListener((observable, oldValue, newValue) -> applyFilter());
        categoryFilter.valueProperty().addListener((observable, oldValue, newValue) -> applyFilter());
        filteredParts.addListener((javafx.collections.ListChangeListener<Part>) change -> updateSummary());
        data.getCartItems().addListener(
                (javafx.collections.ListChangeListener<CartItem>) change -> updateCartCount()
        );

        updateSummary();
        updateCartCount();
    }

    private void configureColumns() {
        colDashCode.setCellValueFactory(new PropertyValueFactory<>("partCode"));
        colDashName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDashBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colDashPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colDashQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colDashCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colDashImage.setCellValueFactory(new PropertyValueFactory<>("imagePath"));
        colDashImage.setCellFactory(column -> new TableCell<>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);
                Image image = empty ? null : ImageUtil.loadImage(imagePath);
                imageView.setImage(image);
                imageView.setFitWidth(38);
                imageView.setFitHeight(38);
                imageView.setPreserveRatio(true);
                setGraphic(image == null ? null : imageView);
            }
        });

        colDashAction.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue()));
        colDashAction.setCellFactory(column -> new TableCell<>() {
            private final Button addButton = new Button("Add to Cart");

            {
                addButton.getStyleClass().add("table-action-button");
                addButton.setOnAction(event -> addOneToCart(getItem()));
            }

            @Override
            protected void updateItem(Part part, boolean empty) {
                super.updateItem(part, empty);
                setGraphic(empty || part == null ? null : addButton);
            }
        });
    }

    @FXML
    private void onSearch() {
        applyFilter();
    }

    private void applyFilter() {
        String query = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase();
        String selectedCategory = categoryFilter.getValue();
        filteredParts.setPredicate(part -> {
            boolean matchesText = query.isEmpty()
                    || part.getPartCode().toLowerCase().contains(query)
                    || part.getName().toLowerCase().contains(query)
                    || part.getBrand().toLowerCase().contains(query);
            boolean matchesCategory = selectedCategory == null
                    || ALL_CATEGORIES.equals(selectedCategory)
                    || part.getCategory().equalsIgnoreCase(selectedCategory);
            return matchesText && matchesCategory;
        });
        updateSummary();
    }

    private void addOneToCart(Part part) {
        if (part == null || part.getQuantity() <= 0) {
            showAlert(Alert.AlertType.WARNING, "Out of Stock", "This part is currently out of stock.");
            return;
        }

        CartItem existing = null;
        for (CartItem item : data.getCartItems()) {
            if (item.getPart() == part) {
                existing = item;
                break;
            }
        }
        int cartQuantity = existing == null ? 0 : existing.getQuantity();
        if (cartQuantity + 1 > part.getQuantity()) {
            showAlert(Alert.AlertType.WARNING, "Insufficient Stock",
                    "All available units of " + part.getName() + " are already in the cart.");
            return;
        }

        if (existing == null) {
            data.getCartItems().add(new CartItem(part, 1));
        } else {
            existing.setQuantity(existing.getQuantity() + 1);
            data.getCartItems().set(data.getCartItems().indexOf(existing), existing);
        }
        updateCartCount();
    }

    private void updateSummary() {
        int quantity = 0;
        double value = 0;
        if (filteredParts != null) {
            for (Part part : filteredParts) {
                quantity += part.getQuantity();
                value += part.getPrice() * part.getQuantity();
            }
        }
        lblTotalParts.setText("Total Parts: " + quantity);
        lblTotalValue.setText(String.format("Inventory Value: Rs. %.2f", value));
    }

    private void updateCartCount() {
        int count = data.getCartItems().stream().mapToInt(CartItem::getQuantity).sum();
        lblCartCount.setText("Cart: " + count);
    }

    @FXML private void onOpenInventory(ActionEvent event) {
        navigateTo(event, "/Fxml/inventory.fxml");
    }

    @FXML private void onOpenDealers(ActionEvent event) {
        navigateTo(event, "/Fxml/dealers.fxml");
    }

    @FXML private void onOpenSales(ActionEvent event) {
        navigateTo(event, "/Fxml/sales.fxml");
    }

    @FXML private void onOpenLowStock(ActionEvent event) {
        navigateTo(event, "/Fxml/low_stock.fxml");
    }

    @FXML private void onOpenAuditLog(ActionEvent event) {
        navigateTo(event, "/Fxml/audit_log.fxml");
    }
}
