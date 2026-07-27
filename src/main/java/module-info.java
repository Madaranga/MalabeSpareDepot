module com.example.malabesparedepot {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.malabesparedepot to javafx.fxml;
    opens com.example.malabesparedepot.model to javafx.base;
    exports com.example.malabesparedepot;
    exports com.example.malabesparedepot.model;
}