package app.thread;

import app.connections.Connection;
import app.handlers.ExternalContextHandler;
import app.messages.InternalMessage;
import app.messages.ServiceMessage;
import app.model.ConsoleChat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class ThreadOfExternalContext implements Runnable {
    private static Logger LOGGER = LogManager.getLogger(ThreadOfExternalContext.class);
    private Connection<InternalMessage> connection;

    public ThreadOfExternalContext() {
        this.connection = ConsoleChat.getConnection();
    }

    @Override
    public void run() {
        while (!ConsoleChat.isExited()) {
            InternalMessage gettingContext;
            try {
                Thread.sleep(200);
                gettingContext = connection.getContext();
                if (gettingContext != null) {
                    ExternalContextHandler.service(gettingContext);
                }
            } catch (IOException | InterruptedException e) {
                LOGGER.error(e.getMessage());
                ConsoleChat.setExited(true);
            }
        }
    }
}
