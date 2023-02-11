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
    public static void main(String[] args) throws IOException, InterruptedException {
        /**
         * Getting audio from the user
         * */
        try {
            AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2,4,44100, false);
            DataLine.Info dataInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
            if(!AudioSystem.isLineSupported(dataInfo)){
                System.out.printf("Not Supported");
            }

            TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(dataInfo);
            targetLine.open();

            JOptionPane.showMessageDialog(null, "Hit Ok to record");
            targetLine.start();

            Thread audioRecordThread = new Thread(){
                @Override public void run(){
                    AudioInputStream recordingStream = new AudioInputStream(targetLine);
                    File outputFile = new File("record.wav");
                    try{
                        AudioSystem.write(recordingStream, AudioFileFormat.Type.WAVE, outputFile);
                        /*int i = 0;
                        while(i<5){
                            AudioSystem.write(recordingStream, AudioFileFormat.Type.WAVE, outputFile);
                            Thread.sleep(5000);
                            i++;
                        }*/
                    }catch(IOException ex){
                        System.out.println(ex);
                    }
                    System.out.println("Stopped Recording");
                }
            };
            /*boolean running = true;
            int i=0;
            while(i<5){
                audioRecordThread.start();
                Thread.sleep(5000);
                targetLine.stop();
                i++;
            }*/
            /*audioRecordThread.start();
            Thread.sleep(5000);
            targetLine.stop();
            targetLine.close();*/
            boolean running = true;
            while(running) {
                fiveSecRecording(audioRecordThread);
                Thread.sleep(5000);
                int cont = userOption();
                if (cont == 1) {
                    fiveSecRecording(audioRecordThread);
                    Thread.sleep(5000);
                    //cont = userOption();
                }else{
                    running = false;
                }
                /*targetLine.start();
                audioRecordThread.start();
                Thread.sleep(5000);
                targetLine.stop();
                targetLine.close();*/
            }
            targetLine.stop();
            targetLine.close();


        }catch (Exception e){
            System.out.println(e);
        }


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
            var x = client.send(req, HttpResponse.BodyHandlers.ofString());
            System.out.println(x.body());



    }
    public static void fiveSecRecording(Thread audioRecordThread){
        audioRecordThread.start();
    }

    public static int userOption(){
        //JOptionPane.showConfirmDialog(null, "Do you want to stop?");
        return JOptionPane.showConfirmDialog(null, "Do you want to stop?");
    }



}