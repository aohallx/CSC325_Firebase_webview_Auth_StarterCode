package com.example.csc325_firebase_webview_auth.view;

import com.example.csc325_firebase_webview_auth.model.FirestoreContext;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    public static Firestore fstore;
    public static FirebaseAuth fauth;
    public static Scene scene;

    private final FirestoreContext contxtFirebase = new FirestoreContext();

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load splash screen
        Parent splashRoot = loadFXML("files/splash.fxml");
        Scene splashScene = new Scene(splashRoot);

        // Apply global stylesheet to splash screen
        splashScene.getStylesheets().add(App.class.getResource("/files/style.css").toExternalForm());

        primaryStage.setScene(splashScene);
        primaryStage.show();

        // Load Firebase and switch to main scene in background
        new Thread(() -> {
            try {
                Thread.sleep(2500); // Delay for splash

                fstore = contxtFirebase.firebase();
                fauth = FirebaseAuth.getInstance();

                Parent mainRoot = loadFXML("files/AccessFBView.fxml");

                Platform.runLater(() -> {
                    scene = new Scene(mainRoot);
                    scene.getStylesheets().add(App.class.getResource("/files/style.css").toExternalForm());
                    primaryStage.setScene(scene);
                    primaryStage.show();
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void setRoot(String fxml) throws IOException {
        Parent root = loadFXML(fxml);
        scene.setRoot(root);
        scene.getStylesheets().clear();
        scene.getStylesheets().add(App.class.getResource("/files/style.css").toExternalForm());
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/" + fxml));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
