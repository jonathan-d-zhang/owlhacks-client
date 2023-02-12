import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.Scanner;
import java.net.http.HttpRequest;
import javax.swing.JOptionPane;

public class ClientSideRequest {
    public static void main(String[] args) throws IOException, InterruptedException, javax.sound.sampled.LineUnavailableException {
        /**
         * Getting audio from the user
         * */
        int x = JOptionPane.showConfirmDialog(null, "Start the recording?");
        while(true){
            record();
            //x = JOptionPane.showConfirmDialog(null, "Do you wish to continue?");
        }

    }
    public static void record() throws javax.sound.sampled.LineUnavailableException, java.lang.InterruptedException, java.io.IOException, java.lang.InterruptedException{

        AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2,4,44100, false);
        DataLine.Info dataInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
        if(!AudioSystem.isLineSupported(dataInfo)){
            System.out.printf("Not Supported");
        }

        TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(dataInfo);

        Thread audioRecordThread = new Thread(){
            @Override public void run(){
                AudioInputStream recordingStream = new AudioInputStream(targetLine);
                File outputFile = new File("record.wav");
                try{
                    AudioSystem.write(recordingStream, AudioFileFormat.Type.WAVE, outputFile);
                }catch(IOException ex){
                    System.out.println(ex);
                }
                System.out.println("Stopped Recording");
            }
        };

        targetLine.open();
        targetLine.start();
        audioRecordThread.start();
        Thread.sleep(5000);
        //JOptionPane.showMessageDialog(null, "Press to stop");
        targetLine.stop();
        targetLine.close();
        sendRequest();
    }

    public static void sendRequest()throws java.io.IOException, java.lang.InterruptedException{
        /**
         * Connect to server 'http://172.104.14.22/open' and make a POST request
         * */
        HttpClient client = HttpClient.newHttpClient();
        //ID - what the last data from the server was
        //Words translated
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("http://172.104.14.22/open"))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        var y = client.send(req, HttpResponse.BodyHandlers.ofString());
        System.out.println(y.body());
    }

}
