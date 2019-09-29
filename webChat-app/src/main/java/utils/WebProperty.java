package utils;

public class WebProperty {
    private static WebProperty instance;
    private final String serverHost;
    private final int serverPort;

    public WebProperty(){
        serverHost = "127.0.0.1";
        serverPort = 40404;
    }


    public static WebProperty getInstance() {
        if (instance == null) {
            instance = new WebProperty();
        }
        return instance;
    }

    public int getServerPort() {
        return serverPort;
    }

    public String getServerHost() {
        return serverHost;
    }
}
