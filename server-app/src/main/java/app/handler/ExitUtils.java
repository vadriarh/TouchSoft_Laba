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
        Connection<Message> userConnection=user.getConnection();
        if(user.getRecipient()==null){
            SocketStorage.removeUserFromMap(user);
                if(user.getName()==null){
                    LOGGER.info(UNREGISTERED_EXIT_THE_APP);
                }else{
                    LOGGER.info(String.format(EXIT_THE_APP,user.getUserType(),user.getName()));
                }
            }else{
            User recipient=SocketStorage.getUserFromMap(user.getRecipient());
            Connection<Message> recipientConnection=recipient.getConnection();
            String leaveMessage=String.format(USER_LEAVE_CHAT,user.getUserType(),user.getName());
            recipientConnection.sendMessage(new Message(leaveMessage));
            recipient.setRecipient(null);
            SocketStorage.removeUserFromMap(user);
            LOGGER.info(String.format(EXIT_THE_APP,user.getUserType(),user.getName()));
            if(recipient.getUserType().equals("client")){
                SocketStorage.editCountClients(1);
            }else{
                SocketStorage.editCountAgents(1);
            }
            try {
                user.close();
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
            LOGGER.info(String.format("Chat between %s %s and %s %s was closed.",
                    user.getUserType(),user.getName(),recipient.getUserType(),recipient.getName()));
        }
    }
}
