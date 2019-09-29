package web.threads;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import web.interfaces.Connection;
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
        LOGGER.info("Thread of sending messages started");
        while (!Thread.currentThread().isInterrupted()){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
                Thread.currentThread().interrupt();
            }
            if(storage.storageSize()>0){
                HashMap<Session, Connection<String>> actualMapConnection=storage.getMapConnection();
                for (Map.Entry<Session,Connection<String>> entry:actualMapConnection.entrySet()) {
                    Session currentSession =entry.getKey();
                    Connection<String> currentConnection=entry.getValue();
                    try {
                        String report=currentConnection.getMessage();
                        if(report!=null){
                            currentSession.getBasicRemote().sendText(report);
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
