package app.handler;

import app.interfaces.Connection;
import app.entities.Message;
import app.entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static app.utils.MessageConstant.*;

public class MessageHandler {
    private static Logger LOGGER = LogManager.getLogger(MessageHandler.class);
    public static void service(User user, Message message){
        Connection<Message> userConnection=user.getConnection();
        String textFromMessage=message.getText();

        if(textFromMessage.startsWith("/")){
            if(textFromMessage.equals("/leave")){
                LeaveUtils.leaveUser(user);
            }else if(textFromMessage.equals("/exit")){
                ExitUtils.exitUser(user);
            }else if(textFromMessage.startsWith("/reg ")) {
                RegisterUtils.register(user,textFromMessage);
            }else{
                userConnection.sendMessage(new Message(WRONG_COMMAND));
            }
        }else{
            if(user.getName()==null){
                userConnection.sendMessage(new Message(NO_REGISTRATION));
                userConnection.sendMessage(new Message(INVITATION_TO_REGISTER));
            }else if(user.getRecipient()==null){

                //with this approach, user messages are lost.
                //You must ensure:
                // "Messages sent by the client in the absence of a connection to the agent must not be lost."

                userConnection.sendMessage(new Message(LACK_OF_INTERLOCUTORS));
                userConnection.sendMessage(new Message(WAIT_TO_CONNECTION));
            }else {
                SendUtils.sendMessage(user,message);
            }
        }
    }
}
