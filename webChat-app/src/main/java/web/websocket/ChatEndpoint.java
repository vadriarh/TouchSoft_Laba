package web.websocket;

import web.storage.MemoryWebUser;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/chat")
public class ChatEndpoint{
    private MemoryWebUser storage=MemoryWebUser.getInstance();

    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        storage.addConnection(session);
    }

    @OnMessage
    public void onMessage(Session session,String message){
        storage.sendMessageFromSession(session,message);
    }

    @OnClose
    public void onClose(Session session){
        storage.closeCurrentSession(session);
    }

    @OnError
    public void onError(Session session,Throwable throwable) {
        storage.registerAndLogError(session,throwable);
    }

}
