package app.utils;

import app.threads.ThreadOfGettingNetworkConnections;
import app.threads.ThreadOfGettingNetworkMessages;
import app.threads.ThreadOfHandleIncomingMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ThreadUtils {
    private static Logger LOGGER = LogManager.getLogger(ThreadUtils.class);
    private static Thread threadOfGettingNetworkConnections;
    private static Thread threadOfHandleIncomingMessages;
    private static Thread threadOfGettingNetworkMessages;


    private static boolean startThreadOfGettingNetworkConnections() {
        if (threadOfGettingNetworkConnections == null) {
            threadOfGettingNetworkConnections = new Thread(new ThreadOfGettingNetworkConnections());
            threadOfGettingNetworkConnections.setDaemon(true);
            threadOfGettingNetworkConnections.setName("ThreadOfGettingNetworkConnections");
            threadOfGettingNetworkConnections.start();
            return true;
        }
        LOGGER.debug("ThreadOfGettingNetworkConnections been started.");
        return false;
    }

    private static boolean stopThreadOfGettingNetworkConnections() {
        if (threadOfGettingNetworkConnections.isAlive()) {
            threadOfGettingNetworkConnections.interrupt();
            threadOfGettingNetworkConnections = null;
            return true;
        }
        LOGGER.debug("ThreadOfGettingNetworkConnections not started.");
        return false;
    }

    private static boolean startThreadOfHandleIncomingMessages() {
        if (threadOfHandleIncomingMessages == null) {
            threadOfHandleIncomingMessages = new Thread(new ThreadOfHandleIncomingMessages());
            threadOfHandleIncomingMessages.setDaemon(true);
            threadOfHandleIncomingMessages.setName("ThreadOfHandleIncomingMessages");
            threadOfHandleIncomingMessages.start();
            return true;
        }
        LOGGER.debug("Error to start ThreadOfHandleIncomingMessages");
        return false;
    }

    private static boolean stopThreadOfHandleIncomingMessages() {
        if (threadOfHandleIncomingMessages.isAlive()) {
            threadOfHandleIncomingMessages.interrupt();
            threadOfHandleIncomingMessages = null;
            return true;
        }
        LOGGER.debug("ThreadOfHandleIncomingMessages not run");
        return false;
    }

    private static boolean startThreadOfGettingNetworkMessages() {
        if (threadOfGettingNetworkMessages == null) {
            threadOfGettingNetworkMessages = new Thread(new ThreadOfGettingNetworkMessages());
            threadOfGettingNetworkMessages.setDaemon(true);
            threadOfGettingNetworkMessages.setName("ThreadOfGettingNetworkMessages");
            threadOfGettingNetworkMessages.start();
            return true;
        }
        LOGGER.debug("ThreadOfGettingNetworkMessages been started");
        return false;
    }

    private static boolean stopThreadOfGettingNetworkMessages() {
        if (threadOfGettingNetworkMessages.isAlive()) {
            threadOfGettingNetworkMessages.interrupt();
            threadOfGettingNetworkMessages = null;
            return true;
        }
        LOGGER.debug("ThreadOfGettingNetworkMessages not run");
        return false;
    }

    public static boolean startThreads() {
        return ThreadUtils.startThreadOfGettingNetworkConnections()
                && ThreadUtils.startThreadOfHandleIncomingMessages()
                && ThreadUtils.startThreadOfGettingNetworkMessages();
    }

    public static boolean stopThreads() {
        return ThreadUtils.stopThreadOfGettingNetworkConnections()
                && ThreadUtils.stopThreadOfGettingNetworkMessages()
                && ThreadUtils.stopThreadOfHandleIncomingMessages();
    }
}
