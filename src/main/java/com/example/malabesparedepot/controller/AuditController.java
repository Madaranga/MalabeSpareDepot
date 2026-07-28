package com.example.malabesparedepot.controller;

import com.example.malabesparedepot.util.LoggerUtil;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class AuditController extends NavigationController {

    @FXML private ListView<String> lstAuditLog;
    @FXML private Label lblStatus;

    @FXML
    private void initialize() {
        loadAuditEntries();
    }

    @FXML
    private void onRefresh() {
        loadAuditEntries();
    }

    private void loadAuditEntries() {
        Path logPath = LoggerUtil.getLogPath();
        if (!Files.exists(logPath)) {
            lstAuditLog.setItems(FXCollections.observableArrayList());
            lstAuditLog.setPlaceholder(new Label("No audit log has been created yet."));
            lblStatus.setText("Audit file not found: " + logPath.toAbsolutePath());
            return;
        }

        try {
            List<String> entries = Files.readAllLines(logPath);
            lstAuditLog.setItems(FXCollections.observableArrayList(entries));
            lstAuditLog.setPlaceholder(new Label("The audit log is empty."));
            lblStatus.setText(entries.size() + " audit entries loaded.");
            if (!entries.isEmpty()) {
                lstAuditLog.scrollTo(entries.size() - 1);
            }
        } catch (IOException exception) {
            lstAuditLog.setItems(FXCollections.observableArrayList());
            lblStatus.setText("Unable to read audit log: " + exception.getMessage());
            System.err.println("Unable to read " + logPath.toAbsolutePath() + ": "
                    + exception.getMessage());
        }
    }

    @FXML
    protected void onBackToDashboard(ActionEvent event) {
        super.onBackToDashboard(event);
    }
}
