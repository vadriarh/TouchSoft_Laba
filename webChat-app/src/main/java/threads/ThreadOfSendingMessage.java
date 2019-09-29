package threads;

import interfaces.Connection;
import storage.MemoryWebUser;

import javax.websocket.Session;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ThreadOfSendingMessage implements Runnable{

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()){
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(MemoryWebUser.connectionStorage.size()>0){
                HashMap<Session, Connection<String>> actualMapConnection=MemoryWebUser.getMapConnection();
                for (Map.Entry<Session,Connection<String>> entry:actualMapConnection.entrySet()) {
                    boolean statusConnection=entry.getValue().isActive();
                    if(statusConnection){
                        String report=entry.getValue().getMessage();
                        if(report!=null){
                            try {
                                entry.getKey().getBasicRemote().sendText(report);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }else{
                        MemoryWebUser.connectionStorage.remove(entry.getKey());
                    }
                }
            }
        }

    }
}
