package com.example.malabesparedepot;

import com.example.malabesparedepot.model.CartItem;
import com.example.malabesparedepot.model.Dealer;
import com.example.malabesparedepot.model.Part;
import com.example.malabesparedepot.util.DataParser;
import com.example.malabesparedepot.util.DealerSelector;
import com.example.malabesparedepot.util.LoggerUtil;
import com.example.malabesparedepot.util.PriceCalculator;
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
import java.io.File;



public class MainController {

    private List<Part> masterInventory = new ArrayList<>();

    private final ObservableList<CartItem> cartList = FXCollections.observableArrayList();
    private List<Dealer> allDealers = new ArrayList<>();


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
    @FXML private TableColumn<Part, String> colDashImage;
    @FXML private TableColumn<Part, Part> colDashAction;

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
    @FXML private TableView<CartItem> tblCart;
    @FXML private TableColumn<CartItem, String> colCartName;
    @FXML private TableColumn<CartItem, Integer> colCartQty;
    @FXML private TableColumn<CartItem, Double> colCartSubtotal;
    @FXML private ComboBox<Dealer> cmbDealer;
    @FXML private Label lblTotal;
    @FXML private Button btnCheckout;





    //Initialization
    @FXML
    public void initialize() {
        System.out.println("MainController initialized successfully");
        //Load data
        masterInventory = DataParser.parseInventory("inventory_legacy.txt");
        allDealers = DataParser.parseDealers("dealers_legacy.txt");

        if (masterInventory.isEmpty()) {
            System.err.println("!! Inventory empty or not found !!!");
            return;
        }

        //Extract unique categories
        Set<String> categories = new HashSet<>();

        for (Part part : masterInventory) {
            categories.add(part.getCategory());
        }

        //Populate tab 1 and tab 2 category ComboBoxes
        ObservableList<String> categoryOptions = FXCollections.observableArrayList(categories);
        categoryFilter.setItems(categoryOptions);
        cmbCategory.setItems(categoryOptions);

        categoryFilter.getItems().add(0,"All Categories");
        categoryFilter.getSelectionModel().selectFirst();


        //Populate Cart Dropdown
        populateCartPartDropdown();

        //populate dealer list
        List<Dealer> selectedDealers = DealerSelector.getRandomFoundDealers(allDealers);
        cmbDealer.setItems(FXCollections.observableArrayList(selectedDealers));
        if (!selectedDealers.isEmpty()) {
            cmbDealer.getSelectionModel().selectFirst();
        }

        //Add an "All Categories"
//        categoryFilter.getItems().add(0,"All Categories");
//        categoryFilter.getSelectionModel().selectFirst();


        //Populate Tab 3 Cart Parts ComboBox
//        ObservableList<String> partOptions = FXCollections.observableArrayList(partNamesForCart);
//        cmbCartPart.setItems(partOptions);
//
//        //Populate Dealer sample list for testing
//        cmbDealer.setItems(FXCollections.observableArrayList("Retail Customer", "Authorized Dealer (10% Disc)", "Wholesale Partner (15% Disc)"));
//        cmbCartPart.getSelectionModel().selectFirst();
//
//        System.out.println("Data bindings successfully synchronized to UI elements!!!");


        //Map custom table columns to Part properties
        colDashCode.setCellValueFactory(new PropertyValueFactory<>("partCode"));
        colDashName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colDashBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        colDashPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colDashQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colDashCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colDashImage.setCellValueFactory(new PropertyValueFactory<>("imagePath"));
        colDashImage.setCellFactory(column -> new TableCell<Part, String>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(String imagePath, boolean empty) {
                super.updateItem(imagePath, empty);
                if (empty ||  imagePath == null || imagePath.isEmpty()) {
                    setGraphic(null);
                } else {
                    try {
                        //read directly from Images folder
                        String resourcePath = "/Images/" + imagePath.trim();
                        java.io.InputStream is = getClass().getResourceAsStream(resourcePath);
                        if (is != null) {
                            imageView.setImage(new javafx.scene.image.Image(is));
//                            javafx.scene.image.Image img = new javafx.scene.image.Image(is);
//                            imageView.setImage(img);
//                            imageView.setFitHeight(40);
//                            imageView.setFitWidth(40);
//                            imageView.setPreserveRatio(true);
//                            setGraphic(imageView);
                        } else {
                            java.io.InputStream defaultIs = getClass().getResourceAsStream("/Images/placeholder.png");
                            if (defaultIs != null) {
                                imageView.setImage(new javafx.scene.image.Image(defaultIs));
//                                imageView.setImage(new javafx.scene.image.Image(defaultIs));
//                                imageView.setFitHeight(40);
//                                imageView.setFitWidth(40);
//                                setGraphic(imageView);
                            } else {
                                setGraphic(null);
                            }
                        }
                        imageView.setFitHeight(40);
                        imageView.setFitWidth(40);
                        imageView.setPreserveRatio(true);
                        setGraphic(imageView);;

                    } catch (Exception e) {
                        setGraphic(null);
                    }
                }
            }
        });

        //Custom render for the add to cart button
        colDashAction.setCellValueFactory(param -> new javafx.beans.property.SimpleObjectProperty<>(param.getValue()));
        colDashAction.setCellFactory(column -> new TableCell<Part, Part>() {
            private final Button btnAddToCartInline = new Button("Add to Cart");

            {
                btnAddToCartInline.setOnAction(event -> {
                    Part selectedPart = getTableView().getItems().get(getIndex());
                    if (selectedPart != null) {
                        handleInlineAddToCart(selectedPart);
                    }
                });
            }
            @Override
            protected void updateItem(Part part, boolean empty) {
                super.updateItem(part, empty);
                if (empty || part == null) {
                    setGraphic(null);
                } else {
                    setGraphic(btnAddToCartInline);
                }
            }
        });

        //push data into table on app
        tblDashboard.setItems(FXCollections.observableArrayList(masterInventory));
        updateDashboardSummary(masterInventory);  //calculation labels,refresh UI text

        //setup tab3 active cart columns
        colCartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colCartQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colCartSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        tblCart.setItems(cartList);

        categoryFilter.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            onSearch();
        });

    }





    //populate dropdown in tab 3
    private void populateCartPartDropdown() {
        List<String> partNamesForCart = new ArrayList<>();
        for (Part part : masterInventory) {
            partNamesForCart.add(part.getPartCode() + " - " +part.getName() + " (" + part.getBrand() + ")");
        }
        cmbCartPart.setItems(FXCollections.observableArrayList(partNamesForCart));
        if (!partNamesForCart.isEmpty()) {
            cmbCartPart.getSelectionModel().selectFirst();
        }
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
                showAlert(Alert.AlertType.ERROR,"Duplicate code","Part code" + newPart.getPartCode() + " already exists");
                return;
            }
        }


        masterInventory.add(newPart); //insert into our master inventory list
        tblDashboard.setItems(FXCollections.observableArrayList(masterInventory)); //refresh UI views instantly
        updateDashboardSummary(masterInventory);
        populateCartPartDropdown(); //Refresh dropdown
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
            populateCartPartDropdown();
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
            populateCartPartDropdown();
            clearManageFields();
            System.out.println("Successfully deleted part " + targetCode);
        } else {
            showAlert(Alert.AlertType.ERROR, "Not found","Part code" + targetCode + " not found for delete");
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
        String selectedStr = cmbCartPart.getValue();
        if (selectedStr == null) return;

        String code = selectedStr.split(" - ")[0].trim();
        Part partToCart = findPartByCode(code);

        if (partToCart == null) return;

        int requestQty = 0;
        try {
            requestQty = Integer.parseInt(txtCartQty.getText().trim());
            if (requestQty <= 0) {
                showAlert(Alert.AlertType.ERROR, "Invalid Quantity", "Quantity must be a positive integer.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Quantity","Please enter a valid number.");
        }

        //Validate stock availability
        int existingCartQty = 0;
        CartItem existingItem = null;
        for (CartItem item : cartList) {
            if (item.getPartCode().equalsIgnoreCase(code)) {
                existingCartQty = item.getQuantity();
                existingItem = item;
                break;
            }
        }

        if (partToCart.getQuantity() < (existingCartQty + requestQty)) {
            showAlert(Alert.AlertType.WARNING, "Insufficient Stock", "Only " + partToCart.getQuantity() + " units available. You currently have " + existingCartQty + " units in your cart");
            return;
        }

        //Add or merge into cart list
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + requestQty);
            tblCart.refresh();
        } else {
            cartList.add(new CartItem(partToCart, requestQty));
        }
        updateCartTotal();

    }

    @FXML
    void onCheckout() {
        if (cartList.isEmpty()) {
            showAlert(Alert.AlertType.WARNING,"Empty Cart", "Your Shopping Cart is Empty");
            return;
        }

        Dealer selectedDealer = cmbDealer.getValue();
        if (selectedDealer == null) {
            showAlert(Alert.AlertType.WARNING, "No Dealer selected","Please Select a Dealer");
            return;
        }

        //Complete the transaction
        for (CartItem item : cartList) {
            Part invPart = findPartByCode(item.getPartCode());
            if (invPart != null) {
                int originalQty = invPart.getQuantity();
                invPart.setQuantity(originalQty - item.getQuantity());

                //Log using LoggerUtil
                LoggerUtil.logAction("PURCHASE", item.getPartCode(), item.getQuantity());

            }
        }

        //confirmation
        double finalTotal = PriceCalculator.calculateFinalTotal(cartList);
        showAlert(Alert.AlertType.INFORMATION,"Transaction Approved", String.format("Successfully purchased items for %s.%nFinal paid amound: Rs. %.2f",selectedDealer.getName(), finalTotal));

        //cleanup
        cartList.clear();
        tblDashboard.setItems(FXCollections.observableArrayList(masterInventory));
        updateDashboardSummary(masterInventory);
        updateCartTotal();
        txtCartQty.clear();

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
                showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields must be filled and prices must be positive.");
                return null;
            }

            //Generate standard date string for today
            String defaultDate = java.time.LocalDate.now().toString();

            return new Part(code, name, brand, price, quantity, category, defaultDate, selectedImagePath);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR,"Validation error", "Invalid price or quantity numeric format.");
            return null;
        }
    }


    private void handleInlineAddToCart(Part selectedPart) {
        if (selectedPart.getQuantity() <= 0) {
            showAlert(Alert.AlertType.WARNING, "Out of Stock", selectedPart.getName() + " is currently out of stock");
            return;
        }

        //match the tab 3's ComboBox
        String itemToMatch = selectedPart.getPartCode() + " - " + selectedPart.getName() + " (" + selectedPart.getBrand() + ")";
        cmbCartPart.getSelectionModel().select(itemToMatch);
        txtCartQty.setText("1"); //Default quantity input

        //fire existing tab 3 cart
        onAddToCart();

        System.out.println("Inline add to cart: Sent " + selectedPart.getName() + " to checkout");
    }





    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private Part findPartByCode(String code) {
        for (Part part : masterInventory) {
            if (part.getPartCode().equalsIgnoreCase(code)) {
                return part;
            }
        }
        return null;
    }

    private void updateCartTotal() {
        double total = PriceCalculator.calculateFinalTotal(cartList);
        lblTotal.setText(String.format("Total : Rs. %.2f", total));
    }


}