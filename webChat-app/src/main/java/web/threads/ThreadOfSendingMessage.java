package web.threads;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import web.connections.Connection;
import web.converters.Converter;
import web.converters.JsonConverter;
import web.messages.InternalMessage;
import web.storage.MemoryWebUser;

import javax.websocket.Session;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ThreadOfSendingMessage implements Runnable{
    private static Logger LOGGER = LogManager.getLogger(MemoryWebUser.class);
    private MemoryWebUser storage=MemoryWebUser.getInstance();

    @Override
    public void run() {
        Converter<InternalMessage,String> converter=new JsonConverter();
        LOGGER.info("Thread of sending messages started");
        while (!Thread.currentThread().isInterrupted()){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
                Thread.currentThread().interrupt();
            }
            if(storage.storageSize()>0){
                HashMap<Session, Connection<InternalMessage>> actualMapConnection=storage.getMapConnection();
                for (Map.Entry<Session,Connection<InternalMessage>> entry:actualMapConnection.entrySet()) {
                    Session currentSession =entry.getKey();
                    Connection<InternalMessage> currentConnection=entry.getValue();
                    try {
                        InternalMessage context=currentConnection.getContext();
                        if(context!=null){
                            currentSession.getBasicRemote().sendText(context.getText());
                            if(context.getAction().equals("EXIT")){
                                storage.closeCurrentSession(currentSession);
                            }
                        }
                    } catch (IOException e) {
                        LOGGER.error(e.getMessage());
                    }
                }
            }
        }
        LOGGER.info("Thread of sending messages was stoped");
    }
}
