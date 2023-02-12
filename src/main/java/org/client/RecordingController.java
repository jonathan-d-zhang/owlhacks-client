package org.client;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class RecordingController {
    private final CloseableHttpClient client = HttpClients.createDefault();
    private final AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2,4,44100, false);
    private final DataLine.Info dataInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
    private int key = 3419844;

    @FXML
    private void startRecording() {
        // spawn a thread that spawns threads. alternate between numbers 1 and 2
        var task = new Task<Void>() {
            @Override protected Void call() throws LineUnavailableException, InterruptedException, IOException {
                if(!AudioSystem.isLineSupported(dataInfo)){
                    System.out.println("Not Supported");
                    // TODO: error here
                }

                while (!isCancelled()) {
                    if(!AudioSystem.isLineSupported(dataInfo)){
                        System.out.println("Not Supported");
                    }

                    TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(dataInfo);

                    Thread audioRecordThread = new Thread(() -> {
                        AudioInputStream recordingStream = new AudioInputStream(targetLine);
                        File outputFile = new File("record.wav");
                        try{
                            AudioSystem.write(recordingStream, AudioFileFormat.Type.WAVE, outputFile);
                        } catch(IOException ignored){
                        }
                    });

                    targetLine.open();
                    targetLine.start();
                    audioRecordThread.start();
                    Thread.sleep(5000);
                    System.out.println("Stopping");
                    targetLine.stop();
                    targetLine.close();

                    HttpPost p = new HttpPost("http://172.104.14.22/stt/" + key);

                    final var e = MultipartEntityBuilder.create()
                            .addPart("file", new FileBody(new File("record.wav")))
                            .build();
                    p.setEntity(e);
                    client.execute(p, new BasicHttpClientResponseHandler());
                    // TODO: async
                }
                return null;
            }
        };

        var thread = new Thread(task);
        thread.start();
    }

    @FXML
    public void stopRecording() {

    }

    public void setKey(int key) {
        this.key = key;
    }
}
