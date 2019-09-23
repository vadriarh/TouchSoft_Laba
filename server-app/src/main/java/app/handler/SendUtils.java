package app.handler;

import app.interfaces.Connection;
import app.entities.Message;
import app.entities.User;
import app.storage.SocketStorage;

public class SendUtils {
    public static void sendMessage(User user, Message message){
        Connection<Message> userConnection=user.getConnection();

        Message sendMessage=new Message(user.getName()+": "+message.getText());
        userConnection.sendMessage(sendMessage);

        User recipient= SocketStorage.getUserFromMap(user.getRecipient());
        Connection<Message> recipientConnection=recipient.getConnection();
        recipientConnection.sendMessage(sendMessage);
    }

}
