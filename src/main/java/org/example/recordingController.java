package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.http.HttpClient;
public class recordingController {
    @FXML Button startBut;
    @FXML
    Button stopBut;

    boolean isRecording = false;

    private void startRecording() throws IOException {
        isRecording = true;
    }
    public void stopRecording() throws IOException {
        isRecording = false;
    }

}
