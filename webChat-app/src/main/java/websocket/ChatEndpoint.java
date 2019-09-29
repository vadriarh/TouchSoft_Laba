package websocket;

import storage.MemoryWebUser;

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
    public void onError(Throwable throwable) {
        System.out.println("onError::" + throwable.getMessage());
    }

}
