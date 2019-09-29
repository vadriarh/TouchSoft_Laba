package app.handler;

import app.interfaces.Connection;
import app.entities.Message;
import app.entities.User;
import app.storage.SocketStorage;

public class SendUtils {
    public static void sendMessage(User user, Message message){
        SocketStorage storage=SocketStorage.getInstance();
        Connection<Message> userConnection=user.getConnection();
        String userName=user.getName();
        String recipientNameKey=user.getRecipient();
        String getTextFromMessage=message.getText();
        User recipient= storage.getUserFromMap(recipientNameKey);
        Connection<Message> recipientConnection=recipient.getConnection();

        String answerServer=userName+": "+getTextFromMessage;

        Message sendMessage=new Message(answerServer);

        userConnection.sendMessage(sendMessage);
        recipientConnection.sendMessage(sendMessage);
    }

}
