package app.utils;

import app.handler.MessageHandler;
import app.interfaces.Connection;
import app.entities.Message;
import app.entities.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MessageUtils {

    public static String getConsoleMessage() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String message = null;
        try {
            message = reader.readLine();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return message;
    }

    public static void handleMessageFromUserConnection(User user){
        Connection connection=user.getConnection();
        Message message = (Message) connection.getMessage();
        if(message.getText()!=null){
            MessageHandler.service(user, message);
        }
    }

    public static String createKeyOfTypeAndName(User user){
        String userName=user.getName();
        String userType=user.getUserType();
        String key=userType+"#"+userName;
        return key;
    }

}
