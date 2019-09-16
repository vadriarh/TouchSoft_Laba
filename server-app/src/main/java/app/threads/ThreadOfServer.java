package app.threads;

import app.main.Server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import app.storage.SocketStorage;
import app.utils.ThreadUtils;

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
        ThreadUtils.startThreadOfRegistration();
        ThreadUtils.startThreadOfInnerReports();
        while (!ThreadUtils.isIsThreadsAlive()){

        }
        LOGGER.debug("All app.threads is run");
        Socket incomingSocket;
        while (!Thread.currentThread().isInterrupted()) {
            incomingSocket = null;
            try {
                incomingSocket = server.getServerSocket().accept();
                LOGGER.info("Get inner connection");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            if (server.isAlive()) {
                SocketStorage.addConnection(incomingSocket);
            }
        }
    }
}
