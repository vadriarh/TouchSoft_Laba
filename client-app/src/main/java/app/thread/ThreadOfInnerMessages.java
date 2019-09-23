package app.thread;

import app.interfaces.Connection;
import app.main.APPChat;
import app.entities.Message;

public class ThreadOfInnerMessages implements Runnable {
    private APPChat chat;
    private Connection connection;

    public ThreadOfInnerMessages(APPChat chat) {
        this.chat=chat;
        this.connection=chat.getConnection();
    }

    @Override
    public void run() {
        while (!chat.isExited()&&!Thread.currentThread().isInterrupted()) {
            Message message=(Message)connection.getMessage();
            if(message.getText()!=null){
                System.out.println(message.getText());
            }
        }
    }
}
