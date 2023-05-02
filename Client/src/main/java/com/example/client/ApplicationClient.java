package com.example.client;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ApplicationClient extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ApplicationClient.class.getResource("start-page.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);

        stage.setTitle("Authorization page");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    private static Socket socket;
    private static BufferedReader bufferedReader;
    private static BufferedWriter bufferedWriter;

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

    public static void getResponse(ActionEvent actionEvent) {

        String response;
        //while (socket.isConnected()) {
            try {
                response = bufferedReader.readLine();
                String[] words = response.split(" ");
                ArrayList<String> newWords = new ArrayList<>(List.of(words));
                int size = newWords.size();
                if (newWords.get(0).equals("Error1")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Предоставленные учётные данные неверны. Пользователь не найден.");
                    alert.show();
                } else if (newWords.get(0).equals("Error2"))  {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Предоставленные учётные данные неверны. Пароли не совпадают.");
                    alert.show();
                } else if (newWords.get(0).equals("Error3")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Пользователь с данным номером телефона уже существует.");
                    alert.show();
                } else {
                    System.out.println(response + " - ответ с сервера!\n");
                    method(newWords.get(size - 1), newWords, actionEvent);
                }

            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                //break;
            }
        //}
    }

    public static void method(String resp, ArrayList<String> words, ActionEvent actionEvent) {
        words.remove(resp);
        if (resp.equals("SIGN-IN-PATIENT")) {
            SwitchScene.changeScene(actionEvent,"personal-account.fxml", "Welcome", words.get(0));
        }
        if (resp.equals("SIGN-IN-DOCTOR")) {
            SwitchScene.changeScene(actionEvent, "personal-account.fxml", "Welcome", words.get(0));
        }
        if (resp.equals("SIGN-UP-PATIENT")) {
            SwitchScene.changeScene(actionEvent, "personal-account.fxml", "Welcome", words.get(0));
            // сделать так, чтобы после регистрации открывалось новое окно с сообщением об успешной регистрации
            // и кнопкой, чтобы войти в личный кабинет
        }
        if (resp.equals("SIGN-UP-DOCTOR")) {
            SwitchScene.changeScene(actionEvent, "personal-account.fxml", "Welcome", words.get(0));
            // сделать так, чтобы после регистрации открывалось новое окно с сообщением об успешной регистрации
            // и кнопкой, чтобы войти в личный кабинет
        }
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