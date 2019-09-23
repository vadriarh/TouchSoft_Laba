package app.main;

import app.interfaces.Converter;
import app.entities.JsonConverter;
import app.storage.SocketStorage;
import app.utils.MessageUtils;
import app.utils.ThreadUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;


public class Server {
    private boolean isExit;
    private static Logger LOGGER = LogManager.getLogger(Server.class);
    private ServerSocket serverSocket;
    private boolean isInit;
    private boolean isAlive;
    private final int DEFAULT_SERVER_PORT = 40404;
    private int serverPort;
    public static final Converter CONVERTER=new JsonConverter();

    private void init(String command) {
        if (!isAlive) {
            serverPort = DEFAULT_SERVER_PORT;
            if (!command.equals("/init")) {
                if (command.startsWith("/init port:")) {
                    command = command.replaceFirst("/init port:", "");
                    try {
                        serverPort = Integer.parseInt(command);
                        isInit = true;
                    } catch (NumberFormatException e) {
                        LOGGER.error("Incorrect number port");
                    }
                } else {
                    LOGGER.error("Incorrect prefix. Prefix will been /init port:{$number}");
                }
            } else {
                isInit = true;
            }
        } else {
            LOGGER.error("Init denied. Server is run");
        }
    }

    private void startServer() {
        if (!isAlive) {
            if (isInit) {
                SocketStorage.init();
                LOGGER.debug("Server is run.");
                try {
                    serverSocket = new ServerSocket(serverPort);
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
                ThreadUtils.startThreadServer(this);
                isAlive = true;
            } else {
                LOGGER.error("Error. Server not initialized");
            }
        } else {
            LOGGER.error("Error. Server been started");
        }

    }

    private void stopServer() {
        if (isAlive) {
            isAlive = false;
            ThreadUtils.stopThreadOfServer();
            ThreadUtils.stopThreadOfInnerReports();
            ThreadUtils.stopThreadOfCreateChats();
            SocketStorage.close();
            try {
                serverSocket.close();
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
            serverSocket = null;
            LOGGER.debug("Server stoped.");
        } else {
            System.out.println("Server not started");
        }
    }

    private void exit() {
        if (isAlive) {
            System.out.println("Server is run. Press \"yes\" to stop server ond exit. " +
                    "Press another to cancel.");
            if (MessageUtils.getConsoleMessage().equalsIgnoreCase("yes")) {
                stopServer();
            } else {
                return;
            }
        }
        System.out.println("Server APP exit. Goodbye.");
        isExit = true;
    }

    private void help() {
        System.out.println("list of commands:");
        System.out.println("\t/init - initialization server on default port(40404)");
        System.out.println("\t/init port:#{number} - init port in manual mode");
        System.out.println("\t/start - start server");
        System.out.println("\t/stop - stop and erase server");
        System.out.println("\t/status - server status");
        System.out.println("\t/exit - exit server application.");
    }

    void service(String command) {
        if (command.startsWith("/init")) {
            init(command);
        } else if (command.equals("/start")) {
            startServer();
        }  else if (command.equals("/stop")) {
            stopServer();
        } else if (command.equals("/exit")) {
            exit();
        } else if (command.equals("/?") || command.equals("/help")) {
            help();
        } else if (command.equals("/status")) {
            System.out.println("Server status:");
            if (isInit) {
                System.out.println("server is init to port#" + serverPort);
                if (isAlive) {
                    System.out.println("server is started");
                } else {
                    System.out.println("server not started");
                }
            } else {
                System.out.println("server not initialization");
            }
        } else {
            wrongCommand();
        }
    }



    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public boolean isAlive() {
        return isAlive;
    }


    private void wrongCommand() {
        System.out.println("Incorrect command");
    }

    boolean isExit() {
        return isExit;
    }

}

