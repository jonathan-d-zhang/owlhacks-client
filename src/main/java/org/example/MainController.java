package org.example;

import java.io.IOException;
import javafx.fxml.FXML;

import java.net.http.HttpClient;

public class MainController {
    private HttpClient client;

    @FXML
    private void loadText() {
        if (client == null) {
            client = HttpClient.newBuilder().build();
        }
    }
}
