package com.example.server;

// сервер ждёт подключения -> кто-то подключается, на сервер приходит запрос от клиента ->
// запрос отправляется на обработку в обработчик клиента -> обработчик клиента общается с базой данных и из неё получает всю
// необходимую информацию -> и отправляет её клиенту

// запрос -> в Client Handler -> DBUtils -> в Client Handler -> Client

// принятие и отправка на обработку запроса от клиента


import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

// ОБРАБОТЧИК КЛИЕНТА

public class ClientHandler implements Runnable {

    // телефонная трубка
    private static Socket socket;

    // bufferedReader - слушает, что говорят в трубу
    private static BufferedReader bufferedReader;

    // bufferedWriter - говорит в трубку
    private static BufferedWriter bufferedWriter;

    // конструктор
    // обработчику передаётся "телефонная трубка", чтобы он знал, какой запрос нужно обработать
    public  ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override
    public void run() {
        // получаем запрос/сообщение от клиента
        String requestFromClient;

        // пока есть подключение
        //while (socket.isConnected()) {
            try {
                // в строку запроса считываем, то что сказали в "телефонную трубку"
                requestFromClient = bufferedReader.readLine();
                System.out.println("Запрос - " + requestFromClient + " - получен сервером.");

                String[] words = requestFromClient.split("   ");
                ArrayList<String> newWords = new ArrayList<>(List.of(words));

                String action = newWords.get(0);
                System.out.println(action);
                newWords.remove(0);

                if (action.equals("SIGN-IN-PATIENT")) {
                    requestFromClient = newWords.get(0) + "   " + newWords.get(1);
                    System.out.println(requestFromClient);
                    DBUtils.authorizationPatient(requestFromClient, action);
                }
                if (action.equals("SIGN-IN-DOCTOR")) {
                    requestFromClient = newWords.get(0) + "   " + newWords.get(1);
                    System.out.println(requestFromClient);
                    DBUtils.authorizationDoctor(requestFromClient, action);
                }
                if (action.equals("SIGN-UP-PATIENT")) {
                    requestFromClient = newWords.get(0) + "   " + newWords.get(1) + "   " + newWords.get(2) + "   " + newWords.get(3) + "   " + newWords.get(4) + "   " + newWords.get(5) + "   " + newWords.get(6);
                    System.out.println(requestFromClient);
                    DBUtils.registrationPatient(requestFromClient, action);
                }
                if (action.equals("SIGN-UP-DOCTOR")) {
                    requestFromClient = newWords.get(0) + "   " + newWords.get(1) + "   " + newWords.get(2) + "   " + newWords.get(3) + "   " + newWords.get(4) + "   " + newWords.get(5) + "   " + newWords.get(6) + "   " + newWords.get(7) + "   " + newWords.get(8);
                    System.out.println(requestFromClient);
                    DBUtils.registrationDoctor(requestFromClient, action);
                }
                if (action.equals("InfoAboutDoctors")) {
                    //DBUtils.getInfoAboutDoctors();
                    DBUtils.getDates();
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                //break;
            }
        //}
    }

    // сейчас в качестве параметра только номер телефона
    public static void sendResponseToClient(String response, String act) {
        try {
            bufferedWriter.write(response + "   " + act);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            System.out.println("Ответ - " + response + " " + act + " - отправлен клиенту.\n");
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public static void sendResponseToClientAboutDoctors(ArrayList<Doctor> doctors, String act) {
        try {
            bufferedWriter.write(doctors + " " + act);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            System.out.println("Ответ - " + doctors + " " + act + " - отправлен клиенту.\n");
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
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
}