package RPC.Client;

import RPC.Charge;
import RPC.message;
import RPC.test;

public class Client {
    public static void main(String[] args) {
        Charge charge = (Charge) DynamicProxyFactory.getProxy(Charge.class);
        test test = (test) DynamicProxyFactory.getProxy(RPC.test.class);
        message result = charge.charge("112345678900005055736");
        System.out.println(result.getResult());
    }
}
