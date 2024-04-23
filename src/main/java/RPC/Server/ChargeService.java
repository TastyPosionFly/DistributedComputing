package RPC.Server;

import RPC.message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

public class ChargeService implements Runnable {
    private Socket clientSocket;
    private Map<String, String> serverMethods;

    public ChargeService(Socket clientSocket, Map<String, String> serverMethods) {
        this.clientSocket = clientSocket;
        this.serverMethods = serverMethods;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());

            message message = (message) inputStream.readObject();

            System.out.println("Client message: " + message);

            String className = message.getClassName();
            String methodName = message.getMethodName();
            Object[] params = message.getParams();
            Class<?> classType = Class.forName(serverMethods.get(className));
            Class<?>[] paramTypes = message.getParamTypes();
            Method method = classType.getMethod(methodName, paramTypes);

            Object result = method.invoke(classType.newInstance(), params);

            outputStream.writeObject(result);
            outputStream.flush();

            clientSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
