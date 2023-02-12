package org.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("startPage"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    static void loadRecording(int key) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("recording.fxml"));
        RecordingController rc = fxmlLoader.getController();
        rc.setKey(key);
        scene.setRoot(fxmlLoader.load());
    }

    static void loadTranscribing(int key) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("textview.fxml"));
        TextViewController tc = fxmlLoader.getController();
        tc.setKey(key);
        scene.setRoot(fxmlLoader.load());
    }

    public static void main(String[] args) {
        launch();
    }

}