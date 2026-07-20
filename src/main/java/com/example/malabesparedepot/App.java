package com.example.malabesparedepot;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application{

    @Override
    public void start(Stage stage) {
        try {
            //Load fxml file
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/main_view.fxml"));
            Parent root = fxmlLoader.load();

            //Set up window size
            Scene scene = new Scene(root, 850, 600);

            //Attach the stylesheet
            String cssPath = getClass().getResource("/Styles/style.css").toExternalForm();
            scene.getStylesheets().add(cssPath);


            stage.setTitle("Malabe Spare Depot - Management System");
            stage.setScene(scene);
            stage.show();


        } catch (IOException e) {
            System.err.println("Error loading FXML file.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
