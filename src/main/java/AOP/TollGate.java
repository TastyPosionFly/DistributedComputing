package AOP;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.sql.rowset.spi.XmlReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.lang.reflect.*;
import java.util.*;

public class TollGate{
    private IoContainer ioContainer;
    private TollAspect tollAspect;
    private XMLReader xmlReader;

    public TollGate(IoContainer ioContainer, TollAspect tollAspect, XMLReader xmlReader) {
        this.ioContainer = ioContainer;
        this.tollAspect = tollAspect;
        this.xmlReader = xmlReader;
    }

    public void charge() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("输入车辆类型:");
        String vehicleType = scanner.nextLine();
        System.out.println("请输入行驶里程数：");
        double distance = scanner.nextDouble();

        tollAspect.setDistance(distance);

        Vehicle vehicle = ioContainer.getVehicle(vehicleType);
        if (vehicle == null) {
            System.out.println("车辆类型未找到");
            return;
        }


        Vehicle proxy = (Vehicle) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[]{Vehicle.class},
                new TollGateInvocationHandler(
                        vehicle, tollAspect, xmlReader
                )
        );

        proxy.tollFee(distance);

        scanner.close();
    }
}

class TollGateInvocationHandler implements InvocationHandler {
    private final Vehicle vehicle;
    private final TollAspect tollAspect;
    private final XMLReader xmlReader;

    public TollGateInvocationHandler(Vehicle vehicle, TollAspect tollAspect, XMLReader xmlReader) {
        this.vehicle = vehicle;
        this.tollAspect = tollAspect;
        this.xmlReader = xmlReader;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<String> beforeMethod = xmlReader.getBeforeMethod();
        for (String methodName : beforeMethod) {
            Method beforeMethodMethod = TollAspect.class.getMethod(methodName);
            beforeMethodMethod.invoke(tollAspect);
        }

        double result = (double)method.invoke(vehicle, args);
        System.out.println("费用为: " + result);

        List<String> afterMethod = xmlReader.getAfterMethod();
        for (String methodName : afterMethod) {
            Method afterMethodMethod = TollAspect.class.getMethod(methodName);
            afterMethodMethod.invoke(tollAspect);
        }

        return result;
    }
}

class TollAspect {
    private double distance; // 行驶距离

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void beforeCharge(){
        System.out.println("欢迎光临xxx收费站，行驶里程数为" + distance);
    }

    public void afterCharge(){
        System.out.println("谢谢，祝旅途顺利！");
    }
}


class IoContainer{
    private Map<String, Vehicle> vehicleMap = new HashMap<>();

    public void loadConfig(String filePath){
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
            Document document = builder.parse(filePath);
            NodeList nodeList = document.getElementsByTagName("vehicle");
            for (int i=0; i<nodeList.getLength(); i++){
                Element element = (Element) nodeList.item(i);
                String vehicleType = element.getAttribute("type");
                String className = element.getTextContent();
                Class<?> clazz = Class.forName(className);
                Vehicle vehicle = (Vehicle) clazz.getDeclaredConstructor().newInstance();
                vehicleMap.put(vehicleType,vehicle);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Vehicle getVehicle(String vehicleType) {
        return vehicleMap.get(vehicleType);
    }
}

class XMLReader{
    private List<String> beforeMethod = new ArrayList<>();
    private List<String> afterMethod = new ArrayList<>();

    public void loadConfig(String filePath){
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(filePath);
            Element root = document.getDocumentElement();
            NodeList beforeMethodNodes = root.getElementsByTagName("beforeMethod");
            for (int i=0; i<beforeMethodNodes.getLength(); i++){
                Element element = (Element) beforeMethodNodes.item(i);
                String methodName = element.getTextContent();
                beforeMethod.add(methodName);
            }
            NodeList afterMethodNodes = root.getElementsByTagName("afterMethod");
            for (int i=0; i<afterMethodNodes.getLength(); i++){
                Element element = (Element) afterMethodNodes.item(i);
                String methodName = element.getTextContent();
                afterMethod.add(methodName);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<String> getBeforeMethod() {
        return beforeMethod;
    }
    public List<String> getAfterMethod() {
        return afterMethod;
    }
}

class Main{
    public static void main(String[] args) {
        IoContainer ioContainer = new IoContainer();
        ioContainer.loadConfig("src/main/java/AOP/IOConfig.xml");
        TollAspect tollAspect = new TollAspect();
        XMLReader xmlReader = new XMLReader();
        xmlReader.loadConfig("src/main/java/AOP/AopConfig.xml");

        TollGate tollGate = new TollGate(ioContainer, tollAspect, xmlReader);
        try {
            tollGate.charge();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

