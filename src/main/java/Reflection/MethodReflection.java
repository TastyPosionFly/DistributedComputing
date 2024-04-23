package Reflection;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Scanner;

public class MethodReflection {
    public static void main(String[] args) {
        try {
            System.out.print("请输入类名：");
            Scanner scanner = new Scanner(System.in);
            String className = scanner.next();

            Class<?> clazz = Class.forName(className);

            System.out.print("请输入属性名或方法名：");
            String memberName = scanner.next();

            Field field = null;
            Method method = null;

            try {
                field = clazz.getDeclaredField(memberName);
            } catch (NoSuchFieldException e) {
                try {
                    method = clazz.getDeclaredMethod(memberName);
                } catch (NoSuchMethodException ex) {
                    System.out.println("属性或方法不存在");
                    return;
                }
            }

            if (field != null) {
                Class<?> fieldType = field.getType();
                System.out.println("输入属性值");
                String valueStr = scanner.next();

                Object value = null;
                if (fieldType == int.class || fieldType == Integer.class) {
                    value = Integer.parseInt(valueStr);
                } else if (fieldType == double.class || fieldType == Double.class) {
                    value = Double.parseDouble(valueStr);
                } else if (fieldType == boolean.class || fieldType == Boolean.class) {
                    value = Boolean.parseBoolean(valueStr);
                } else if (fieldType == String.class) {
                    value = valueStr;
                }

                field.setAccessible(true);
                if (Modifier.isStatic(field.getModifiers())) {
                    field.set(null, value);
                } else {
                    Object instance = clazz.getDeclaredConstructor().newInstance();
                    field.set(instance, value);
                }
            }

            if (method != null) {
                Class<?>[] paramTypes = method.getParameterTypes();
                Object[] params = new Object[paramTypes.length];

                for (int i = 0; i < paramTypes.length; i++) {
                    System.out.print("请输入参数 " + (i + 1) + " 的值：");
                    String paramValue = scanner.nextLine();

                    // 根据参数类型进行转换
                    if (paramTypes[i] == int.class || paramTypes[i] == Integer.class) {
                        params[i] = Integer.parseInt(paramValue);
                    } else if (paramTypes[i] == double.class || paramTypes[i] == Double.class) {
                        params[i] = Double.parseDouble(paramValue);
                    } else if (paramTypes[i] == boolean.class || paramTypes[i] == Boolean.class) {
                        params[i] = Boolean.parseBoolean(paramValue);
                    } else {
                        params[i] = paramValue;
                    }
                }

                Object result;
                if (Modifier.isStatic(method.getModifiers())){
                    result = method.invoke(null, params);
                } else {
                    result = method.invoke(clazz.getDeclaredConstructor().newInstance(), params);
                }
                System.out.println("方法执行结果：" + result);
            }

        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}


