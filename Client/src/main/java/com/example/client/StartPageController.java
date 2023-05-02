package com.example.client;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StartPageController implements Initializable {

    @FXML
    private Button button_patient;
    @FXML
    private Button button_doctor;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // PATIENT
        button_patient.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("patient-authorization.fxml"));

                    Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                    Stage stage = new Stage();
                    stage.setTitle("Patient Authorization Page");
                    stage.setResizable(false);
                    stage.setScene(scene);
                    stage.show();

                    // Hide this current window (if this is what you want)
                    ((Node)(event.getSource())).getScene().getWindow().hide();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });

        button_patient.setOnMouseEntered(event -> {
            button_patient.setStyle("-fx-background-color: #81ffe6;" + "-fx-background-radius: 50;" + "-fx-border-radius: 50;" + "-fx-cursor: hand;" + "-fx-border-width: 1.5;");
        });

        button_patient.setOnMouseExited(event -> {
            button_patient.setStyle("-fx-background-color: #6debd2;" + "-fx-background-radius: 50;" + "-fx-border-radius: 50;" + "-fx-border-width: 1.5;");
        });

        // DOCTOR
        button_doctor.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("doctor-authorization.fxml"));

                    Scene scene = new Scene(fxmlLoader.load(), 600, 400);
                    Stage stage = new Stage();
                    stage.setTitle("Doctor Authorization Page");
                    stage.setResizable(false);
                    stage.setScene(scene);
                    stage.show();

                    // Hide this current window (if this is what you want)
                    ((Node)(event.getSource())).getScene().getWindow().hide();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        button_doctor.setOnMouseEntered(event -> {
            button_doctor.setStyle("-fx-background-color: #9cf9ff;" + "-fx-background-radius: 50;" + "-fx-border-radius: 50;" + "-fx-cursor: hand;" + "-fx-border-width: 1.5;");
        });

        button_doctor.setOnMouseExited(event -> {
            button_doctor.setStyle("-fx-background-color: #88e5ff;" + "-fx-background-radius: 50;" + "-fx-border-radius: 50;" + "-fx-border-width: 1.5;");
        });
    }
}
