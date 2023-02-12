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
import java.util.LinkedList;

public class RecordingController {
    private final AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2,4,44100, false);
    private final DataLine.Info dataInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
    private int key;
    private final LinkedList<Integer> queue = new LinkedList<>();

    private Task<Void> task = null;

    @FXML
    private void startRecording() {
        task = new Task<>() {
            /**
             * @noinspection BusyWait
             */
            @Override
            protected Void call() throws LineUnavailableException, InterruptedException {
                if (!AudioSystem.isLineSupported(dataInfo)) {
                    System.out.println("Not Supported");
                    // TODO: error here
                }
                int c = 0;
                while (!isCancelled()) {
                    TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(dataInfo);

                    int finalC = c;
                    Thread audioRecordThread = new Thread(() -> {
                        AudioInputStream recordingStream = new AudioInputStream(targetLine);
                        File outputFile = new File("recordings/record" + finalC + ".wav");
                        try {
                            AudioSystem.write(recordingStream, AudioFileFormat.Type.WAVE, outputFile);
                        } catch (IOException ignored) {
                        }
                    });

                    targetLine.open();
                    targetLine.start();
                    audioRecordThread.start();
                    Thread.sleep(5000);
                    System.out.println("Stopping");
                    targetLine.stop();
                    targetLine.close();

                    queue.addLast(c);
                    c++;
                }
                return null;
            }
        };

        var recordThread = new Thread(task);
        var uploadThread = new Thread(new UploadAudio(queue, key));
        uploadThread.setDaemon(true);
        uploadThread.start();
        recordThread.start();
    }

    @FXML
    public void stopRecording() {
        if (task != null) {
            System.out.println("Cancelling task");
            task.cancel();
        }
        // TODO: error or something if already stopped
    }

    public void setKey(int key) {
        System.out.println("Key set to: " + key);
        this.key = key;
    }
}

class UploadAudio extends Task<Void> {
    private final CloseableHttpClient client = HttpClients.createDefault();
    private final LinkedList<Integer> queue;
    private final int key;

    public UploadAudio(LinkedList<Integer> queue, int key) {
        this.queue = queue;
        this.key = key;
    }

    @Override
    protected Void call() throws InterruptedException, IOException {
        Integer x;
        while (true) {
            System.out.println("hi from daemon " + queue);
            if ((x = queue.pollFirst()) != null) {
                File f = new File("recordings/record" + x + ".wav");
                final var e = MultipartEntityBuilder.create()
                        .addPart("file", new FileBody(f))
                        .build();
                var p = new HttpPost("http://172.104.14.22/stt/" + key);
                p.setEntity(e);
                client.execute(p, new BasicHttpClientResponseHandler());

                f.delete();
            }
            Thread.sleep(1000);
        }
    }
}
