package com.example.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

// Этот класс будет обрабатывать всё, что связано с подключением к базе данных
// Служебный класс, который отвечает за коммуникацию с базой данных (авторизацию и регистрацию)

public class SwitchScene {
    public static void changeScene(ActionEvent event, String fxmlFile, String title, String phone) {
        // сцена, которую мы хотим отобразить пользователю при переключении
        // сцена, которую мы будем наполнять
        Parent root = null;

        if (phone != null) {
            try {
                FXMLLoader loader = new FXMLLoader(SwitchScene.class.getResource(fxmlFile));
                root = loader.load();
                PersonalAccountController personalAccountController = loader.getController();
                personalAccountController.setUserInformation(phone);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                root = FXMLLoader.load(SwitchScene.class.getResource(fxmlFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.centerOnScreen();
        stage.show();
        System.out.println("");
    }
}
