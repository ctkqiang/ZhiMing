package xin.ctkqiang.model;

public enum HttpRequestMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    HEAD("HEAD"),
    OPTIONS("OPTIONS"),
    PATCH("PATCH"),
    TRACE("TRACE"),
    CONNECT("CONNECT"),
    UNKNOWN("UNKNOWN");

    private final String value;

    HttpRequestMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
