package com.example.client;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class AfterRegistrationPatientController implements Initializable {

    @FXML
    private Button button_go_to_personal_account;

    // @FXML
    // private Label label_welcome;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        button_go_to_personal_account.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                SwitchScene.changeScene(actionEvent, "patient-authorization.fxml", "Patient Authorization Page", null);

            }
        });

        button_go_to_personal_account.setOnMouseEntered(event -> {
            button_go_to_personal_account.setStyle("-fx-background-color: #81ffe6;" + "-fx-background-radius: 50;" + "-fx-border-radius: 50;" + "-fx-cursor: hand;" + "-fx-border-width: 1.5;");
        });

        button_go_to_personal_account.setOnMouseExited(event -> {
            button_go_to_personal_account.setStyle("-fx-background-color: #ffffff;" + "-fx-background-radius: 50;" + "-fx-border-radius: 50;" + "-fx-border-width: 1.5;");
        });
    }

    // public void setUserInformation(String phone) {
    //    label_welcome.setText("Welcome " + phone);
    // }
}
