package com.example.csc325_firebase_webview_auth.view;

import java.util.Map;
import java.util.HashMap;import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleRegister() {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (email == null || email.isEmpty() || !email.contains("@")) {
            showAlert("Invalid email.");
            return;
        }

        if (password == null || password.length() < 6) {
            showAlert("Password must be at least 6 characters.");
            return;
        }

        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password)
                .setEmailVerified(false)
                .setDisabled(false);

        try {
            UserRecord user = App.fauth.createUser(request);
            System.out.println("Registered: " + user.getUid());

            // 🔥 Save user to Firestore
            Map<String, Object> data = new HashMap<>();
            data.put("Name", ""); // or extract from another input field
            data.put("Major", "");
            data.put("Age", 0);   // or parse from field if you collect age

            App.fstore.collection("References").document(user.getUid()).set(data);

            showAlert("User registered successfully: " + user.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Registration failed: " + e.getMessage());
        }
    }


    @FXML
    private void handleBack() throws Exception {
        App.setRoot("/files/AccessFBView.fxml");
    }

    // === Helper method to show popup alerts ===
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registration");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
