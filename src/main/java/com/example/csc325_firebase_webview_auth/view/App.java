package com.example.csc325_firebase_webview_auth.view;

import com.example.csc325_firebase_webview_auth.model.FirestoreContext;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 * JavaFX App
 */
public class App extends Application {
    public static Firestore fstore;
    public static FirebaseAuth fauth;
    public static Scene scene;
    private final FirestoreContext contxtFirebase = new FirestoreContext();

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Show splash screen first
        Parent splashRoot = loadFXML("/files/Splash.fxml");
        Scene splashScene = new Scene(splashRoot);
        primaryStage.setScene(splashScene);
        primaryStage.show();

        // After delay, load main app
        new Thread(() -> {
            try {
                Thread.sleep(2500); // 2.5s splash duration

                // Init Firebase
                fstore = contxtFirebase.firebase();
                fauth = FirebaseAuth.getInstance();

                Parent mainRoot = loadFXML("/files/AccessFBView.fxml");
                javafx.application.Platform.runLater(() -> {
                    scene = new Scene(mainRoot);
                    primaryStage.setScene(scene);
                    primaryStage.show();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }


    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml ));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

