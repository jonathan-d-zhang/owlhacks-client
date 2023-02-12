module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.google.gson;
    requires java.desktop;
    requires org.apache.httpcomponents.client5.httpclient5.fluent;
    requires org.apache.httpcomponents.client5.httpclient5;
    requires org.apache.httpcomponents.core5.httpcore5;

    opens org.example to com.google.gson, javafx.fxml;
    exports org.example;
}
