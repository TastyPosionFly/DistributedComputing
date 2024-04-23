package Proxy;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Scanner;

interface Calculate {
    int divide(int a, int b);
}


public class Division implements Calculate {
    public int divide(int a, int b) {
        int result = -1;

        try {
            System.out.println("��ʼ����.....");
            result = a / b;
            System.out.println("�������.....");
            return result;
        } catch (Exception e) {
        }

        return result;
    }
}

class ProxyHandler implements InvocationHandler {
    private Object target;

    public ProxyHandler(Object target) {
        this.target = target;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("divide")) {
            int a = (int) args[0];
            int b = (int) args[1];

            if (b == 0) {
                System.out.println("�������");
                return -9999;
            }

            int result = (int) method.invoke(target, args);

            if (a % b != 0) {
                System.out.println("����Ϊ: " + (a % b));
            }

            return result;
        } else {
            return method.invoke(target, args);
        }
    }
}

class Main{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("�����뱻������");
        int a = scanner.nextInt();
        System.out.print("�����������");
        int b = scanner.nextInt();

        Calculate calculate = new Division();
        Calculate proxy = (Calculate) Proxy.newProxyInstance(
                calculate.getClass().getClassLoader(),
                calculate.getClass().getInterfaces(),
                new ProxyHandler(calculate)
        );

        int result = proxy.divide(a,b);
        System.out.println("���ս��Ϊ: "+result);
    }
}