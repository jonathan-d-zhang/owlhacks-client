import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.Scanner;
import java.net.http.HttpRequest;

public class ClientSideRequest {
    public static void main(String[] args) throws IOException, InterruptedException {
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


            int i = 0;
            while(i<20){
                HttpRequest req = HttpRequest.newBuilder()
                        .uri(URI.create("http://172.104.14.22/open"))
                        .POST(HttpRequest.BodyPublishers.noBody())
                        .build();
                var x = client.send(req, HttpResponse.BodyHandlers.ofString());
                System.out.println(x.body());
                i++;
            }
    }

}