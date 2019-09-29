package storage;

import interfaces.Connection;
import model.SocketConnection;
import utils.MessageUtils;
import utils.WebProperty;

import javax.websocket.Session;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryWebUser {
    private ConcurrentHashMap<Session, Connection<String>> connectionStorage;
    private static MemoryWebUser instance;
    private WebProperty property;

    private MemoryWebUser(){
        connectionStorage=new ConcurrentHashMap<>();
        property=WebProperty.getInstance();
    }

    public HashMap<Session,Connection<String>> getMapConnection(){
        return new HashMap<>(connectionStorage);
    }

    public void addConnection(Session session){
        String serverHost = property.getServerHost();
        int serverPort = property.getServerPort();
        Socket socket = null;
        try {
            socket = new Socket(serverHost, serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Connection<String> connection = new SocketConnection(socket);
        connectionStorage.put(session,connection);
    }


    public void sendMessageFromSession(Session session,String message){
        connectionStorage.get(session).sendMessage(message);
    }

    public void closeCurrentSession(Session session){
        String report= MessageUtils.createCloseReport();
        connectionStorage.get(session).sendMessage(report);
        connectionStorage.remove(session);
    }

    public int storageSize(){
        return connectionStorage.size();
    }

    public static MemoryWebUser getInstance(){
        if(instance==null){
            instance= new MemoryWebUser();
        }
        return instance;
    }

}
