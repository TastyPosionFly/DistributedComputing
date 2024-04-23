package RPC.Register;

import RPC.serverInfo;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ClientRegister implements Runnable{
    private Socket client;
    private Map<String, List<serverInfo>> serverMethods;

    public ClientRegister(Socket client, Map<String, List<serverInfo>> serverMethods) {
        this.client = client;
        this.serverMethods = serverMethods;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
            String requestMethod = ois.readUTF();
            System.out.println("Client request method is "+requestMethod + "\n");
            //负载均衡
            List<serverInfo> list = serverMethods.get(requestMethod);
            if (list == null || list.isEmpty()) {
                oos.writeObject(new serverInfo("0.0.0.0", 0));
                oos.flush();
            } else {
                Random random = new Random();
                int randomIndex = random.nextInt(list.size());

                serverInfo serverInfo = list.get(randomIndex);
                oos.writeObject(serverInfo);
                oos.flush();
            }
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
