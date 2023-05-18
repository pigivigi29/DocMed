package com.example.server;

// Этот класс будет обрабатывать всё, что связано с подключением к базе данных
// Служебный класс, который отвечает за коммуникацию с базой данных (авторизацию и регистрацию)

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import java.sql.*;
import java.util.ArrayList;

public class DBUtils {

    public static void getDates() {
        Connection connection = null;
        Statement statement = null;

        try {
            // Выполнить запрос
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "11111111");
            statement = connection.createStatement();

            String sql = "SELECT specialization, doctorsFIO, time_10_00, time_11_00, time_12_00, time_13_00, time_15_00, time_16_00, time_17_00, time_18_00, time_19_00 FROM records";

            ResultSet resultSet = statement.executeQuery(sql);
            String response = "";

            System.out.println("\nВывод информации, хранящейся в базе данных:");

            // Извлечение данных из результирующего набора
            while(resultSet.next()) {

                // Извлекаем по имени столбца
                String specialization  = resultSet.getString("specialization");
                String doctorFIO  = resultSet.getString("doctorsFIO");
                String _10  = resultSet.getString("time_10_00");
                String _11  = resultSet.getString("time_11_00");
                String _12  = resultSet.getString("time_12_00");
                String _13  = resultSet.getString("time_13_00");
                String _15  = resultSet.getString("time_15_00");
                String _16  = resultSet.getString("time_16_00");
                String _17  = resultSet.getString("time_17_00");
                String _18  = resultSet.getString("time_18_00");
                String _19  = resultSet.getString("time_19_00");

                response = response + specialization + " " + doctorFIO + " " + _10 + " " + _11 + " " + _12 + " " + _13 + " " + _15 + " " + _16 + " " + _17 + " " + _18 + " " + _19 + "   ";

                // Проверка
                System.out.print(specialization + " ");
                System.out.print(doctorFIO + " ");
                System.out.print(_10 + " ");
                System.out.print(_11 + " ");
                System.out.print(_12 + " ");
                System.out.print(_13 + " ");
                System.out.print(_15 + " ");
                System.out.print(_16 + " ");
                System.out.print(_17 + " ");
                System.out.print(_18 + " ");
                System.out.println(_19);
            }

            resultSet.close();
            System.out.println();
            System.out.println(response);
            ClientHandler.sendResponseToClient(response, "InfoAboutDoctors");

        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if(statement != null)
                    connection.close();
            } catch(SQLException se) {
                // do nothing
            }
            try {
                if (connection != null)
                    connection.close();
            } catch(SQLException se) {
                se.printStackTrace();
            }
        }
    }

    /*
    public static void getInfoAboutDoctors() {

        Connection connection = null;
        Statement statement = null;

        try {
            // Выполнить запрос
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "11111111");
            statement = connection.createStatement();

            String sql = "SELECT surname, name, patronymic, specialization, experience FROM doctors";

            ResultSet resultSet = statement.executeQuery(sql);
            String response = "";

            //ArrayList<Doctor> doctors = new ArrayList<Doctor>();

            // Извлечение данных из результирующего набора
            while(resultSet.next()) {

                // Извлекаем по имени столбца
                String surname  = resultSet.getString("surname");
                String name  = resultSet.getString("name");
                String patronymic  = resultSet.getString("patronymic");
                String specialization  = resultSet.getString("specialization");
                String experience  = resultSet.getString("experience");
                response = response + specialization + " " + surname + " " + name + " " + patronymic + " " + experience + "   ";

                //Doctor doctor = new Doctor(surname, name, patronymic, specialization, experience);
                //doctors.add(doctor);

                //Display values
                System.out.print(surname + " ");
                System.out.print(name + " ");
                System.out.print(patronymic + " ");
                System.out.print(specialization + " ");
                System.out.println(experience);
            }

            resultSet.close();
            System.out.println(response);
            ClientHandler.sendResponseToClient(response, "InfoAboutDoctors");
            // ClientHandler.sendResponseToClientAboutDoctors(doctors, "InfoAboutDoctors");
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if(statement != null)
                    connection.close();
            } catch(SQLException se) {
                // do nothing
            }
            try {
                if (connection != null)
                    connection.close();
            } catch(SQLException se) {
                se.printStackTrace();
            }
        }
    }
*/

    public static void registrationPatient(String requestFromClient, String action) {

        String request = requestFromClient;
        String act = action;
        String[] words = request.split("   ");

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
        String[] words = request.split("   ");

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
        String[] words = request.split("   ");

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
        String[] words = request.split("   ");

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
