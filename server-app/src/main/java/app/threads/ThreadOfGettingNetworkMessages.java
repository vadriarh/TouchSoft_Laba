package app.threads;

import app.messages.InternalMessage;
import app.model.ServerUser;
import app.storage.MemoryStorage;
import app.utils.ServerProperty;
import app.utils.ServerStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class ThreadOfGettingNetworkMessages implements Runnable {
    private static Logger LOGGER = LogManager.getLogger(ThreadOfGettingNetworkMessages.class);
    private MemoryStorage storage;

    public ThreadOfGettingNetworkMessages() {
        storage = ServerProperty.getInstance().getStorage();
    }

    @Override
    public void run() {
        while (ServerStatus.isAlive()) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage());
                Thread.currentThread().interrupt();
            }

            getMessageFromEachConnection(storage.getSetOfRegistration());
            getMessageFromEachConnection(storage.getSetOfAllUsers());
        }
    }

    private void getMessageFromEachConnection(CopyOnWriteArraySet<ServerUser> serUsers) {
        Set<ServerUser> setOfRemovingUsers = new HashSet<>();
        InternalMessage receivingMessage = null;
        for (ServerUser user : serUsers) {
            try {
                if(user.getConnection().isClosed()){
                    setOfRemovingUsers.add(user);
                }else {
                    receivingMessage = user.getMessage();
                }
            } catch (IOException e) {
                LOGGER.error("Error to connection. User connection is closed", e);
            }
            if(receivingMessage !=null){
                storage.addMessageToQueueForProcessing(user, receivingMessage);
            }
            for (ServerUser removingUser : setOfRemovingUsers
            ) {
                serUsers.remove(removingUser);
            }
        }
    }
}
