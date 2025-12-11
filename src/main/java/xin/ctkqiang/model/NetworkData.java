package xin.ctkqiang.model;

import java.util.Map;

public class NetworkData {
    private String host;
    private int port;
    private String param;
    private Map<String, Object> headers;
    private Map<String, Object> body;
    private HttpRequestMethod method;

    public NetworkData() {  }

    public NetworkData(String host) {
        this.host = host;
        this.port = 80;
    }

    public NetworkData(String host, int port, String param, Map<String, Object> headers, Map<String, Object> body, HttpRequestMethod method) {
        this.host = host;
        this.port = port <= 0 ? 80 : port;
        this.param = param;
        this.headers = headers == null ? defaultHeaders() : headers;
        this.body = body;
        this.method = method == null ? HttpRequestMethod.GET : method;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getParam() {
        return param;
    }

    public String getRequestMethod() {
        return method.getValue().toString().toUpperCase();
    }

    public void setParam(String param) {
        this.param = param;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }

    public Map<String, Object> getBody() {
        return body;
    }

    public void setBody(Map<String, Object> body) {
        this.body = body;
    }

    public void setRequestMethod(HttpRequestMethod method) {
        this.method = method;
    }
    
    /**
     * 构造并返回一个包含默认HTTP请求头信息的Map对象
     * 
     * @return Map<String, Object> 包含预设HTTP头信息的键值对集合
     */
    private Map<String, Object> defaultHeaders() {
        Map<String, Object> headers = new java.util.HashMap<>();
        
        headers.put("X-Host", "internal_app.local");
        headers.put("Host", "127.0.0.1");
        headers.put("X-Forwarded-Host", "127.0.0.1");
        
        headers.put("X-Forwarded-For", "127.0.0.1");
        headers.put("X-Real-IP", "127.0.0.1");
        headers.put("X-Custom-IP-Authorization", "127.0.0.1");
        headers.put("X-Originating-IP", "127.0.0.1");
        
        headers.put("Referer", "localhost");
        headers.put("Origin", "localhost");
        
        headers.put("X-Forwarded-Scheme", "http");
        headers.put("X-Scheme", "http");
        headers.put("X-Original-Scheme", "http");
    
        headers.put("X-HTTP-Method-Override", "DELETE");
        headers.put("X-HTTP-Method", "DELETE");
        headers.put("_method", "DELETE");
        
        headers.put("Transfer-Encoding", "chunked");
        headers.put("Content-Length", "0");
        headers.put("Forwarded", "for=127.0.0.1;host=admin");
        headers.put("Connection", "close");

        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        
        return headers;
    }

    @Override
    public String toString() {
        return "NETWORK_DATA [host=" + host + ", port=" + port + ", param=" + param + ", headers=" + headers + ", body=" + body
                + ", defaultHeaders()=" + defaultHeaders() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
                + ", toString()=" + super.toString() + "]";
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }


    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
