package com.example.client;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

// Личный кабинет -> запись на приём -> выбрать специализацию ->

public class PersonalAccountController implements Initializable {

    @FXML
    private Button button_logout;

    @FXML
    private Label label_welcome;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        button_logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                SwitchScene.changeScene(actionEvent, "start-page.fxml", "Authorization Page", null);

            }
        });
    }

    public void setUserInformation(String phone) {
        label_welcome.setText("Welcome " + phone);
    }
}
