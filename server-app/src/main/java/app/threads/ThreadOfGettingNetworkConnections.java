package app.threads;

import app.connections.SocketConnection;
import app.messages.ServerMessage;
import app.model.ServerUser;
import app.storage.MemoryStorage;
import app.utils.ServerProperty;
import app.utils.ServerStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.Socket;

import static app.utils.MessageConstant.INVITATION_TO_REGISTER;

public class ThreadOfGettingNetworkConnections implements Runnable {
    private ServerProperty property;
    private MemoryStorage storage;
    private static Logger LOGGER = LogManager.getLogger(ThreadOfGettingNetworkConnections.class);

    public ThreadOfGettingNetworkConnections() {
        property = ServerProperty.getInstance();
        storage = property.getStorage();
    }

    @Override
    public void run() {
        while (ServerStatus.isAlive()) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage());
            }
            Socket incomingSocket;
            try {
                incomingSocket = property.getServerSocket().accept();
                LOGGER.info("Get inner connection" + incomingSocket);
                SocketConnection connection = new SocketConnection(incomingSocket);
                connection.sendContext(new ServerMessage(INVITATION_TO_REGISTER));
                ServerUser user=new ServerUser();
                user.setConnection(connection);
                storage.addNewUser(user);
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }
}
