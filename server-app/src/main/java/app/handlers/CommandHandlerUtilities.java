package app.handlers;

import app.utils.ServerProperty;
import app.utils.ServerStatus;
import app.utils.ThreadUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;

class CommandHandlerUtilities {
    private static Logger LOGGER = LogManager.getLogger(CommandHandlerUtilities.class);
    private ServerProperty property;
    private int serverPort;

    CommandHandlerUtilities() {
        this.property = ServerProperty.getInstance();
        this.serverPort = property.getServerPort();
    }

    void init(String command) {
        property = ServerProperty.getInstance();
        if (!ServerStatus.isAlive()) {
            serverPort = property.getServerPort();
            if (!command.equals("/init")) {
                if (command.startsWith("/init port:")) {
                    command = command.replaceFirst("/init port:", "");
                    try {
                        serverPort = Integer.parseInt(command);
                        ServerStatus.setIsInit(true);
                    } catch (NumberFormatException e) {
                        LOGGER.error("Incorrect number port");
                    }
                } else {
                    LOGGER.error("Incorrect prefix. Prefix will been /init port:{$number}");
                }
            } else {
                ServerStatus.setIsInit(true);
            }
        } else {
            LOGGER.error("Init denied. Server is run");
        }
    }

    void startServer() {
        if (!ServerStatus.isAlive()) {
            if (ServerStatus.isInit()) {
                try {
                    ServerStatus.setIsAlive(true);
                    property.initServerSocket(serverPort);
                    if(ThreadUtils.startThreads()){
                        LOGGER.debug("All threads is run");
                    }
                    LOGGER.debug("Server is run.");
                } catch (IOException e) {
                    LOGGER.error(e.getMessage());
                }

            } else {
                LOGGER.error("Error. Server not initialized");
            }
        } else {
            LOGGER.error("Error. Server been started");
        }

    }

    void stopServer() {
        ServerSocket serverSocket = property.getServerSocket();
        if (ServerStatus.isAlive()) {
            ServerStatus.setIsAlive(false);
            try {
                if(ThreadUtils.stopThreads()){
                    LOGGER.debug("All threads stoped");
                    serverSocket.close();
                }else{
                    LOGGER.error("Error to stoping threads");
                }
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
            if(serverSocket.isClosed()){
                property.getStorage().close(); //erase storage
                LOGGER.debug("Server stoped.");
            }else{
                LOGGER.error("Error to stoping server");
            }
        } else {
            LOGGER.debug("Server not started");
        }
    }

    void exit() {
        stopServer();
        LOGGER.info("Server APP exit. Goodbye.");
        ServerStatus.setIsExit(true);
    }

    void help() {
        System.out.println("list of commands:");
        System.out.println("\t/init - initialization server on default port(40404)");
        System.out.println("\t/init port:#{number} - init port in manual mode");
        System.out.println("\t/start - start server");
        System.out.println("\t/stop - stop and erase server");
        System.out.println("\t/status - server status");
        System.out.println("\t/exit - exit server application.");
    }

    void wrongCommand() {
        System.out.println("Incorrect command");
    }
}
