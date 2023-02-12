package org.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;

import java.io.IOException;

public class KeyController {

    @FXML TextField textField;

    @FXML
    private void submitKey() throws IOException {
        try {
            int key = Integer.parseInt(textField.getText());
            App.setRoot(loadTranscribing(key));
        } catch (NumberFormatException ignored) {
            // TODO: say error
        }
    }

    Parent loadTranscribing(int key) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("textview.fxml"));
        Parent root = fxmlLoader.load();
        TextViewController tc = fxmlLoader.getController();
        tc.init(key);
        return root;
    }
}
