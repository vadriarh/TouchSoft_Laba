package threads;

import interfaces.Connection;
import storage.MemoryWebUser;

import javax.websocket.Session;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ThreadOfSendingMessage implements Runnable{
    private MemoryWebUser storage=MemoryWebUser.getInstance();

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
                Thread.currentThread().interrupt();
            }
            if(storage.storageSize()>0){
                HashMap<Session, Connection<String>> actualMapConnection=storage.getMapConnection();
                for (Map.Entry<Session,Connection<String>> entry:actualMapConnection.entrySet()) {
                    Session currentSession =entry.getKey();
                    Connection<String> currentConnection=entry.getValue();
                    String report=currentConnection.getMessage();
                    if(report!=null){
                        try {
                            currentSession.getBasicRemote().sendText(report);
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            }
        }

    }
}
