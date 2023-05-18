package com.example.client;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class PatientProfileController implements Initializable {

    @FXML
    private Button button_logout;
    @FXML
    private Button button_schedule_an_appointment;

    @FXML
    private Label label_welcome;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // ВЫХОД ИЗ ЛИЧНОГО КАБИНЕТА
        button_logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                SwitchScene.changeScene(actionEvent, "start-page.fxml", "Authorization Page", null);

            }
        });

        // ЗАПИСЬ НА ПРИЁМ
        button_schedule_an_appointment.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                SwitchScene.changeScene(actionEvent, "make-appointment.fxml", "Make an Appointment", null);
            }
        });
    }

    public void setUserInformation(String phone) {
        label_welcome.setText("Welcome, " + phone);
    }
}
