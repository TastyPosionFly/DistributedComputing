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

        //调用私有的静态方法
        //获取静态方法
        Method staticMethod = clazz.getDeclaredMethod("staticMethod");
        //先将私有的方法设置为可以访问
        staticMethod.setAccessible(true);
        //运行静态方法
        Object staticResult = staticMethod.invoke(null);
        System.out.println("private static method is " + staticResult);

        //私有方法只能通过getDeclaredMethod获取， getMethod获取的是Public的方法或变量
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