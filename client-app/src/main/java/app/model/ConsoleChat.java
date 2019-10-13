package app.model;

import app.connections.Connection;
import app.connections.SocketConnection;
import app.messages.InternalMessage;
import app.messages.SendMessage;
import app.messages.ServiceMessage;
import app.thread.ThreadOfExternalContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;

public class ConsoleChat {
    private static Logger LOGGER = LogManager.getLogger(ConsoleChat.class);
    private BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
    private static boolean isExited;
    private static Connection<InternalMessage> connection;
    private String host;
    private int port;


    public void initApp() {
        host = "localhost";
        port = 40404;
    }

    public boolean connectToServer() {
        boolean status = false;
        String message;
        try {
            Socket socket = new Socket(host, port);
            connection = new SocketConnection(socket);
            status = true;
            message = "Server connect to host " + host + ":" + port;
            System.out.println("SYSTEM: " + message);
            LOGGER.info(message);
        } catch (IOException e) {
            message = "Server not connected to host " + host + ":" + port + ". " + e.getMessage();
            System.out.println("SYSTEM ERROR: " + message);
            LOGGER.error(message);
        }
        return status;
    }

    public void createThreadOfExternalContext() {
        Thread threadApp = new Thread(new ThreadOfExternalContext());
        threadApp.setName("Thread Of External Context");
        threadApp.setDaemon(true);
        threadApp.start();
    }

    public void startChat() {
        System.out.println("Welcome to Console Chat.");
        while (!isExited) {
            String command;
            try {
                command = consoleReader.readLine();
                if (command.startsWith("/")) {
                    connection.sendContext(new ServiceMessage(command));
                    if (command.equals("/exit")) {
                        isExited = true;
                    }
                } else {
                    connection.sendContext(new SendMessage(command));
                }
            } catch (IOException e) {
                System.out.println("Error to input command");
            }
        }
    }

    public void stopChat() {
        String message;
        try {
            connection.close();
            message = "Server closed";
            System.out.println("SYSTEM: " + message);
            LOGGER.info(message);
        } catch (IOException e) {
            message = "Error to closing server. " + e.getMessage();
            System.out.println("SYSTEM ERROR: " + message);
            LOGGER.error(message);
        }
        message = "Chat stoped";
        System.out.println("SYSTEM: " + message);
        LOGGER.info(message);
        System.out.println("Goodbye");
    }

    public static boolean isExited() {
        return isExited;
    }

    public static void setExited(boolean exited) {
        isExited = exited;
    }

    public static Connection<InternalMessage> getConnection() {
        return connection;
    }

}
