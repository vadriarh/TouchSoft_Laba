package main;

import threads.ThreadOfInnerMessages;
import utilsMessage.GetMessage;
import utilsMessage.SendMessage;

import java.io.*;
import java.net.Socket;

public class APPChat {
    private static Socket socket;
    private static boolean isAliveChat = true;

    public static void setIsAliveChat(boolean isAliveChat) {
        APPChat.isAliveChat = isAliveChat;
    }

    public static void main(String[] args) {
        APPChat appChat = new APPChat();
        appChat.getSocket();
        appChat.createThreadOfInnerMessages();
        appChat.startChat();


    }

    private void getSocket() {
        try {
            socket = new Socket("127.0.0.1", 40404);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createThreadOfInnerMessages() {
        Thread threadApp = new Thread(new ThreadOfInnerMessages(socket));
        threadApp.setDaemon(true);
        threadApp.start();
    }

    private void startChat() {
        System.out.println("Console Chat v1.0");
        System.out.println("Enter /register {$client or $agent} {$name}");
        while (socket.isConnected()||isAliveChat) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String message=null;
            message = GetMessage.getSystemMessage();
            parseMessage(message);
        }
        if(!socket.isConnected()){
            System.out.println("Server down.app close.");
        }
    }

    private void parseMessage(String message) {
        if (message.equals("")) return;
        System.out.println("your message: ".concat(message));
        if (message.equalsIgnoreCase("/leave")) {
            System.out.println("Chat leave");
        } else if (message.equalsIgnoreCase("/exit")) {
            System.out.println("App exit");
            isAliveChat = false;
        }
        SendMessage.sendMessageSocket(socket, message);
    }


}
