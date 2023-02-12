package org.example;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javax.sound.sampled.*;

import java.io.File;
import java.io.IOException;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;

public class RecordingController {
    final CloseableHttpClient client = HttpClients.createDefault();

    @FXML
    private void startRecording() throws LineUnavailableException, InterruptedException {
        AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2,4,44100, false);
        DataLine.Info dataInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
        if(!AudioSystem.isLineSupported(dataInfo)){
            System.out.println("Not Supported");
        }

        TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(dataInfo);

        var task = new Task<Void>() {
            @Override protected Void call() throws IOException, InterruptedException {
                AudioInputStream recordingStream = new AudioInputStream(targetLine);
                File outputFile = new File("record.wav");
                try {
                    AudioSystem.write(recordingStream, AudioFileFormat.Type.WAVE, outputFile);
                } catch(IOException ignored) {}

                Thread.sleep(1000);

                HttpPost p = new HttpPost("http://172.104.14.22/abcd");

                final var e = MultipartEntityBuilder.create()
                        .addPart("file", new FileBody(outputFile))
                        .build();
                p.setEntity(e);
                var r = client.execute(p, new BasicHttpClientResponseHandler());
                System.out.println(r);

                return null;
            }
        };

        var thread = new Thread(task);
        thread.start();
        thread.join();
    }

    @FXML
    public void stopRecording() throws IOException {

    }
}
