module IoT {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fazecast.jSerialComm;

    opens view to javafx.fxml;
    exports view;

    opens application to javafx.graphics;
    exports application;
}
