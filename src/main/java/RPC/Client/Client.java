package RPC.Client;

import RPC.Charge;
import RPC.message;

public class Client {
    public static void main(String[] args) {
        Charge charge = (Charge) DynamicProxyFactory.getProxy(Charge.class);
        message result = charge.charge("112345678900005055736");
        System.out.println(result.getResult());
    }
}
