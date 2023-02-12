package org.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class StartController {
    private final HttpClient client = HttpClient.newBuilder().build();

    @FXML
    private void startMeet() throws IOException, InterruptedException {
        var req = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.noBody())
                .uri(URI.create("http://172.104.14.22/open"))
                .build();

        HttpResponse<String> r = client.send(req, HttpResponse.BodyHandlers.ofString());

        App.setRoot(loadRecording(Integer.parseInt(r.body())));
    }

    @FXML
    private void joinMeet() throws IOException {
        App.setRoot("key");
    }

    Parent loadRecording(int key) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("recording.fxml"));
        Parent root = fxmlLoader.load();
        System.out.println(fxmlLoader.getLocation());
        RecordingController rc = fxmlLoader.getController();
        rc.setKey(key);
        return root;
    }
}
