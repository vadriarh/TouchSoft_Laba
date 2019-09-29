package websocket;

import interfaces.Connection;
import model.SocketConnection;
import storage.MemoryWebUser;
import utils.MessageUtils;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.net.Socket;

@ServerEndpoint("/chat")
public class ChatEndpoint{
    private final String serverHost = "127.0.0.1";
    private final int serverPort = 40404;

    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        Socket socket = null;
        try {
            socket = new Socket(serverHost, serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Connection<String> connection = new SocketConnection(socket);
        MemoryWebUser.connectionStorage.put(session, connection);
    }

    @OnMessage
    public void onMessage(Session session,String message){
        MemoryWebUser.connectionStorage.get(session).sendMessage(message);
    }

    @OnClose
    public void onClose(Session session){
        String report= MessageUtils.createCloseReport();
        MemoryWebUser.connectionStorage.get(session).sendMessage(report);
    }

    @OnError
    public void onError(Throwable t) {
        System.out.println("onError::" + t.getMessage());
    }

}
