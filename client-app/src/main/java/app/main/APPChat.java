package app.main;

import java.io.*;
import java.net.Socket;


public class APPChat {
    private String name;
    private String nameKey;
    private String recipient;
    private boolean isRegistered;
    private boolean isleave;

    private Socket socket;
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
        name="unknown";
        recipient="server";
        try {
            socket = new Socket("127.0.0.1", 40404);
            connection = new SocketStringConnection(socket);
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
        String command = null;
        while (!isExited) {
            command = MessageUtils.getConsoleMessage();
            if(command.equals("/exit")){
                isExited=true;
            }
            sendMessage(command);
        }
    }

    private void stopChat() {
        threadApp.interrupt();
        try {
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Chat stoped");
    }

    void setIsleave(boolean isleave) {
        this.isleave = isleave;
    }


    void setNameKey(String nameKey) {
        this.nameKey = nameKey;
    }

    void setRegistered(boolean registered) {
        isRegistered = registered;
    }

    String getRecipient() {
        return recipient;
    }

    void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    private void sendMessage(String message) {
        if(isRegistered){
            if(message.equals("/leave")){
                message="leave#@"+recipient+"#@"+nameKey;
            }else if(message.equals("/exit")){
                message="exit#@"+recipient+"#@"+nameKey;
            }else if(isleave){
                message="send#@"+recipient+"#@"+message;
            }else{
                System.out.println("Wait to connection");
                return;
            }
        }
        connection.sendMessage(message);
    }

    void setName(String name) {
        this.name = name;
    }

    Connection getConnection() {
        return connection;
    }

    boolean isExited() {
        return isExited;
    }

    void setExited(boolean exited) {
        isExited = exited;
    }
}
