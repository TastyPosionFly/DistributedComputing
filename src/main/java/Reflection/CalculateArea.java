package Reflection;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

interface Shape{
    double calculateArea(double... dimensions);
}

class Circle implements Shape{
    @Override
    public double calculateArea(double... dimensions) {
        double radius = dimensions[0];
        return Math.PI * radius * radius;
    }
}

class Rectangle implements Shape {
    @Override
    public double calculateArea(double... dimensions) {
        double length = dimensions[0];
        double width = dimensions[1];
        return length * width;
    }
}

public class CalculateArea {
    public static void main(String[] args) {
        Properties properties = new Properties();
        try {
            properties.loadFromXML(new FileInputStream("D:/Work/DC/DistributedComputing/src/Reflection/shape.xml"));
        } catch (IOException e){
            e.printStackTrace();
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("��������״���͵����ƣ�");
        String shapeType = scanner.next();

        String englishShapeType = properties.getProperty(shapeType);
        if (englishShapeType == null) {
            System.out.println("δ�ҵ���Ӧ����״����");
            return;
        }

        List<Double> dimensionsList = new ArrayList<>();
        System.out.println("��������״�Ĳ�������������������ַ��������룩��");
        while (scanner.hasNextDouble()) {
            dimensionsList.add(scanner.nextDouble());
        }

        double[] dimensions = dimensionsList.stream().mapToDouble(Double::doubleValue).toArray();

        try {
            Class<?> clazz = Class.forName("Reflection."+englishShapeType);
            Shape shape = (Shape) clazz.getDeclaredConstructor().newInstance();
            double result = shape.calculateArea(dimensions);
            System.out.println("Area is "+ result);
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            System.out.println("�޷��������״�����");
            e.printStackTrace();
        }

    }
}
