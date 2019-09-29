package app.threads;

import app.entities.Message;
import app.entities.User;
import app.storage.SocketStorage;
import app.utils.MessageConstant;
import app.utils.ServerProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.Socket;

public class ThreadOfServer implements Runnable {
    private ServerProperty property;
    private SocketStorage storage;
    private static Logger LOGGER = LogManager.getLogger(ThreadOfServer.class);

    public ThreadOfServer() {
        property=ServerProperty.getInstance();
        storage=property.getStorage();
    }
    @Override
    public void run() {
        LOGGER.debug("All threads is run");
        Socket incomingSocket;
        while (!Thread.currentThread().isInterrupted()) {
            incomingSocket = null;
            try {
                incomingSocket = property.getServerSocket().accept();
                LOGGER.info("Get inner connection"+incomingSocket);
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
            User newUser=new User(incomingSocket);
            String invitationReport=MessageConstant.INVITATION_TO_REGISTER;
            newUser.getConnection().sendMessage(new Message(invitationReport));
            storage.addUser(newUser);
        }
    }
}
