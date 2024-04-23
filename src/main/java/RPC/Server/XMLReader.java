package RPC.Server;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.HashMap;
import java.util.Map;

public class XMLReader {
    public static Map<String, String> loadConfig(String filePath) {
        Map<String, String> methodClassMap = new HashMap<>();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(filePath);
            doc.getDocumentElement().normalize();


            // 获取 <methods> 元素
            Element methodsElement = (Element) doc.getElementsByTagName("methods").item(0);

            // 获取所有 <method> 元素
            NodeList methodNodes = methodsElement.getElementsByTagName("method");
            for (int i = 0; i < methodNodes.getLength(); i++) {
                Element methodElement = (Element) methodNodes.item(i);
                String methodName = methodElement.getAttribute("name");
                String className = methodElement.getTextContent();

                // 将方法名和类名添加到Map中
                methodClassMap.put(methodName, className);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return methodClassMap;
    }
}
