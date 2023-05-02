module com.example.server {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires java.sql;

    opens com.example.server to javafx.fxml;
    exports com.example.server;
}