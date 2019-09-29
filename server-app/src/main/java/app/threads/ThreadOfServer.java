package app.threads;

import app.main.Server;
import app.entities.Message;
import app.entities.User;
import app.storage.SocketStorage;
import app.utils.MessageConstant;
import app.utils.ThreadUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.Socket;

public class ThreadOfServer implements Runnable {
    private Server server;
    private static Logger LOGGER = LogManager.getLogger(ThreadOfServer.class);

    public ThreadOfServer(Server server) {
        this.server = server;
    }
    @Override
    public void run() {
        ThreadUtils.startThreadOfCreateChats();
        ThreadUtils.startThreadOfInnerReports();
        while (!ThreadUtils.isIsThreadsAlive()){

        }
        LOGGER.debug("All threads is run");
        Socket incomingSocket;
        while (!Thread.currentThread().isInterrupted()) {
            incomingSocket = null;
            try {
                incomingSocket = server.getServerSocket().accept();
                LOGGER.info("Get inner connection"+incomingSocket);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            if (server.isAlive()) {
                User newUser=new User(incomingSocket);
                newUser.getConnection().sendMessage(new Message(MessageConstant.INVITATION_TO_REGISTER));
                SocketStorage.addUser(newUser);
            }
        }
    }
}
