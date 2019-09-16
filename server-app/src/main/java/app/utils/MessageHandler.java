package app.utils;

import app.messages.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import app.storage.SocketStorage;

public class MessageHandler {
    private static Logger LOGGER = LogManager.getLogger(MessageHandler.class);
    public static void service(Message message){
        if(message.getStatus()==null) return;
        if(message.getStatus().equals("send")){
            SocketStorage.getConnection(message.getName()).sendMessage(message);
        }else if(message.getStatus().equals("leave")){
            LOGGER.info("The chat between "+message.getName()+" and "+message.getText()+" was over.");
            SocketStorage.getConnection(message.getName()).sendMessage(message);
            SocketStorage.getConnection(message.getText()).sendMessage(message);
            SocketStorage.editCountAgents(1);
            SocketStorage.editCountClients(1);
        }else if(message.getStatus().equals("exit")){
            LOGGER.info("User "+message.getText()+" was exited");
            LOGGER.info("The chat between "+message.getName()+" and "+message.getText()+" was over.");
            message.setStatus("leave");
            SocketStorage.removeUserFromMap(message.getText());
            String keyOfMap=message.getName();
            SocketStorage.getConnection(keyOfMap).sendMessage(message);
            if(keyOfMap.startsWith("client")){
                SocketStorage.editCountClients(1);
            }else if(keyOfMap.startsWith("agent")){
                SocketStorage.editCountAgents(1);
            }
        }
    }
}
