package Socket.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public static void main(String[] args) {
        Socket server = null;
        try {
            String inputString;
            server =new Socket("127.0.0.1", 6666);
            System.out.println("choose your want service: 0:consume, 1:recharge");
            BufferedReader sin =new BufferedReader(new InputStreamReader(System.in));
            PrintWriter serverMessage =new PrintWriter(server.getOutputStream());
            BufferedReader clientMessage = new BufferedReader(new InputStreamReader(server.getInputStream()));
            String serviceType = sin.readLine();
            System.out.println("Enter 9-digit card number");
            String cardNumber = sin.readLine();
            System.out.println("Enter 5-digit amount");
            String amount = sin.readLine();
            String data = serviceType + cardNumber + amount + "55746";

            serverMessage.println(data);
            serverMessage.flush();
            String response = clientMessage.readLine();
            System.out.println("Server response: "+ response);

            serverMessage.close();
            clientMessage.close();
            server.close();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
