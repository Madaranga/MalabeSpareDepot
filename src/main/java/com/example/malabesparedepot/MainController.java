package com.example.malabesparedepot;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TableView;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController {
    //TAB 1
    @FXML private TextField searchField;
    @FXML private ComboBox<String> categoryFilter;
    @FXML private Button btnSearch;

    //TAB 2
    @FXML private TextField txtPartCode;
    @FXML private TextField txtName;
    @FXML private TextField txtBrand;
    @FXML private TextField txtPrice;
    @FXML private TextField txtQty;
    @FXML private ComboBox<String> cmbCategory;


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
        System.out.println("Main Controller initialized successfully");
    }

    //TAB 1 - Dashboard
    @FXML
    void onSearch() {
        System.out.println("Search Button clicked!");
    }



    //TAB 2 - Manage Items
    @FXML
    void onAddPart() {
        System.out.println("Add Part Button clicked!");
    }

    @FXML
    void onUpdateStock() {
        System.out.println("Update Stock Button clicked!");
    }

    @FXML
    void onDeletePart() {
        System.out.println("Delete Part Button clicked!");
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

}
