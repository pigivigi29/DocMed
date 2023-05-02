package com.example.server;

// Этот класс будет обрабатывать всё, что связано с подключением к базе данных
// Служебный класс, который отвечает за коммуникацию с базой данных (авторизацию и регистрацию)

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import java.sql.*;

public class DBUtils {

    public static void registrationPatient(String requestFromClient, String action) {

        String request = requestFromClient;
        String act = action;
        String[] words = request.split(" ");

        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "11111111");
            psCheckUserExists = connection.prepareStatement("SELECT * FROM patients WHERE phone = ?");
            psCheckUserExists.setString(1, words[0]);
            resultSet = psCheckUserExists.executeQuery();

            if (resultSet.isBeforeFirst()) {
                ClientHandler.sendResponseToClient("Error3", act);
                System.out.println("Пользователь уже существует!");
            } else {
                psInsert = connection.prepareStatement("INSERT INTO patients (phone, password, surname, name, patronymic, email, birthday) VALUES (?, ?, ?, ?, ?, ?, ?)");
                psInsert.setString(1, words[0]);
                psInsert.setString(2, words[1]);
                psInsert.setString(3, words[2]);
                psInsert.setString(4, words[3]);
                psInsert.setString(5, words[4]);
                psInsert.setString(6, words[5]);
                psInsert.setString(7, words[6]);
                psInsert.executeUpdate();

                ClientHandler.sendResponseToClient(words[0], act);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeR(resultSet, psCheckUserExists, psInsert, connection);
        }
    }

    public static void registrationDoctor(String requestFromClient, String action) {

        String request = requestFromClient;
        String act = action;
        String[] words = request.split(" ");

        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "11111111");
            psCheckUserExists = connection.prepareStatement("SELECT * FROM doctors WHERE phone = ?");
            psCheckUserExists.setString(1, words[0]);
            resultSet = psCheckUserExists.executeQuery();

            if (resultSet.isBeforeFirst()) {
                ClientHandler.sendResponseToClient("Error3", act);
                System.out.println("Пользователь уже существует!");

                //Alert alert = new Alert(Alert.AlertType.ERROR);
                //alert.setContentText("Пользователь с данным номером телефона уже существует.");
                //alert.show();
            } else {
                //psInsert = connection.prepareStatement("INSERT INTO patients (phone, password) VALUES (?, ?)");
                //psInsert.setString(1, phone);
                //psInsert.setString(2, password);
                //psInsert.executeUpdate();
                psInsert = connection.prepareStatement("INSERT INTO doctors (phone, password, surname, name, patronymic, email, birthday, specialization, experience) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                psInsert.setString(1, words[0]);
                psInsert.setString(2, words[1]);
                psInsert.setString(3, words[2]);
                psInsert.setString(4, words[3]);
                psInsert.setString(5, words[4]);
                psInsert.setString(6, words[5]);
                psInsert.setString(7, words[6]);
                psInsert.setString(8, words[7]);
                psInsert.setString(9, words[8]);
                psInsert.executeUpdate();

                ClientHandler.sendResponseToClient(words[0], act);
                //changeScene(actionEvent, "personal-account.fxml", "Welcome!", phone);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeR(resultSet, psCheckUserExists, psInsert, connection);
        }
    }


    // АВТОРИЗАЦИЯ

    public static void authorizationPatient(String requestFromClient, String action) {

        String request = requestFromClient;
        String act = action;
        String[] words = request.split(" ");

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "11111111");
            preparedStatement =connection.prepareStatement("SELECT password FROM patients WHERE phone = ?");
            preparedStatement.setString(1, words[0]);
            resultSet = preparedStatement.executeQuery();
            System.out.println("Подключение к базе данных установлено.");

            if (!resultSet.isBeforeFirst()) {
                ClientHandler.sendResponseToClient("Error1", act);
                System.out.println("Пользователь не найден!");
            } else {
                while (resultSet.next()) {
                    String retrievedPassword = resultSet.getString("password");

                    if (retrievedPassword.equals(words[1])) {
                        ClientHandler.sendResponseToClient(words[0], act);
                    } else {
                        System.out.println("Пароли не совпадают!");
                        ClientHandler.sendResponseToClient("Error2", act);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeA(resultSet, preparedStatement, connection);
        }
    }

    public static void authorizationDoctor(String requestFromClient, String action) {

        String request = requestFromClient;
        String act = action;
        String[] words = request.split(" ");

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "11111111");
            preparedStatement =connection.prepareStatement("SELECT password FROM doctors WHERE phone = ?");
            preparedStatement.setString(1, words[0]);
            resultSet = preparedStatement.executeQuery();
            System.out.println("Подключение к базе данных установлено.");

            if (!resultSet.isBeforeFirst()) {
                ClientHandler.sendResponseToClient("Error1", act);
                System.out.println("Пользователь не найден!");
            } else {
                while (resultSet.next()) {
                    String retrievedPassword = resultSet.getString("password");

                    if (retrievedPassword.equals(words[1])) {
                        ClientHandler.sendResponseToClient(words[0], act);
                    } else {
                        System.out.println("Пароли не совпадают!");
                        ClientHandler.sendResponseToClient("Error2", act);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeA(resultSet, preparedStatement, connection);
        }
    }

    public static void closeA(ResultSet resultSet, PreparedStatement preparedStatement, Connection connection) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeR(ResultSet resultSet, PreparedStatement psCheckUserExists, PreparedStatement psInsert, Connection connection) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (psCheckUserExists != null) {
            try {
                psCheckUserExists.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (psInsert != null) {
            try {
                psInsert.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
