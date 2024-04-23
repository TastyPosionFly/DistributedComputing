package RPC.Register;

import RPC.serverInfo;

import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServerRegister implements Runnable{
    private Socket server;
    private Map<String, List<serverInfo>> remoteMethod;
    public ServerRegister(Map<String, List<serverInfo>> remoteMethod, Socket server) {
        this.remoteMethod = remoteMethod;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(server.getInputStream());
            serverInfo receivedServerObj = (serverInfo) inputStream.readObject();
            String host = receivedServerObj.getHost();
            int port = receivedServerObj.getPort();
            System.out.println(host + ":" + port);
            List<String> methods = receivedServerObj.getMethod();

            for (String method : methods) {
                if (remoteMethod.containsKey(method)) {
                    List<serverInfo> serverList = remoteMethod.get(method);
                    // 判断是否已存在相同的服务器信息
                    boolean exists = false;
                    for (serverInfo info : serverList) {
                        if (info.getHost().equals(host) && info.getPort() == port) {
                            exists = true;
                            break;
                        }
                    }
                    if (!exists) {
                        remoteMethod.get(method).add(new serverInfo(host, port, methods));
                    }
                } else {
                    remoteMethod.put(method, new ArrayList<>());
                    remoteMethod.get(method).add(new serverInfo(host, port));
                }
            }

            XMLHandler.writeConfig("src/main/java/RPC/Register/config.xml", remoteMethod);

            PrintWriter severMessage = new PrintWriter(server.getOutputStream());

            severMessage.println("Register successfully");
            severMessage.flush();

            server.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
