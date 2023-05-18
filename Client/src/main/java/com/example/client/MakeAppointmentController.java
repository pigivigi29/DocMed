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
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MakeAppointmentController implements Initializable {

    @FXML
    private Button button_back;
    @FXML
    private Button button_make_an_appointment;

    @FXML
    private ChoiceBox<String> choiceBox_specialization;
    @FXML
    private ChoiceBox<String> choiceBox_fio_doctor;
    @FXML
    private ChoiceBox<String> choiceBox_time;

    //private String specializations[];
    private ActionEvent actionEvent;
    private ApplicationClient client;
    private String specialization;
    private String doctor;
    private String recordTime;

    public ActionEvent getActionEvent() {
       return actionEvent;
    }

    public void setActionEvent(ActionEvent actionEvent) {
        this.actionEvent = actionEvent;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // сначала необходимо получить список со специализациями имеющихся врачей и список самих врачей
        // это нужно для нашего choiceBox
        try {
            client = new ApplicationClient(new Socket("localhost", 1234));
            System.out.println("MakeAppointmentController: Connected to server.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        String action = "InfoAboutDoctors";
        String request = action;

        client.sendRequest(request);
        client.getResponse(null);

        choiceBox_specialization.getItems().addAll(ApplicationClient.getSpecializations());
        choiceBox_specialization.setOnAction(this::getSpecialization);


        // КНОПОЧКИ

        button_make_an_appointment.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String request = "";
                String action = "MAKE-AN-APPOINTMENT";

                if (specialization.isEmpty() || doctor.isEmpty() || recordTime.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Пожалуйста, заполните все поля.");
                    alert.show();
                } else {
                    // в этом реквесте мы должны посылать еще фио пациента
                    request = action + "   " + specialization + "   " + doctor + "   " + recordTime;
                    setActionEvent(actionEvent);
                    client.sendRequest(request);
                    client.getResponse(getActionEvent());
                }
            }
        });

        button_back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                SwitchScene.changeScene(actionEvent, "patient-profile.fxml", "Patient Profile", null);
            }
        });

        button_back.setOnMouseEntered(event -> {
            button_back.setStyle("-fx-background-color: #ffffff;" + "-fx-text-fill: #3c97ff;" + "-fx-background-radius: 50;" + "-fx-border-color: #6debd2;" + "-fx-border-radius: 50;" + "-fx-cursor: hand;" + "-fx-border-width: 1.5;");
        });

        button_back.setOnMouseExited(event -> {
            button_back.setStyle("-fx-background-color: #ffffff;" + "-fx-background-radius: 50;" + "-fx-border-radius: 50;" + "-fx-border-color: #6debd2;" + "-fx-border-width: 1.5;");
        });
    }

    public void getSpecialization(ActionEvent actionEvent) {
        specialization = choiceBox_specialization.getValue();
        String[] doctors = ApplicationClient.getDoctors();
        String[] FIOsDoctor;     // checkBox_fio_doctor
        String fioDoctor = null;
        ArrayList<String> FIOsDoctorsList = new ArrayList<String>();

        // Проверка
        for (int i = 0; i < doctors.length; i++) {
            System.out.println(doctors[i]);
        }

        System.out.println("\nПользователь выбрал специализацию: " + specialization + "\n");
        System.out.println("Доступные врачи по выбранной специализации: ");
        for (int i = 0; i < doctors.length; i++) {
            String[] infoDoctor = doctors[i].split(" ");

            if (infoDoctor[0].equals(specialization)) {
                System.out.println(infoDoctor[1] + " " + infoDoctor[2] + " " + infoDoctor[3]);
                fioDoctor = infoDoctor[1] + " " + infoDoctor[2] + " " + infoDoctor[3];
                FIOsDoctorsList.add(fioDoctor);
            }
        }
        FIOsDoctor = FIOsDoctorsList.toArray(new String[0]);
        choiceBox_fio_doctor.getItems().addAll(FIOsDoctor);
        choiceBox_fio_doctor.setOnAction(this::getDates);
    }

    public void getDates(ActionEvent actionEvent) {
        doctor = choiceBox_fio_doctor.getValue();
        String[] doctors = ApplicationClient.getDoctors();
        String[] time;     // checkBox_time
        ArrayList<String> timeList = new ArrayList<String>();

        System.out.println("\nПользователь выбрал доктора: " + doctor + "\n");
        System.out.println("Доступное время приёмов у выбранного доктора: ");
        for (int i = 0; i < doctors.length; i++) {
            String temp = "";
            String[] infoDoctor = doctors[i].split(" ");
            temp = infoDoctor[1] + " " + infoDoctor[2] + " " + infoDoctor[3];

            if (temp.equals(doctor)) {
                if(infoDoctor[4].equals("0")) {
                    timeList.add("10:00");
                    System.out.print("10:00 ");
                }
                if(infoDoctor[5].equals("0")) {
                    timeList.add("11:00");
                    System.out.print("11:00 ");
                }
                if(infoDoctor[6].equals("0")) {
                    timeList.add("12:00");
                    System.out.print("12:00 ");
                }
                if(infoDoctor[7].equals("0")) {
                    timeList.add("13:00");
                    System.out.print("13:00 ");
                }
                if(infoDoctor[8].equals("0")) {
                    timeList.add("15:00");
                    System.out.print("15:00 ");
                }
                if(infoDoctor[9].equals("0")) {
                    timeList.add("16:00");
                    System.out.print("16:00 ");
                }
                if(infoDoctor[10].equals("0")) {
                    timeList.add("17:00");
                    System.out.print("17:00 ");
                }
                if(infoDoctor[11].equals("0")) {
                    timeList.add("18:00");
                    System.out.print("18:00 ");
                }
                if(infoDoctor[12].equals("0")) {
                    timeList.add("19:00");
                    System.out.println("19:00 ");
                }
            }
        }
        time = timeList.toArray(new String[0]);
        choiceBox_time.getItems().addAll(time);
        choiceBox_time.setOnAction(this::getTime);
    }

    public void getTime(ActionEvent actionEvent) {
        recordTime = choiceBox_time.getValue();
        System.out.println("\nПользователь выбрал время: " + recordTime + "\n");
    }
}
