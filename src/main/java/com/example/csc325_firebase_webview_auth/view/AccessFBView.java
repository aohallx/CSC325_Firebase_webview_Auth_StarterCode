package com.example.csc325_firebase_webview_auth.view;

import com.example.csc325_firebase_webview_auth.model.Person;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.FileInputStream;
import java.io.File;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import javafx.stage.FileChooser;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class AccessFBView {

    @FXML private TextField nameField;
    @FXML private TextField majorField;
    @FXML private TextField ageField;
    @FXML private Button writeButton;
    @FXML private Button readButton;

    // TableView and columns
    @FXML private TableView<Person> tableView;
    @FXML private TableColumn<Person, String> nameCol;
    @FXML private TableColumn<Person, String> majorCol;
    @FXML private TableColumn<Person, Integer> ageCol;

    private final ObservableList<Person> listOfUsers = FXCollections.observableArrayList();
    private Person person;

    @FXML
    public void initialize() {
        // Link columns to Person properties
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        majorCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getMajor()));
        ageCol.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getAge()).asObject());

        // Optionally: load data on start
        // readFirebase();
    }

    public ObservableList<Person> getListOfUsers() {
        return listOfUsers;
    }

    public boolean registerUser() {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail("user@example.com")
                .setEmailVerified(false)
                .setPassword("secretPassword")
                .setPhoneNumber("+11234567890")
                .setDisplayName("John Doe")
                .setDisabled(false);

        try {
            UserRecord userRecord = App.fauth.createUser(request);
            System.out.println("Successfully created new user: " + userRecord.getUid());
            return true;
        } catch (FirebaseAuthException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @FXML
    private void addRecord(ActionEvent event) {
        addData();
    }

    @FXML
    private void readRecord(ActionEvent event) {
        readFirebase();
    }

    @FXML
    private void regRecord(ActionEvent event) {
        registerUser();
    }

    @FXML
    private void handleDelete() {
        listOfUsers.clear();
        tableView.getItems().clear();
        System.out.println("Delete menu clicked");
    }

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("files/WebContainer.fxml");
    }

    public void addData() {
        String ageText = ageField.getText();

        if (nameField.getText().isEmpty() || majorField.getText().isEmpty() || ageText.isEmpty()) {
            showAlert("Please fill out all fields.");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            showAlert("Age must be a number.");
            return;
        }

        DocumentReference docRef = App.fstore.collection("References").document(UUID.randomUUID().toString());
        Map<String, Object> data = new HashMap<>();
        data.put("Name", nameField.getText());
        data.put("Major", majorField.getText());
        data.put("Age", age);
        ApiFuture<WriteResult> result = docRef.set(data);
    }


    public boolean readFirebase() {
        listOfUsers.clear(); // Clear old entries
        ApiFuture<QuerySnapshot> future = App.fstore.collection("References").get();
        try {
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            if (!documents.isEmpty()) {
                for (QueryDocumentSnapshot document : documents) {
                    Person p = new Person(
                            String.valueOf(document.getData().get("Name")),
                            String.valueOf(document.getData().get("Major")),
                            Integer.parseInt(document.getData().get("Age").toString())
                    );
                    listOfUsers.add(p);
                }
                tableView.setItems(listOfUsers); // Populate the TableView
            } else {
                System.out.println("No data found.");
            }
            return true;
        } catch (InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // === Menu Methods ===

    @FXML
    private void goToRegister() throws IOException {
        App.setRoot("files/Register.fxml");
    }

    @FXML
    private void goToSignin() throws IOException {
        App.setRoot("files/Signin.fxml");
    }

    @FXML
    private void handleClose() {
        Platform.exit();
    }

    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Firebase JavaFX App");
        alert.setContentText("CSC 325 Project by Aidan");
        alert.showAndWait();
    }

    @FXML
    private void uploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose an image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                FileInputStream fileInputStream = new FileInputStream(selectedFile);

                Storage storage = StorageOptions.newBuilder()
                        .setCredentials(App.fstore.getOptions().getCredentials())
                        .build()
                        .getService();

                String blobString = "profilePics/" + selectedFile.getName();
                BlobInfo blobInfo = BlobInfo.newBuilder("week10csc325fbjava-a6cf3.firebasestorage.app", blobString)
                        .setContentType("image/png")
                        .build();

                storage.create(blobInfo, fileInputStream);
                showAlert("Upload successful!");

            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Upload failed: " + e.getMessage());
            }
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setContentText(message);
        alert.showAndWait();
    }


}

