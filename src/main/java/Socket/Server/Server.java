package Socket.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    static int port = 6666;
    static int maxUser = 10;
    static ExecutorService executorService = Executors.newFixedThreadPool(maxUser);
    static Timer timer = new Timer();
    static Map<String, String> userData = new HashMap<>();
    static volatile boolean isRunning = true;

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket client = null;
        //����ļ���
        File saveFile = new File("D:/Work/DC/DistributedComputing/save.txt");
        if (!saveFile.exists()){
            try {
                saveFile.createNewFile();
                System.out.println("file has created");
            } catch (IOException e) {
                System.out.println("file created fail");
                e.printStackTrace();
            }
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(saveFile))){
            String line;
            while ((line = reader.readLine()) != null){
                String[] pair = line.split(" ");
                userData.put(pair[0],pair[1]);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        timer.schedule(new WriteToFile(userData, saveFile), 0, 10000);

        //��������̨���÷������ɿص�ֹͣ
        Thread consoleListener = new Thread(()->{
            Scanner scanner = new Scanner(System.in);
            while (true){
                String input = scanner.nextLine();
                if (input.equals("shutdown")){
                    isRunning = false;
                    shutdownServer();
                    System.exit(0);
                    break;
                }
            }
            scanner.close();
        });

        consoleListener.start();

        while (isRunning){
            try {
                serverSocket =new ServerSocket(port);
                System.out.println("Server is listening on port:" + port);
            } catch (Exception e) {
                System.out.println("Error: " + e);
                System.exit(-1);
            }

            try {
                client = serverSocket.accept();
            }catch (Exception e){
                System.out.println("Client connected failed");
                System.exit(-1);
            }
            ServerThread serverThread =new ServerThread(client, userData);
            Thread t = new Thread(serverThread);
            executorService.submit(t);
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.out.println("close failed");
            }
        }
    }

    static private void shutdownServer(){
        executorService.shutdown();
        timer.cancel();
        System.out.println("Sever has closed");
    }
}

class ServerThread implements Runnable{

    private Socket client;
    private Map<String, String> userData;

    public ServerThread(Socket client, Map<String, String> userData){
        this.client = client;
        this.userData = userData;
    }

    @Override
    public void run() {
        try {
            BufferedReader clientMessage = new BufferedReader(new InputStreamReader(
                    client.getInputStream()));
            PrintWriter severMessage = new PrintWriter(client.getOutputStream());

            String data = clientMessage.readLine();
            if (data != null && data.length() == 20){
                if (data.startsWith("55746", 15)){
                    String serverType = data.substring(0,1);//0Ϊ���ѣ�1Ϊ��ֵ
                    String cardNumber = data.substring(1,10);
                    String money = data.substring(10,15);
                    System.out.println("user's card number: " + cardNumber + " connected");
                    if (!userData.containsKey(cardNumber)){
                        if (serverType.equals("0")){
                            System.out.println("User is not exist");
                            severMessage.println("User is not exist");
                            severMessage.flush();
                        } else {
                            userData.put(cardNumber, money);
                            System.out.println("user created success,recharge success, recharge" + money + "yuan");
                            severMessage.println("user created success,recharge success, recharge" + money + "yuan");
                            severMessage.flush();
                        }
                    } else {
                        if(serverType.equals("0")){
                            int originMoney = Integer.parseInt(userData.get(cardNumber));
                            int subMoney = Integer.parseInt(money);
                            if (originMoney < subMoney){
                                System.out.println("money is not enough");
                                severMessage.println("money is not enough");
                                severMessage.flush();
                            } else {
                                userData.put(cardNumber, String.valueOf(originMoney - subMoney));
                                System.out.println("pay " + money + "success");
                                severMessage.println("pay " + money + "success");
                                severMessage.flush();
                            }
                        } else if (serverType.equals("1")) {
                            int originMoney = Integer.parseInt(userData.get(cardNumber));
                            int addMoney = Integer.parseInt(money);
                            userData.put(cardNumber, String.valueOf(originMoney + addMoney));
                            System.out.println("recharge"+money+" success");
                            severMessage.println("recharge"+money+" success");
                            severMessage.flush();
                        }
                    }
                }else {
                    System.out.println("Data illegal");
                    severMessage.println("Data illegal");
                    severMessage.flush();
                }
            }else {
                System.out.println("Data Error");
                severMessage.println("Data Error");
                severMessage.flush();
            }
            clientMessage.close();
            severMessage.close();
            client.close();
            System.out.println("trade "+data+" finished");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class WriteToFile extends TimerTask{

    private Map<String, String> userData;
    private File saveFile;

    public WriteToFile(Map<String, String> userData, File saveFile){
        this.userData = userData;
        this.saveFile = saveFile;
    }

    @Override
    public void run() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(saveFile))){
            if (userData != null) {
                for (Map.Entry<String, String> entry : userData.entrySet()) {
                    bufferedWriter.write(entry.getKey() + " " + entry.getValue());
                    bufferedWriter.newLine();
                }
            }
            try (BufferedReader reader = new BufferedReader(new FileReader(saveFile))){
                String line;
                while ((line = reader.readLine()) != null){
                    String[] pair = line.split(" ");
                    userData.put(pair[0],pair[1]);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            System.out.println("save File error");
            e.printStackTrace();
        }
    }
}
