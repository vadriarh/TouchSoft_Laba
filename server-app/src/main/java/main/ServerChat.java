package main;

import threads.ThreadOfCheckLists;
import threads.ThreadOfRegistration;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerChat {
    private ServerSocket serverSocket;
    private Socket incomingSocket;
    private boolean isAlive=false;

    public static void main(String[] args) {
        System.out.println("Server Console Chat v1.0");
        ServerChat serverChat=new ServerChat();
        serverChat.startServer();
        serverChat.createThreadStore();
        serverChat.getIncomingSockets();
    }

    private void startServer() {
        if(!isAlive){
            isAlive=true;
            try {
                serverSocket = new ServerSocket(40404);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createThreadStore() {
        Thread checklists=new Thread(new ThreadOfCheckLists());
        checklists.setDaemon(true);
        checklists.start();
    }

    private void getIncomingSockets() {
        while (isAlive) {
            try {
                incomingSocket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
            createThreadRegistration(incomingSocket);
        }
    }

    private void createThreadRegistration(Socket incomingSocket) {
        Thread threadOfRegistration=new Thread(new ThreadOfRegistration(incomingSocket));
        threadOfRegistration.setDaemon(true);
        threadOfRegistration.start();
    }


}
