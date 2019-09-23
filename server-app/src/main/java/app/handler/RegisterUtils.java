package app.handler;

import app.interfaces.Connection;
import app.entities.Message;
import app.entities.User;
import app.storage.SocketStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static app.utils.MessageConstant.*;
import static app.utils.MessageConstant.BEEN_REGISTERED;

public class RegisterUtils {
    private static Logger LOGGER = LogManager.getLogger(RegisterUtils.class);
    public static void register(User user, String text){
        User userCopy=new User(user);
        Connection<Message> userConnection=userCopy.getConnection();
        if(userCopy.getName()==null){
            String[]partsCommands=text.split(" ");
            if(partsCommands.length>2){
                String userType=partsCommands[1];
                if(userType.equals("client")||userType.equals("agent")){
                    String name=text.replace("/reg "+userType+" ","");
                    userCopy.setUserType(userType);
                    userCopy.setName(name);

                    if(SocketStorage.addToMapOfUsers(userCopy)){
                        String regMessage=String.format(REG_SUCCESS,userType,name);
                        userConnection.sendMessage(new Message(regMessage));
                        userConnection.sendMessage(new Message(WAIT_TO_CONNECTION));
                        SocketStorage.removeUserFromSetRegistration(user);

                        if(userType.equals("client")){
                            SocketStorage.editCountClients(1);
                        }else {
                            SocketStorage.editCountAgents(1);
                        }
                        LOGGER.info(userCopy.toString()+" was registered.");
                    }else{
                        userConnection.sendMessage(new Message(REG_ERROR));
                        userConnection.sendMessage(new Message(BUSY_NAME));
                        userConnection.sendMessage(new Message(REPEAT_REGISTRATION));
                    }
                }
                else{
                    userConnection.sendMessage(new Message(WRONG_TYPE_USER));
                    userConnection.sendMessage(new Message(REPEAT_REGISTRATION));
                }
            }else{
                userConnection.sendMessage(new Message(REG_ERROR));
                userConnection.sendMessage(new Message(WRONG_COMMAND));
                userConnection.sendMessage(new Message(REPEAT_REGISTRATION));
            }
        }else{
            userConnection.sendMessage(new Message(BEEN_REGISTERED));
        }
    }

}
