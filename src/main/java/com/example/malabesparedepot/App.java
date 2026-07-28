package com.example.malabesparedepot;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class App extends Application {

    private static final String DASHBOARD_FXML = "/Fxml/dashboard.fxml";
    private static final String STYLESHEET = "/Styles/style.css";

    @Override
    public void start(Stage stage) throws IOException {
        URL dashboard = Objects.requireNonNull(
                App.class.getResource(DASHBOARD_FXML),
                "Missing required FXML resource: " + DASHBOARD_FXML
        );
        Parent root = new FXMLLoader(dashboard).load();

        Scene scene = new Scene(root, 1100, 720);
        URL stylesheet = Objects.requireNonNull(
                App.class.getResource(STYLESHEET),
                "Missing required stylesheet: " + STYLESHEET
        );
        scene.getStylesheets().add(stylesheet.toExternalForm());

        stage.setTitle("Malabe Spare Depot - Management System");
        stage.setMinWidth(900);
        stage.setMinHeight(620);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
