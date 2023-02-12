package org.client;

import javafx.fxml.FXML;
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
                .GET()
                .uri(URI.create("http://172.104.14.22/open"))
                .build();

        HttpResponse<String> r = client.send(req, HttpResponse.BodyHandlers.ofString());

        App.loadRecording(Integer.parseInt(r.body()));
    }

    @FXML
    private void joinMeet() throws IOException {
        App.setRoot("key");
    }
}
