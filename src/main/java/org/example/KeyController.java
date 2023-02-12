package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.http.HttpClient;

public class KeyController {
    private HttpClient client = HttpClient.newBuilder().build();

    @FXML TextField textField;

    @FXML
    private void submitKey() throws IOException {
        System.out.println("called submitKey");

        try {
            int key = Integer.parseInt(textField.getText());
            System.out.println("Inputted: " + key);
            App.loadTranscribing(key);
        } catch (NumberFormatException ignored) {}
    }
}
