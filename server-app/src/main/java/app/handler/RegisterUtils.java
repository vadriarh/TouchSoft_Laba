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
        SocketStorage storage=SocketStorage.getInstance();

        Connection<Message> userConnection=user.getConnection();
        if(user.getName()==null){
            String[]partsCommands=text.split(" ");
            if(partsCommands.length>2){
                String userType=partsCommands[1];
                if(userType.equals("client")||userType.equals("agent")){
                    String name=text.replace("/reg "+userType+" ","");
                    user.setUserType(userType);
                    user.setName(name);

                    if(storage.addToMapOfUsers(user)){
                        String regMessage=String.format(REG_SUCCESS,userType,name);
                        userConnection.sendMessage(new Message(regMessage));
                        userConnection.sendMessage(new Message(WAIT_TO_CONNECTION));
                        storage.removeUserFromSetRegistration(user);

                        // maybe more elegantly create a method "SocketStorage.editCount(userType ut, int number)"
                        // the method "editCount" will determine the user type
                        if(userType.equals("client")){
                            storage.editCountClients(1);
                        }else {
                            storage.editCountAgents(1);
                        }
                        LOGGER.info(user.toString()+" was registered.");
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
