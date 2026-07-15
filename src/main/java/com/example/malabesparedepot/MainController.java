package com.example.malabesparedepot;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController {

    @FXML private TextField searchField;
    @FXML private ComboBox<String> categoryFilter;
    @FXML private Button btnSearch;

    @FXML private TextField txtPartCode;
    @FXML private TextField txtName;
    @FXML private TextField txtBrand;
    @FXML private TextField txtPrice;
    @FXML private TextField txtQty;
    @FXML private ComboBox<String> cmbCategory;


    @FXML private Button btnAdd;
    @FXML private Button btnUpdate;
    @FXML private Button btnDelete;

    @FXML
    public void initialize() {
        System.out.println("Main Controller initialized successfully");
    }

    @FXML
    void onSearch() {
        System.out.println("Search Button clicked!");
    }

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

}
