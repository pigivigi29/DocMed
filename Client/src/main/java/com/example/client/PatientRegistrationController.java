package com.example.client;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

// Контроллер для регистрации пользователей
public class PatientRegistrationController implements Initializable {

    @FXML
    private Button button_sign_up;
    @FXML
    private Button button_sign_in;
    @FXML
    private Button button_back;

    @FXML
    private TextField tf_phone;
    @FXML
    private TextField tf_password;
    @FXML
    private TextField tf_surname;
    @FXML
    private TextField tf_name;
    @FXML
    private TextField tf_patronymic;
    @FXML
    private TextField tf_birthday;
    @FXML
    private TextField tf_email;

    private ActionEvent actionEvent;

    private ApplicationClient client;

    public ActionEvent getActionEvent() {
        return actionEvent;
    }

    public void setActionEvent(ActionEvent actionEvent) {
        this.actionEvent = actionEvent;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        tf_surname.setFocusTraversable(false);
        tf_name.setFocusTraversable(false);
        tf_patronymic.setFocusTraversable(false);
        tf_birthday.setFocusTraversable(false);
        tf_phone.setFocusTraversable(false);
        tf_password.setFocusTraversable(false);
        tf_email.setFocusTraversable(false);

        // SIGN UP
        button_sign_up.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                try {
                    client = new ApplicationClient(new Socket("localhost", 1234));
                    System.out.println("PatientRegistrationController: Connected to server.");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String action = "SIGN-UP-PATIENT";
                String phone = tf_phone.getText();
                String password = tf_password.getText();
                String surname = tf_surname.getText();
                String name = tf_name.getText();
                String patronymic = tf_patronymic.getText();
                String birthday = tf_birthday.getText();
                String email = tf_email.getText();
                String request = action + "   " + phone + "   " + password + "   " + surname + "   " + name + "   " + patronymic + "   " + email + "   " + birthday;

                if (phone.isEmpty() || password.isEmpty() || surname.isEmpty() || name.isEmpty() || patronymic.isEmpty() || email.isEmpty() || birthday.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Пожалуйста, заполните все поля.");
                    alert.show();
                } else {
                    setActionEvent(actionEvent);
                    client.sendRequest(request);
                    client.getResponse(getActionEvent());
                }
            }
        });

        button_sign_up.setOnMouseEntered(event -> {
            button_sign_up.setStyle("-fx-background-color:  #81ffe6;" + "-fx-background-radius: 50;" + "-fx-border-radius: 50;" + "-fx-cursor: hand;" + "-fx-border-width: 1.5;");
        });

        button_sign_up.setOnMouseExited(event -> {
            button_sign_up.setStyle("-fx-background-color: #6debd2;" + "-fx-background-radius: 50;" + "-fx-border-radius: 50;" + "-fx-border-width: 1.5;");
        });

        // SIGN IN
        button_sign_in.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                SwitchScene.changeScene(actionEvent, "patient-authorization.fxml", "Patient Authorization Page", null);
            }
        });

        button_sign_in.setOnMouseEntered(event -> {
            button_sign_in.setStyle("-fx-background-color:  #ffffff;" + "-fx-border-color: #405b7d;" + "-fx-background-radius: 50;" + "-fx-border-radius: 50;" + "-fx-cursor: hand;" + "-fx-border-width: 1.5;");
        });

        button_sign_in.setOnMouseExited(event -> {
            button_sign_in.setStyle("-fx-background-color: transparent;" + "-fx-text-fill: #3c97ff;");
        });

        // BACK
        button_back.setOnAction(new EventHandler<ActionEvent>() {
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

        button_back.setOnMouseEntered(event -> {
            button_back.setStyle("-fx-background-color: #ffffff;" + "-fx-text-fill: #3c97ff;" + "-fx-background-radius: 50;" + "-fx-border-radius: 50;" + "-fx-cursor: hand;" + "-fx-border-width: 1.5;");
        });

        button_back.setOnMouseExited(event -> {
            button_back.setStyle("-fx-background-color: #ffffff;" + "-fx-background-radius: 50;" + "-fx-border-radius: 50;" + "-fx-border-width: 1.5;");
        });
    }
}
