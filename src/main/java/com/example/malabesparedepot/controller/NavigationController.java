package com.example.malabesparedepot.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public abstract class NavigationController {

    protected static final String DASHBOARD_FXML = "/fxml/dashboard.fxml";

    protected void navigateTo(ActionEvent event, String fxmlFile) {
        URL resource = getClass().getResource(fxmlFile);
        if (resource == null) {
            showNavigationError(fxmlFile, new IOException("Resource does not exist"));
            return;
        }

        try {
            Parent root = new FXMLLoader(resource).load();
            Node source = (Node) event.getSource();
            Scene scene = source.getScene();
            Stage stage = (Stage) scene.getWindow();
            scene.setRoot(root);
            stage.sizeToScene();
            if (stage.getWidth() < stage.getMinWidth()) {
                stage.setWidth(stage.getMinWidth());
            }
            if (stage.getHeight() < stage.getMinHeight()) {
                stage.setHeight(stage.getMinHeight());
            }
        } catch (IOException | ClassCastException exception) {
            showNavigationError(fxmlFile, exception);
        }
    }

    protected void handleBackToDashboard(ActionEvent event) {
        navigateTo(event, DASHBOARD_FXML);
    }

    protected void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showNavigationError(String fxmlFile, Exception exception) {
        String message = "Unable to load page " + fxmlFile + ". " + exception.getMessage();
        System.err.println(message);
        showAlert(Alert.AlertType.ERROR, "Navigation Error", message);
    }
}

