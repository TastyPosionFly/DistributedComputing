package RPC.Register;

import RPC.serverInfo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static RPC.Register.XMLHandler.loadConfig;

public class Register {
    private static final int SERVER_PORT = 8888;
    private static final int CLIENT_PORT = 9999;
    private static final int MAX_SEVER = 10;
    private static final int MAX_USER = 20;
    private static final ExecutorService SERVERSEVICE = Executors.newFixedThreadPool(MAX_SEVER);
    private static final ExecutorService CLIENTSEVICE = Executors.newFixedThreadPool(MAX_USER);
    private static final Timer timer = new Timer();
    private static final String SAVE_FILE = "src/main/java/RPC/Register/config.xml";

    private static Map<String, List<serverInfo>> remoteMethod;

    private static ServerSocket serverSocket = null;
    private static ServerSocket clientSocket = null;
    private static Socket server = null;
    private static Socket client = null;

    static {
        try {
            serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Server is listening on port:" + SERVER_PORT);
            clientSocket = new ServerSocket(CLIENT_PORT);
            System.out.println("Client is listening on port:" + CLIENT_PORT);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }


    public Register(String filepath) {
        remoteMethod = loadConfig(filepath);
    }

    public static void main(String[] args) {


        new Register(SAVE_FILE);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                remoteMethod = loadConfig(SAVE_FILE);
            }
        }, 0, 10000);

        Thread serverThread = new Thread(() -> {
            while (true) {
                try {
                    server = serverSocket.accept();
                    System.out.println("Server accepted");

                    Runnable serverTask = new ServerRegister(remoteMethod, server);
                    SERVERSEVICE.submit(serverTask);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error: " + e);
                }
            }
        });

        Thread clientThread = new Thread(() -> {
            while (true) {
                try {
                    client = clientSocket.accept();
                    System.out.println("Client accepted");

                    Runnable clientTask = new ClientRegister(client, remoteMethod);
                    CLIENTSEVICE.submit(clientTask);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error: " + e);
                }
            }
        });

        serverThread.start();
        clientThread.start();

    }
}
