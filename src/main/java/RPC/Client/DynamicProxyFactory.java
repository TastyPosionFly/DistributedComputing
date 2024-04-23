package RPC.Client;

import RPC.message;
import RPC.serverInfo;

import javax.sound.sampled.Port;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

public class DynamicProxyFactory {
    private static final String REGISTER_HOST = "127.0.0.1";
    private static final int REGISTER_PORT = 9999;

    public static Object getProxy(final Class classType) {

        serverInfo serverInfo = DynamicProxyFactory.getSever(classType.getName());

        if (serverInfo == null){
            System.out.println("Register is offline");
            System.exit(1);
        }
        String host = serverInfo.getHost();
        int port = serverInfo.getPort();

        if (host == "0.0.0.0" && port == 0){
            System.out.println("Service is not in any server");
            System.exit(1);
        }

        InvocationHandler handler = new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object args[]) throws Exception {
                Connector connector = null;
                try {
                    connector = new Connector(host, port);
                    message call = new message(classType.getName(), method.getName(), method.getParameterTypes(), args);
                    connector.send(call);
                    call = (message) connector.receive();
                    Object result = call.getResult();
                    return call;
                } finally {
                    if (connector != null) connector.close();
                }

            }
        };

        return Proxy.newProxyInstance(classType.getClassLoader(), new Class[]{ classType } , handler);
    }

    private static serverInfo getSever(String className){
        serverInfo serverInfo = null;
        try {
            Socket register = new Socket(REGISTER_HOST, REGISTER_PORT);
            ObjectOutputStream oos = new ObjectOutputStream(register.getOutputStream());

            oos.writeUTF(className);
            oos.flush();

            ObjectInputStream ois = new ObjectInputStream(register.getInputStream());
            serverInfo = (serverInfo) ois.readObject();
            oos.close();
            ois.close();
            register.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return serverInfo;
    }
}
