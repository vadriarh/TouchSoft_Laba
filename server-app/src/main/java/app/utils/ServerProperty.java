package app.utils;

import app.converters.Converter;
import app.converters.JsonConverter;
import app.messages.InternalMessage;
import app.storage.MemoryStorage;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerProperty {
    private static ServerProperty instance;
    private final int serverPort;
    private ServerSocket serverSocket;
    private final MemoryStorage storage;
    private final Converter<InternalMessage, String> converter;

    private ServerProperty() {
        serverPort = 40404;
        converter = new JsonConverter();
        storage = MemoryStorage.getInstance();
    }

    public static ServerProperty getInstance() {
        if (instance == null) {
            instance = new ServerProperty();
        }
        return instance;
    }

    public int getServerPort() {
        return serverPort;
    }

    public Converter<InternalMessage, String> getConverter() {
        return converter;
    }

    public MemoryStorage getStorage() {
        return storage;
    }

    public void initServerSocket(int port) throws IOException {
        if (serverSocket == null) {
            serverSocket = new ServerSocket(port);
        }
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }
}
