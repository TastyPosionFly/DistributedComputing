package RPC;

import java.io.Serializable;
import java.util.List;

public class serverInfo implements Serializable {
    private String host;
    private int port;
    private List<String> method;

    // 缓存读取用
    public serverInfo(String host, int port) {
        this.host = host;
        this.port = port;
    }

    // 给服务器端传输信息用
    public serverInfo(String host, int port, List<String> method) {
        this.host = host;
        this.port = port;
        this.method = method;
    }


    public String getHost() {
        return host;
    }
    public int getPort() { return port; }
    public List<String> getMethod() { return method; }
}
