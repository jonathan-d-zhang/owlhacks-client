package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;

import java.net.URL;
import java.util.ResourceBundle;

public class textPrefsController implements Initializable{

    @FXML
    private ChoiceBox<String> fontChoiceBox;

    private String[] fonts = {"Helvetica","Arial","Georgia"};

    @FXML
    private ChoiceBox<String> fontSizeChoiceBox;
    private String[] fontSize = {"12","14","16","18","20","22","24","26","28","30","32","34","36"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fontChoiceBox.getItems().addAll(fonts);
        fontSizeChoiceBox.getItems().addAll(fontSize);
    }
    public void getFont(ActionEvent event) {
        String font = fontChoiceBox.getValue();
        int fontSize = Integer.parseInt(fontSizeChoiceBox.getValue());
    }
}
