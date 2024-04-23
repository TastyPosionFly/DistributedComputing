package RPC.Server;

import RPC.message;
import RPC.serverInfo;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChargeSever {
    private static Map<String, String> serverMethods = new HashMap<String, String>();
    private static final String REGISTER_HOST = "192.168.172.161";
    private static final int REGISTER_PORT = 8888;
    private static final int PORT = 1234;
    private static final int MAX_USER = 20;
    private static final ExecutorService CLIENT_SERVICE = Executors.newFixedThreadPool(MAX_USER);
    private static Timer timer = new Timer();
    private static Map<String, String> userData = new HashMap<>();
    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static final String FILE_PATH = "src/main/java/RPC/Server/save.txt";
    private static final String SEVER_CONFIG_FILE_PATH = "src/main/java/RPC/Server/chargeConfig.xml";

    private static String HOST = " ";

    static {
        try {
            HOST = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void sendServerInfo(serverInfo info) {
        try {
            Socket socket = new Socket(REGISTER_HOST, REGISTER_PORT);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(info);
            outputStream.flush();

            BufferedReader registerMessage = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response = registerMessage.readLine();
            System.out.println("Server response: "+ response);

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void serverInit() {
        serverMethods = XMLReader.loadConfig(SEVER_CONFIG_FILE_PATH);

        System.out.println("Server Address: " + HOST);
        System.out.println("Server Port: " + PORT);
        System.out.println("Server Methods: " + serverMethods);

        serverInfo serverInfo = new serverInfo(HOST, PORT, new ArrayList<>(serverMethods.keySet()));

        sendServerInfo(serverInfo);

        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Sever is listening on port " + PORT);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        clientSocket = null;

        File saveFile = new File(FILE_PATH);
        if (!saveFile.exists()) {
            try {
                saveFile.createNewFile();
                System.out.println("file has created");
            } catch (IOException e) {
                System.out.println("file created fail");
                e.printStackTrace();
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(saveFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] pair = line.split(" ");
                userData.put(pair[0], pair[1]);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public  void start() {
        serverInit();

        while (true) {
            try {
                clientSocket = serverSocket.accept();

                CLIENT_SERVICE.submit(new ChargeService(clientSocket, serverMethods));


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
