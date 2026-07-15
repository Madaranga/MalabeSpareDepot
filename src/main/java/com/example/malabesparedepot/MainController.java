package com.example.malabesparedepot;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController {

    @FXML
    public void initialize() {
        System.out.println("MainController initialized successfully");
    }

    @FXML
    void onSearch() {
        System.out.println("Search button clicked!");
    }

}
