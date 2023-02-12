package org.client;

import java.util.ArrayList;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;


public class TextViewController {
    @FXML Text lectureText;

    public void init(int key) {
        TextService service = new TextService(key, lectureText);
        //service.words.addListener((ListChangeListener<String>) change -> start = service.getLastValue());
        service.setPeriod(Duration.seconds(3));
        service.start();
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


class TextService extends ScheduledService<Void> {
//    public final ObservableList<String> words = FXCollections.observableList(new LinkedList<>());
    private final int key;
    private final Text lectureText;

    public TextService(int key, Text lectureText) {
        this.key = key;
        this.lectureText = lectureText;
    }

    @Override
    protected Task<Void> createTask() {
        return new FetchTask(this.key, this.lectureText);
    }
}

class FetchTask extends Task<Void> {
    private final int key;
    private final CloseableHttpClient client = HttpClients.createDefault();
    private final Text lectureText;
    private static final Gson gson = new Gson();

    public FetchTask(int key, Text lectureText) {
        this.key = key;
        this.lectureText = lectureText;
    }

    @Override
    protected Void call() throws Exception {
        HttpGet request = new HttpGet("http://172.104.14.22/text/" + this.key);
        var r = client.execute(request, new BasicHttpClientResponseHandler());
        ArrayList<Response> responses = gson.fromJson(r, new TypeToken<ArrayList<Response>>() {}.getType());
        Platform.runLater(() -> lectureText.setText(responses.stream().map(r1 -> r1.word).collect(Collectors.joining(" "))));
        return null;
    }
}