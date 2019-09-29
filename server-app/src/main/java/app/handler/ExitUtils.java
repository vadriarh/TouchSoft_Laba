package app.handler;

import app.interfaces.Connection;
import app.entities.Message;
import app.entities.User;
import app.storage.SocketStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import static app.utils.MessageConstant.*;

public class ExitUtils {

    private static Logger LOGGER = LogManager.getLogger(ExitUtils.class);
    public static void exitUser(User user){
        SocketStorage storage=SocketStorage.getInstance();
        String recipientNameKey=user.getRecipient();
        String userName=user.getName();
        String userType=user.getUserType();

        User recipient=storage.getUserFromMap(recipientNameKey);
        String recipientName=recipient.getName();
        Connection<Message> recipientConnection=recipient.getConnection();
        String recipientType=recipient.getUserType();

        String leaveMessage=String.format(USER_LEAVE_CHAT,userType,userName);

        if(recipientNameKey==null){
            storage.removeUserFromMap(user);

            if(userName==null){
                LOGGER.info(UNREGISTERED_EXIT_THE_APP);
            }else{
                LOGGER.info(String.format(EXIT_THE_APP,userType,userName));
            }
        }else{
            recipientConnection.sendMessage(new Message(leaveMessage));
            recipient.setRecipient(null);
            storage.removeUserFromMap(user);

            LOGGER.info(String.format(EXIT_THE_APP,userType,userName));
            if(recipient.getUserType().equals("client")){
                storage.editCountClients(1);
            }else{
                storage.editCountAgents(1);
            }

            try {
                user.close();
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
            LOGGER.info(String.format("Chat between %s %s and %s %s was closed.",
                    userType,userName,recipientType,recipientName));
        }
    }
}
