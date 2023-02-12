package org.client;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.IOException;

public class KeyController {

    @FXML TextField textField;

    @FXML
    private void submitKey() throws IOException {

        try {
            int key = Integer.parseInt(textField.getText());
            App.loadTranscribing(key);
        } catch (NumberFormatException ignored) {
            // TODO: say error
        }
    }
}
