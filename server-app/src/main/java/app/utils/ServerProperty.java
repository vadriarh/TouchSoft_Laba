package app.utils;

import app.entities.JsonConverter;
import app.entities.Message;
import app.interfaces.Converter;
import app.storage.SocketStorage;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerProperty {
    private static ServerProperty instance;
    private final int serverPort;
    private ServerSocket serverSocket;
    private final SocketStorage storage;
    private final Converter<Message,String> converter;

    private ServerProperty(){
        serverPort=40404;
        converter=new JsonConverter();
        storage=SocketStorage.getInstance();
    }

    public static ServerProperty getInstance() {
        if(instance==null){
            instance=new ServerProperty();
        }
        return instance;
    }

    public int getServerPort() {
        return serverPort;
    }

    public Converter<Message, String> getConverter() {
        return converter;
    }

    public SocketStorage getStorage() {
        return storage;
    }

    public void initServerSocket(int port) throws IOException {
        if(serverSocket==null){
            serverSocket=new ServerSocket(port);
        }
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }
}
