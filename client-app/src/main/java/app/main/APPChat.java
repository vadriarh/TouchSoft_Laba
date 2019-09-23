package app.main;

import app.interfaces.Connection;
import app.interfaces.Converter;
import app.entities.JsonConverter;
import app.entities.Message;
import app.entities.SocketConnection;
import app.thread.ThreadOfInnerMessages;
import app.utils.MessageUtils;

import java.io.IOException;
import java.net.Socket;


public class APPChat {
    private Socket socket;
    private Converter converter;
    private boolean isExited;
    private Thread threadApp;
    private Connection connection;

    public static void main(String[] args) {
        APPChat appChat = new APPChat();
        if (appChat.initSocket()) {
            appChat.createThreadOfInnerMessages();
            appChat.startChat();
            appChat.stopChat();
        }

    }

    private boolean initSocket() {
        boolean status = false;
        converter=new JsonConverter();
        try {
            socket = new Socket("127.0.0.1", 40404);
            connection = new SocketConnection(socket,converter);
            status = true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return status;
    }

    private void createThreadOfInnerMessages() {
        threadApp = new Thread(new ThreadOfInnerMessages(this));
        threadApp.setDaemon(true);
        threadApp.setName("Inner app.messages thread");
        threadApp.start();
    }

    private void startChat() {
        System.out.println("Console Chat v1.0");
        while (!isExited) {
            String command = MessageUtils.getConsoleMessage();
            if(command.equals("/exit")||socket.isClosed()){
                isExited=true;
            }
            connection.sendMessage(new Message(command));
        }
    }

    private void stopChat() {
        threadApp.interrupt();
        try {
            connection.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Chat stoped");
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean isExited() {
        return isExited;
    }

    void setExited(boolean exited) {
        isExited = exited;
    }
}
