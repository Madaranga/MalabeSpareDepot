package com.example.malabesparedepot.controller;

import com.example.malabesparedepot.data.ApplicationData;
import com.example.malabesparedepot.model.Part;
import com.example.malabesparedepot.util.ImageUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.LocalDate;

public class InventoryController extends NavigationController {

    private final ApplicationData data = ApplicationData.getInstance();
    private Part editingPart;
    private String selectedImagePath = "";

    @FXML private TextField txtUpdateSearchCode;
    @FXML private TextField txtPartCode;
    @FXML private TextField txtName;
    @FXML private TextField txtBrand;
    @FXML private TextField txtPrice;
    @FXML private TextField txtQty;
    @FXML private ComboBox<String> cmbCategory;
    @FXML private ImageView imgPreview;
    @FXML private Label lblStatus;
    @FXML private TableView<Part> tblInventory;
    @FXML private TableColumn<Part, String> colPartCode;
    @FXML private TableColumn<Part, String> colPartName;
    @FXML private TableColumn<Part, String> colPartBrand;
    @FXML private TableColumn<Part, Double> colPartPrice;
    @FXML private TableColumn<Part, Integer> colPartQty;
    @FXML private TableColumn<Part, String> colPartCategory;

    @FXML
    private void initialize() {
        colPartCode.setCellValueFactory(new PropertyValueFactory<>("partCode"));
        colPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colPartBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colPartPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colPartQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPartCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        tblInventory.setItems(data.getParts());
        cmbCategory.setItems(data.getCategories());
        tblInventory.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, selected) -> populateForm(selected)
        );
    }

    @FXML
    private void onSearchItemForUpdate() {
        Part part = data.findPartByCode(txtUpdateSearchCode.getText());
        if (part == null) {
            showAlert(Alert.AlertType.WARNING, "Item Not Found",
                    "No item exists with part code: " + txtUpdateSearchCode.getText().trim());
            return;
        }
        tblInventory.getSelectionModel().select(part);
        tblInventory.scrollTo(part);
        populateForm(part);
    }

    @FXML
    private void onAddPart() {
        Part candidate = readForm();
        if (candidate == null) {
            return;
        }
        if (data.findPartByCode(candidate.getPartCode()) != null) {
            showAlert(Alert.AlertType.ERROR, "Duplicate Code",
                    "Part code " + candidate.getPartCode() + " already exists.");
            return;
        }

        data.getParts().add(candidate);
        refreshCategories();
        setStatus("Added " + candidate.getName() + " successfully.");
        clearForm();
    }

    @FXML
    private void onUpdateStock() {
        if (editingPart == null) {
            showAlert(Alert.AlertType.WARNING, "No Part Selected",
                    "Select a part from the table or search by part code first.");
            return;
        }
        Part candidate = readForm();
        if (candidate == null) {
            return;
        }
        Part duplicate = data.findPartByCode(candidate.getPartCode());
        if (duplicate != null && duplicate != editingPart) {
            showAlert(Alert.AlertType.ERROR, "Duplicate Code",
                    "Part code " + candidate.getPartCode() + " already exists.");
            return;
        }

        editingPart.setPartCode(candidate.getPartCode());
        editingPart.setName(candidate.getName());
        editingPart.setBrand(candidate.getBrand());
        editingPart.setPrice(candidate.getPrice());
        editingPart.setQuantity(candidate.getQuantity());
        editingPart.setCategory(candidate.getCategory());
        editingPart.setDateAdded(candidate.getDateAdded());
        editingPart.setImagePath(candidate.getImagePath());
        data.touchPart(editingPart);
        tblInventory.refresh();
        refreshCategories();
        setStatus("Updated " + editingPart.getName() + " successfully.");
        clearForm();
    }

    @FXML
    private void onDeletePart() {
        if (editingPart == null) {
            showAlert(Alert.AlertType.WARNING, "No Part Selected",
                    "Select the part that you want to delete.");
            return;
        }
        String partName = editingPart.getName();
        data.removePart(editingPart);
        refreshCategories();
        clearForm();
        setStatus("Deleted " + partName + ".");
    }

    @FXML
    private void onClearForm() {
        clearForm();
        setStatus("Form cleared.");
    }

    @FXML
    private void onBrowseImage() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select Part Image");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File selected = chooser.showOpenDialog(imgPreview.getScene().getWindow());
        if (selected != null) {
            selectedImagePath = selected.getAbsolutePath();
            imgPreview.setImage(ImageUtil.loadImage(selectedImagePath));
            setStatus("Selected image: " + selected.getName());
        }
    }

    private Part readForm() {
        String code = txtPartCode.getText().trim();
        String name = txtName.getText().trim();
        String brand = txtBrand.getText().trim();
        String priceText = txtPrice.getText().trim();
        String quantityText = txtQty.getText().trim();
        String category = cmbCategory.getValue();

        if (code.isEmpty() || name.isEmpty() || brand.isEmpty()
                || priceText.isEmpty() || quantityText.isEmpty() || category == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Complete every part field.");
            return null;
        }

        try {
            double price = Double.parseDouble(priceText);
            int quantity = Integer.parseInt(quantityText);
            if (price < 0 || quantity < 0) {
                showAlert(Alert.AlertType.ERROR, "Validation Error",
                        "Price and quantity cannot be negative.");
                return null;
            }
            return new Part(code, name, brand, price, quantity, category,
                    LocalDate.now().toString(), selectedImagePath);
        } catch (NumberFormatException exception) {
            showAlert(Alert.AlertType.ERROR, "Validation Error",
                    "Price must be a number and quantity must be a whole number.");
            return null;
        }
    }

    private void populateForm(Part part) {
        if (part == null) {
            return;
        }
        editingPart = part;
        txtUpdateSearchCode.setText(part.getPartCode());
        txtPartCode.setText(part.getPartCode());
        txtName.setText(part.getName());
        txtBrand.setText(part.getBrand());
        txtPrice.setText(String.format("%.2f", part.getPrice()));
        txtQty.setText(Integer.toString(part.getQuantity()));
        cmbCategory.getSelectionModel().select(part.getCategory());
        selectedImagePath = part.getImagePath();
        imgPreview.setImage(ImageUtil.loadImage(selectedImagePath));
        setStatus("Editing " + part.getName() + ".");
    }

    private void clearForm() {
        editingPart = null;
        txtUpdateSearchCode.clear();
        txtPartCode.clear();
        txtName.clear();
        txtBrand.clear();
        txtPrice.clear();
        txtQty.clear();
        cmbCategory.getSelectionModel().clearSelection();
        selectedImagePath = "";
        imgPreview.setImage(null);
        tblInventory.getSelectionModel().clearSelection();
    }

    private void refreshCategories() {
        String selected = cmbCategory.getValue();
        cmbCategory.setItems(data.getCategories());
        cmbCategory.getSelectionModel().select(selected);
    }

    private void setStatus(String message) {
        lblStatus.setText(message);
    }

    @FXML
    protected void onBackToDashboard(ActionEvent event) {
        super.onBackToDashboard(event);
    }
}
