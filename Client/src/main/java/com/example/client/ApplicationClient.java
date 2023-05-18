package com.example.client;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.stream.Collectors;

public class ApplicationClient extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ApplicationClient.class.getResource("start-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        stage.setTitle("Authorization Page");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    private static Socket socket;
    private static BufferedReader bufferedReader;
    private static BufferedWriter bufferedWriter;

    private static String[] specializations;
    private static String[] doctors;
    private String[] doctorsFIO;

    public ApplicationClient() {}

    public ApplicationClient(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (IOException e) {
            System.out.println("Error creating client.");
            e.printStackTrace();
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public static void sendRequest(String request) {
        try {
            bufferedWriter.write(request);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            System.out.println("Запрос - " + request + " - отправлен на сервер.");

        } catch (IOException e) {
            System.out.println("Error sending message to the client.");
            e.printStackTrace();
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    // Получаем ответ с сервера
    public void getResponse(ActionEvent actionEvent) {

        String response;
        //while (socket.isConnected()) {
            try {
                // response - слушаем, что сказали в трубку
                // сервер посылает строку формата: Специализация Фамилия Имя Отчество  Команда-ключ
                response = bufferedReader.readLine();
                String[] words = response.split("   ");
                ArrayList<String> newWords = new ArrayList<>(List.of(words));
                int size = newWords.size();
                if (newWords.get(0).equals("Error1")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Предоставленные учётные данные неверны. Пользователь не найден.");
                    alert.show();
                } else if (newWords.get(0).equals("Error2")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Предоставленные учётные данные неверны. Пароли не совпадают.");
                    alert.show();
                } else if (newWords.get(0).equals("Error3")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Пользователь с данным номером телефона уже существует.");
                    alert.show();
                } else {
                    System.out.println(response + " - ответ с сервера!\n");
                    // это надо
                    method(newWords.get(size - 1), newWords, actionEvent);
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                //break;
            }
        //}
    }

    public void method(String resp, ArrayList<String> words, ActionEvent actionEvent) {
        words.remove(resp);
        if (resp.equals("SIGN-IN-PATIENT")) {
            // SwitchScene.changeScene(actionEvent,"personal-account.fxml", "Welcome", words.get(0));
            SwitchScene.changeScene(actionEvent, "patient-profile.fxml", "Patient Profile", words.get(0));
        }
        if (resp.equals("SIGN-IN-DOCTOR")) {
            SwitchScene.changeScene(actionEvent, "personal-account.fxml", "Doctor Profile", words.get(0));
        }
        if (resp.equals("SIGN-UP-PATIENT")) {
            //SwitchScene.changeScene(actionEvent, "personal-account.fxml", "Welcome", words.get(0));
            SwitchScene.changeScene(actionEvent, "after-registration-patient.fxml", "Registration is completed!", null);
            // сделать так, чтобы после регистрации открывалось новое окно с сообщением об успешной регистрации
            // и кнопкой, чтобы войти в личный кабинет
        }
        if (resp.equals("SIGN-UP-DOCTOR")) {
            //SwitchScene.changeScene(actionEvent, "personal-account.fxml", "Welcome", words.get(0));
            SwitchScene.changeScene(actionEvent, "after-registration-patient.fxml", "Registration is completed!", null);
            // сделать так, чтобы после регистрации открывалось новое окно с сообщением об успешной регистрации
            // и кнопкой, чтобы войти в личный кабинет
        }
        // выгружаем информацию о докторах (специализации, фио, время приёмов)
        if (resp.trim().equals("InfoAboutDoctors")) {

            words.remove(words.size() - 1);
            this.doctors = words.toArray(new String[0]);

            List<String> specializationsList = new ArrayList<>();
            // List<String> experiences = new ArrayList<>();
            List<String> doctorFIOList = new ArrayList<>();
            List<String> timeList = new ArrayList<>();

            // Проверка
            for (int i = 0; i < words.size(); i++) {
               String fio = "";
               String time = "";
               System.out.println(words.get(i));
               // строка из массвиа doctor = специализация - фамилия - имя - отчество - время приема
               String[] doctor = words.get(i).split(" ");

               ArrayList<String> newDoctor = new ArrayList<>(List.of(doctor));

               for (int j = 0; j < newDoctor.size(); j++) {
                   if (j == 0) {
                       // если список специализаций еще не содержит данную специализацию, то добавляем ее в список
                       if (!specializationsList.contains(newDoctor.get(j))) {
                           specializationsList.add(newDoctor.get(j));
                       }
                   } else if (j == 1 || j == 2 || j == 3) {
                       // experiences.add(newDoctor.get(j));
                       fio = fio + newDoctor.get(j) + " ";
                   } else {
                       // fio = fio + newDoctor.get(j) + " ";
                       time = time + newDoctor.get(j) + " ";
                   }
               }
               doctorFIOList.add(fio);
               timeList.add(time);
            }
            String[] specializations = specializationsList.toArray(new String[0]);
            String[] doctorsFIO = doctorFIOList.toArray(new String[0]);
            String[] time = timeList.toArray(new String[0]);

            // Выводим полученные специализации для choiceBox_specialization
            System.out.println();
            for (int i = 0; i < specializations.length; i++) {
                System.out.println(specializations[i]);
            }

            // Выводим полученные ФИО врачей для choiceBox_fio_doctor
            System.out.println();
            System.out.println("fios");
            for (int i = 0; i < doctorsFIO.length; i++) {
                System.out.println(doctorsFIO[i]);
            }

            this.specializations = specializations;

            // Выводим полученные ФИО врачей для choiceBox_time
            System.out.println();
            System.out.println("times");
            for (int i = 0; i < time.length; i++) {
                System.out.println(time[i]);
            }
        }
    }

    public static String[] getSpecializations() {
        return specializations;
    }

    public static String[] getDoctors() {
        return doctors;
    }

    public static void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Application.launch();
    }
}