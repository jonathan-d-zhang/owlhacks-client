package org.example;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.concurrent.Task;
import javafx.fxml.FXML;


public class PrimaryController {
    private final HttpClient client = HttpClient.newBuilder().build();
    private int key;
    private String start = "-";
    private static final Gson gson = new Gson();

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    private void spawnThread() throws InterruptedException {
        System.out.println("clicked");


        var task = new Task<Void>() {
            @Override protected Void call() throws IOException, InterruptedException {
                while (!isCancelled()) {
                    var req = HttpRequest.newBuilder()
                            .GET()
                            .uri(URI.create("http://172.104.14.22/text/" + key + "?&start=" + start))
                            .build();

                    HttpResponse<String> r = client.send(req, HttpResponse.BodyHandlers.ofString());
                    ArrayList<Response> responses = gson.fromJson(r.body(), new TypeToken<ArrayList<Response>>(){}.getType());
                    start = responses.get(responses.size() - 1).start;
                    Thread.sleep(3000);
                }
             return null;
            }
        };

        var thread = new Thread(task);
        thread.start();
        thread.join();
    }

    public void setKey(int key) {
        this.key = key;
    }
}

class Response {
    public String start;
    public String word;

    Response() {}

    @Override
    public String toString() {
        return "Response(start=" + start + ", word=" + word + ")";
    }
}
