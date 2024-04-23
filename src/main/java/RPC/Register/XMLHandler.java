package RPC.Register;

import RPC.serverInfo;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLHandler {
    public static Map<String, List<serverInfo>> loadConfig(String filePath) {
        Map<String, List<serverInfo>> remoteMethod = new HashMap<>();
        try {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();

            NodeList services = doc.getElementsByTagName("service");
            for (int i=0; i<services.getLength(); i++) {
                Element serviceElement = (Element) services.item(i);
                String serviceName = serviceElement.getAttribute("name");
                remoteMethod.put(serviceName, new ArrayList<>());

                NodeList serverList = serviceElement.getElementsByTagName("server");
                for (int j=0; j<serverList.getLength(); j++) {
                    Element serverElement = (Element) serverList.item(j);
                    String ip = serverElement.getElementsByTagName("ip").item(0).getTextContent();
                    int port = Integer.parseInt(serverElement.getElementsByTagName("port").item(0).getTextContent());
                    remoteMethod.get(serviceName).add(new serverInfo(ip, port));
                }
            }

            return remoteMethod;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static synchronized void writeConfig(String filePath, Map<String, List<serverInfo>> remoteMethod) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbFactory.newDocumentBuilder();
            Document doc = documentBuilder.newDocument();

            Element root = doc.createElement("services");
            doc.appendChild(root);

            for (Map.Entry<String, List<serverInfo>> entry : remoteMethod.entrySet()) {
                String serviceName = entry.getKey();
                List<serverInfo> servers = entry.getValue();

                Element serviceElement = doc.createElement("service");
                serviceElement.setAttribute("name", serviceName);
                root.appendChild(serviceElement);

                for (serverInfo server : servers) {
                    Element serverElement = doc.createElement("server");
                    serviceElement.appendChild(serverElement);

                    Element ipElement = doc.createElement("ip");
                    ipElement.appendChild(doc.createTextNode(server.getHost()));
                    serverElement.appendChild(ipElement);

                    Element portElement = doc.createElement("port");
                    portElement.appendChild(doc.createTextNode(String.valueOf(server.getPort())));
                    serverElement.appendChild(portElement);
                }


                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer transformer = tf.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new File(filePath));
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.transform(source, result);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void loadTest() {
        Map<String, List<serverInfo>> remoteMethod = XMLHandler.loadConfig("src/main/java/RPC/Register/config.xml");
        for (Map.Entry<String, List<serverInfo>> entry : remoteMethod.entrySet()) {
            String serviceName = entry.getKey();
            System.out.println(serviceName);
            List<serverInfo> servers = entry.getValue();
            for (serverInfo server : servers) {
                System.out.println(server.getHost() + ":" + server.getPort());
            }
        }
    }

    @Test
    public void writeTest(){
        Map<String, List<serverInfo>> remoteMethod = XMLHandler.loadConfig("src/main/java/RPC/Register/config.xml");
        remoteMethod.put("world", new ArrayList<>());
        remoteMethod.get("world").add(new serverInfo("127.0.0.1", 8080));
        remoteMethod.get("world").add(new serverInfo("127.0.0.1", 8081));
        XMLHandler.writeConfig("src/main/java/RPC/Register/config.xml", remoteMethod);
    }
}


