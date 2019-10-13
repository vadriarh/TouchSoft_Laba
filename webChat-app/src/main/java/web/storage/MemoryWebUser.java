package web.storage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import web.connections.Connection;
import web.connections.SocketConnection;
import web.messages.InternalMessage;
import web.messages.ServiceMessage;
import web.utils.MessageUtils;
import web.utils.WebProperty;

import javax.websocket.Session;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryWebUser {
    private static Logger LOGGER = LogManager.getLogger(MemoryWebUser.class);
    private ConcurrentHashMap<Session, Connection<InternalMessage>> connectionStorage;
    private static MemoryWebUser instance;
    private WebProperty property;

    private MemoryWebUser(){
        connectionStorage=new ConcurrentHashMap<>();
        property=WebProperty.getInstance();
        LOGGER.debug("Storage MemoryWebUser initialized");
    }

    public HashMap<Session, Connection<InternalMessage>> getMapConnection(){
        return new HashMap<>(connectionStorage);
    }

    public void addConnection(Session session){
        String serverHost = property.getServerHost();
        int serverPort = property.getServerPort();
        Connection<InternalMessage> connection=null;
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
        InternalMessage context= MessageUtils.parseAndCreateContext(message);
        connectionStorage.get(session).sendContext(context);
    }

    public void closeCurrentSession(Session session){
        Connection<InternalMessage> sessionConnection=connectionStorage.get(session);
        sessionConnection.sendContext(new ServiceMessage("/exit"));
        try {
            sessionConnection.close();
        } catch (IOException e) {
            LOGGER.error("Error to close connection: "+ session);
        }
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
