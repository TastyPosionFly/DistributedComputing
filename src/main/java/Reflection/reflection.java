package Reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class reflection {

    public static void main(String[] args) throws Exception {

        Class<?> clazz = MyClass.class;

        Field field = clazz.getDeclaredField("filed1");

        field.setAccessible(true);

        MyClass myClass = new MyClass();

        field.set(myClass, "this is not a test");

        System.out.println("filed is:" + myClass.getFiled1());

        //����˽�еľ�̬����
        //��ȡ��̬����
        Method staticMethod = clazz.getDeclaredMethod("staticMethod");
        //�Ƚ�˽�еķ�������Ϊ���Է���
        staticMethod.setAccessible(true);
        //���о�̬����
        Object staticResult = staticMethod.invoke(null);
        System.out.println("private static method is " + staticResult);

        //˽�з���ֻ��ͨ��getDeclaredMethod��ȡ�� getMethod��ȡ����Public�ķ��������
        Method privateMethod = clazz.getDeclaredMethod("privateMethod");
        privateMethod.setAccessible(true);
        Object privateResult = privateMethod.invoke(myClass);
        System.out.println("private method is"+ privateResult);


    }


}

class MyClass{
    private String filed1 = "this is a test";

    public String getFiled1() {
        return filed1;
    }

    private static String staticMethod(){
        return "this is a private static method";
    }

    private String privateMethod(){
        return "this is a private method";
    }
}