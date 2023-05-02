package com.example.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    // serverSocket как телефонный оператор, сидит и ждёт, когда кто-нибудь захочет подключиться ("позвонить")
    // когда "загорается лампочка" и происходит запрос на подключение, serverSocket создаёт socket (телефонную трубку)
    // и даёт её тому, кто будет "разговаривать"
    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {

        this.serverSocket = serverSocket;
    }

    public void startServer() {
        try {
            // пока подключение не закрыто (то есть пока сервер работает)
            while (!serverSocket.isClosed()) {
                // сервер ждёт подключения - метод accept()
                // программа останавливается на этом месте до тех пор, пока не появится желающий подключиться
                // как только он появился, создаётся socket (телефонная трубка), через которую будет происходить общение
                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected!");

                // ClientHandler - класс обработки клиента, то есть этот класс обрабатывает запрос, поступающий на сервер с клиента
                // ему передаётся socket (телефонная трубка/слово)
                ClientHandler clientHandler = new ClientHandler(socket);

                // если у нас уже произошло одно подключение, то чтобы сервер мог одновременно продолжать "стоять на стрёме"
                // и ожидать других подключений И обрабатывать уже поступивший запрос от клиента,
                // обработку запроса нужно выполнять в новом потоке
                // то есть в новых отдельных потоках будут происходить обработки запросов от клиентов
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            // closeServerSocket();
        }
    }

    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException  {

        // создаём "телефонного оператора", который слушает через порт 1234
        ServerSocket serverSocket = new ServerSocket(1234);

        // создаём объект сервера, в котором сидит наш оператор
        Server server = new Server(serverSocket);

        // запускаем сервер
        server.startServer();
    }
}