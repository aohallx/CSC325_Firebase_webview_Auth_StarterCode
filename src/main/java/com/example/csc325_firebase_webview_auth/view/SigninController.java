package com.example.csc325_firebase_webview_auth.view;

import java.io.IOException;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SigninController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        try {
            UserRecord user = FirebaseAuth.getInstance().getUserByEmail(email);
            if (user != null) {
                showAlert("Signed in as: " + user.getEmail());

                // ✅ Redirect to main app view
                App.setRoot("files/AccessFBView.fxml");
            }
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
            showAlert("Login failed: " + e.getMessage());
        } catch (IOException ex) {
            ex.printStackTrace();
            showAlert("Could not load main screen.");
        }
    }


    @FXML
    private void handleBack() throws Exception {
        App.setRoot("files/AccessFBView.fxml");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sign In");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
