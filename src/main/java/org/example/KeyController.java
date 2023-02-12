package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.net.http.HttpClient;

public class KeyController {
    private HttpClient client;

    @FXML
    TextField textField;
    private void submitKey() {
        if (client == null) {
            client = HttpClient.newBuilder().build();
        }

        System.out.println(textField.getText());
    }
}
