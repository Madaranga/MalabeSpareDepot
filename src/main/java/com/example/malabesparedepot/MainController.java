package com.example.malabesparedepot;

import com.example.malabesparedepot.model.Part;
import com.example.malabesparedepot.util.DataParser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;



public class MainController {

    private List<Part> masterInventory = new ArrayList<>();

    //TAB 1
    @FXML private TextField searchField;
    @FXML private ComboBox<String> categoryFilter;
    @FXML private Button btnSearch;

    @FXML private TableView<Part> tblDashboard;
    @FXML private TableColumn<Part, String> colDashCode;
    @FXML private TableColumn<Part, String> colDashName;
    @FXML private TableColumn<Part, String> colDashBrand;
    @FXML private TableColumn<Part, Double> colDashPrice;
    @FXML private TableColumn<Part, Integer> colDashQty;
    @FXML private TableColumn<Part, String> colDashCategory;

    @FXML private Label lblTotalValue;
    @FXML private Label lblTotalParts;

    //TAB 2
    @FXML private TextField txtPartCode;
    @FXML private TextField txtName;
    @FXML private TextField txtBrand;
    @FXML private TextField txtPrice;
    @FXML private TextField txtQty;
    @FXML private ComboBox<String> cmbCategory;
    @FXML private ImageView imgPreview;
    private String selectedImagePath = "placeholder.png"; //default fallback


    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;

    //TAB 3 - Left
    @FXML private ComboBox<String> cmbCartPart;
    @FXML private TextField txtCartQty;
    @FXML private Button btnAddToCart;
    //TAB 3 - Right
    @FXML private TableView<?> tblCart;
    @FXML private ComboBox<String> cmbDealer;
    @FXML private Label lblTotal;
    @FXML private Button btnCheckout;





    //Initialization
    @FXML
    public void initialize() {
        System.out.println("MainController initialized successfully");
        //Load data
        masterInventory = DataParser.parseInventory("inventory_legacy.txt");
        if (masterInventory.isEmpty()) {
            System.err.println("!! Inventory empty or not found !!!");
            return;
        }

        //Extract unique categories
        Set<String> categories = new HashSet<>();
        List<String> partNamesForCart = new ArrayList<>();

        for (Part part : masterInventory) {
            categories.add(part.getCategory());
            partNamesForCart.add(part.getName() + " - " + part.getName() );
        }

        //Populate tab 1 and tab 2 category ComboBoxes
        ObservableList<String> categoryOptions = FXCollections.observableArrayList(categories);
        categoryFilter.setItems(categoryOptions);
        cmbCategory.setItems(categoryOptions);

        //Add an "All Categories"
        categoryFilter.getItems().add(0,"All Categories");
        categoryFilter.getSelectionModel().selectFirst();


        //Populate Tab 3 Cart Parts ComboBox
        ObservableList<String> partOptions = FXCollections.observableArrayList(partNamesForCart);
        cmbCartPart.setItems(partOptions);

        //Populate Dealer sample list for testing
        cmbDealer.setItems(FXCollections.observableArrayList("Retail Customer", "Authorized Dealer (10% Disc)", "Wholesale Partner (15% Disc)"));
        cmbCartPart.getSelectionModel().selectFirst();

        System.out.println("Data bindings successfully synchronized to UI elements!!!");


        //Map custom table columns to Part properties
        colDashCode.setCellValueFactory(new PropertyValueFactory<>("partCode"));
        colDashName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDashBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colDashPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colDashQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colDashCategory.setCellValueFactory(new PropertyValueFactory<>("category"));

        //push data into table on app
        tblDashboard.setItems(FXCollections.observableArrayList(masterInventory));
        updateDashboardSummary(masterInventory);  //calculation labels,refresh UI text

        categoryFilter.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            onSearch();
        });

    }

    //TAB 1 - Dashboard
    @FXML
    void onSearch() {
        String query = searchField.getText().trim().toLowerCase();
        String selectedCategory = categoryFilter.getValue();

        //Create a new list to collect only matching items
        ObservableList<Part> filteredList = FXCollections.observableArrayList();

        //Loop through master inventory and find matches
        for (Part part : masterInventory) {
            //query is empty OR matches name/brand/code
            boolean matchesSearch = query.isEmpty() ||
                                    part.getName().toLowerCase().contains(query) ||
                                    part.getBrand().toLowerCase().contains(query) ||
                                    part.getPartCode().toLowerCase().contains(query);

            //Matches if category selected
            boolean matchesCategory = selectedCategory == null ||
                                      selectedCategory.equals("All Categories") ||
                                      part.getCategory().equalsIgnoreCase(selectedCategory);
            if (matchesSearch && matchesCategory) {
                filteredList.add(part);
            }
        }






        //Loop through our master inventory list and find matches
        for (Part part : masterInventory) {
            boolean matchesSearch = part.getName().toLowerCase().contains(query) ||
                                    part.getPartCode().toLowerCase().contains(query);

            boolean matchesCategory = selectedCategory == null ||
                                      selectedCategory.equals("All Categories") ||
                                      part.getCategory().equalsIgnoreCase(selectedCategory);


            if (matchesSearch && matchesCategory) {
                System.out.println("Found matches: " + part.getName() + " (" + part.getPartCode() + ")");
            }
        }
        tblDashboard.setItems(filteredList);
        updateDashboardSummary(filteredList);


    }

    private void clearManageFields() {
        txtPartCode.clear();
        txtName.clear();
        txtBrand.clear();
        txtPrice.clear();
        txtQty.clear();
        cmbCategory.getSelectionModel().clearSelection();

        imgPreview.setImage(null);
        selectedImagePath = "placeholder.png";
    }








    //TAB 2 - Manage Items
    @FXML
    void onAddPart() {
        Part newPart = getPartFromFields();
        if (newPart == null) return;

        //check for duplicate part code
        for (Part p : masterInventory) {
            if (p.getPartCode().equalsIgnoreCase(newPart.getPartCode())) {
                System.err.println("Error: Part Code " + newPart.getPartCode() + " already exists");
                return;
            }
        }

        //insert into our master inventory list
        masterInventory.add(newPart);

        //refresh UI views instantly
        tblDashboard.setItems(FXCollections.observableArrayList(masterInventory));
        updateDashboardSummary(masterInventory);

        clearManageFields();
        System.out.println("Successfully added part " + newPart.getName());

    }

    @FXML
    void onUpdateStock() {
        String searchCode = txtPartCode.getText().trim();
        Part updatedData = getPartFromFields();
        if (updatedData == null) return;

        boolean found = false;
        for (int i = 0; i < masterInventory.size(); i++) {
            if (masterInventory.get(i).getPartCode().equalsIgnoreCase(searchCode)) {
                masterInventory.set(i, updatedData);
                found = true;
                break;
            }
        }

        if (found) {
            tblDashboard.setItems(FXCollections.observableArrayList(masterInventory));
            updateDashboardSummary(masterInventory);
            clearManageFields();
            System.out.println("Successfully updated stock part " + searchCode);
        } else  {
            System.err.println("Error: Part code " + searchCode + " not found for update");
        }
    }

    @FXML
    void onDeletePart() {
        String targetCode = txtPartCode.getText().trim();
        if (targetCode.isEmpty()) return;

        boolean removed = masterInventory.removeIf(p -> p.getPartCode().equalsIgnoreCase(targetCode));

        if (removed) {
            tblDashboard.setItems(FXCollections.observableArrayList(masterInventory));
            updateDashboardSummary(masterInventory);
            clearManageFields();
            System.out.println("Successfully deleted part " + targetCode);
        } else {
            System.err.println("Error: Part code " + targetCode + " not found for delete");
        }
    }



    //Image
    @FXML
    void onBrowseImage() {
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        fileChooser.setTitle("Select Part Image");
        fileChooser.getExtensionFilters().addAll(new javafx.stage.FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.jpeg"));

        //Open the file navigation window
        java.io.File file = fileChooser.showOpenDialog(imgPreview.getScene().getWindow());

        if (file != null) {
            selectedImagePath = file.getAbsolutePath();

            //Render it instantly inside the ImageView preview slot
            javafx.scene.image.Image img =  new javafx.scene.image.Image(file.toURI().toString());
            imgPreview.setImage(img);
            System.out.println("Image Selected: " + selectedImagePath);
        }
    }







    //TAB 3 - Checkout & Dealers
    @FXML
    void onAddToCart() {
        System.out.println("Add To Cart Button clicked!");
    }

    @FXML
    void onCheckout() {
        System.out.println("Process Checkout Button clicked!");
    }


    //Calculation - labels
    private void updateDashboardSummary(List<Part> currentList) {
        int totalPartsCount = 0;
        double totalInventoryValue = 0.0;

        for (Part part : currentList) {
            totalPartsCount += part.getQuantity();
            totalInventoryValue += (part.getPrice() * part.getQuantity());
        }

        //refresh the UI text
        lblTotalValue.setText(String.format("Total Inventory Value: %.2f", totalInventoryValue));
        lblTotalParts.setText("Total Parts: " + totalPartsCount);
    }





    //Tab 2
    private Part getPartFromFields() {
        try {
            String code = txtPartCode.getText().trim();
            String name = txtName.getText().trim();
            String brand = txtBrand.getText().trim();
            Double price = Double.parseDouble(txtPrice.getText().trim());
            int quantity = Integer.parseInt(txtQty.getText().trim());
            String category = cmbCategory.getValue();

            if (code.isEmpty() || name.isEmpty() || brand.isEmpty() || price < 0 || quantity < 0 || category == null ) {
                System.err.println("!!!Validation error!!!");
                return null;
            }

            //Generate standard date string for today
            String defaultDate = java.time.LocalDate.now().toString();

            return new Part(code, name, brand, price, quantity, category, defaultDate, selectedImagePath);
        } catch (NumberFormatException e) {
            System.err.println("!!!Validation error: Invalid price or quantity numeric format!!!");
            return null;
        }
    }


}
