package app.main;

import app.model.ConsoleChat;

public class APPChat {

    public static void main(String[] args) {
        ConsoleChat appChat = new ConsoleChat();
        appChat.initApp();
        if (appChat.connectToServer()) {
            appChat.createThreadOfExternalContext();
            appChat.startChat();
            appChat.stopChat();
        }
    }
}
