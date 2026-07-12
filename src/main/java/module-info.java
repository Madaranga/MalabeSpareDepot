module com.example.malabesparedepot {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.malabesparedepot to javafx.fxml;
    exports com.example.malabesparedepot;
}