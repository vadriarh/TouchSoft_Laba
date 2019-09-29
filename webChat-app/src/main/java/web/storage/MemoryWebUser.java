package web.storage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import web.interfaces.Connection;
import web.model.SocketConnection;
import web.utils.MessageUtils;
import web.utils.WebProperty;

import javax.websocket.Session;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryWebUser {
    private static Logger LOGGER = LogManager.getLogger(MemoryWebUser.class);
    private ConcurrentHashMap<Session, Connection<String>> connectionStorage;
    private static MemoryWebUser instance;
    private WebProperty property;

    private MemoryWebUser(){
        connectionStorage=new ConcurrentHashMap<>();
        property=WebProperty.getInstance();
        LOGGER.debug("Storage MemoryWebUser initialized");
    }

    public HashMap<Session,Connection<String>> getMapConnection(){
        return new HashMap<>(connectionStorage);
    }

    public void addConnection(Session session){
        String serverHost = property.getServerHost();
        int serverPort = property.getServerPort();
        Connection<String> connection=null;
        try {
            Socket socket = new Socket(serverHost, serverPort);
            connection = new SocketConnection(socket);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        if(connection==null){
            LOGGER.error("Error creating connection to session "+session);
        }else{
            connectionStorage.put(session,connection);
            LOGGER.info("Create connection "+connection+" to session "+session);
        }

    }


    public void sendMessageFromSession(Session session,String message){
        connectionStorage.get(session).sendMessage(message);
    }

    public void closeCurrentSession(Session session){
        String report= MessageUtils.createCloseReport();
        connectionStorage.get(session).sendMessage(report);
        connectionStorage.remove(session);
        LOGGER.info("Close connection to session "+session);
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

    public void registerAndLogError(Session session,Throwable throwable){
        LOGGER.error("WebSocket error: " +throwable.getMessage()+" from session "+session );
    }

}
