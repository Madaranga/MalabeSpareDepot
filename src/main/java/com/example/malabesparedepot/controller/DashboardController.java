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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileNotFoundException;

public class DashboardController extends NavigationController{

    private static final String ALL_CATEGORIES = "All Categories";

    private final ApplicationData data = ApplicationData.getInstance();
    private FilteredList<Part> filteredParts;

    @FXML
    private TextField searchField;
    @FXML private ComboBox<String> categoryFilter;
    @FXML private TableView<Part> inventoryTable;
    @FXML private TableColumn<Part, String> codeColumn;
    @FXML private TableColumn<Part, String> nameColumn;
    @FXML private TableColumn<Part, String> brandColumn;
    @FXML private TableColumn<Part, Double> priceColumn;
    @FXML private TableColumn<Part, Integer> quantityColumn;
    @FXML private TableColumn<Part, String> categoryColumn;
    @FXML private TableColumn<Part, String> imageColumn;
    @FXML private TableColumn<Part, Part> actionColumn;
    @FXML private Label totalValueLabel;
    @FXML private Label totalPartsLabel;
    @FXML private Label cartCountLabel;

    @FXML
    private void initialize() {
        configureColumns();

        categoryFilter.setItems(FXCollections.observableArrayList(ALL_CATEGORIES));
        categoryFilter.getItems().addAll(data.getCategories());
        categoryFilter.getSelectionModel().selectFirst();

        filteredParts = new FilteredList<>(data.getParts(), part -> true);
        SortedList<Part> sortedParts = new SortedList<>(filteredParts);
        sortedParts.comparatorProperty().bind(inventoryTable.comparatorProperty());
        inventoryTable.setItems(sortedParts);

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
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("partCode"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        imageColumn.setCellValueFactory(new PropertyValueFactory<>("imagePath"));
        imageColumn.setCellFactory(column -> new TableCell<>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);
                Image image = null;
                try {
                    image = empty ? null : ImageUtil.loadImage(imagePath);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                imageView.setImage(image);
                imageView.setFitWidth(38);
                imageView.setFitHeight(38);
                imageView.setPreserveRatio(true);
                setGraphic(image == null ? null : imageView);
            }
        });

        actionColumn.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue()));
        actionColumn.setCellFactory(column -> new TableCell<>() {
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
    private void handleSearch() {
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
        totalPartsLabel.setText("Total Parts: " + quantity);
        totalValueLabel.setText(String.format("Inventory Value: Rs. %.2f", value));
    }

    private void updateCartCount() {
        int count = data.getCartItems().stream().mapToInt(CartItem::getQuantity).sum();
        cartCountLabel.setText("Cart: " + count);
    }

    @FXML private void openInventory(ActionEvent event) {
        navigateTo(event, "/fxml/inventory.fxml");
    }

    @FXML private void openDealers(ActionEvent event) {
        navigateTo(event, "/fxml/dealers.fxml");
    }

    @FXML private void openSales(ActionEvent event) {
        navigateTo(event, "/fxml/sales.fxml");
    }

    @FXML private void openLowStock(ActionEvent event) {
        navigateTo(event, "/fxml/lowstock.fxml");
    }

    @FXML private void openAuditLog(ActionEvent event) {
        navigateTo(event, "/fxml/audit.fxml");
    }
}

