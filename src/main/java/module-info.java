module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.google.gson;

    opens org.example to com.google.gson, javafx.fxml;
    exports org.example;
}
