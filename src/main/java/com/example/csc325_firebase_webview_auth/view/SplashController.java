package com.example.csc325_firebase_webview_auth.view;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class SplashController {

    @FXML
    private ImageView splashImage;

    @FXML
    public void initialize() {
        // Load splash image
        splashImage.setImage(new Image(getClass().getResource("/files/interstellar3.jpg").toExternalForm()));

        // Apply fade effect
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), splashImage);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.setCycleCount(1);
        fadeIn.play();
    }
}
