package app.handler;

import app.interfaces.Connection;
import app.entities.Message;
import app.entities.User;
import app.storage.SocketStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static app.utils.MessageConstant.*;
import static app.utils.MessageConstant.USER_LEAVE_CHAT;

public class LeaveUtils {
    private static Logger LOGGER = LogManager.getLogger(LeaveUtils.class);
    public static void leaveUser(User user){
        SocketStorage storage=SocketStorage.getInstance();

        String userName=user.getName();
        String userType=user.getUserType();
        Connection<Message> userConnection=user.getConnection();

        String recipientNameKey=user.getRecipient();
        User recipient= storage.getUserFromMap(recipientNameKey);
        String recipientName=recipient.getName();
        String recipientType=recipient.getUserType();
        Connection<Message> recipientConnection=recipient.getConnection();

        String leaveMessage=String.format(USER_LEAVE_CHAT,userType,userName);

        if(user.getRecipient()==null){
            userConnection.sendMessage(new Message(CHAT_IS_OFF));
            userConnection.sendMessage(new Message(LACK_OF_INTERLOCUTORS));
            userConnection.sendMessage(new Message(WAIT_TO_CONNECTION));
        }else{

            recipientConnection.sendMessage(new Message(leaveMessage));

            leaveMessage=String.format(USER_LEAVE_CHAT,"You","");
            userConnection.sendMessage(new Message(leaveMessage));

            user.setRecipient(null);
            recipient.setRecipient(null);

            storage.editCountAgents(1);
            storage.editCountClients(1);

            LOGGER.info(String.format("Chat between %s %s and %s %s was closed.",
                    userType,userName,recipientType,recipientName));
        }
    }
}
