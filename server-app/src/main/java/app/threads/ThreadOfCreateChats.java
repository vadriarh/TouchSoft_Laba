package app.threads;

import app.entities.Message;
import app.entities.User;
import app.storage.SocketStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

import static app.utils.MessageConstant.TO_CONNECT;

public class ThreadOfCreateChats implements Runnable {
    private static Logger LOGGER = LogManager.getLogger(ThreadOfCreateChats.class);

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            if (SocketStorage.getCountAgents() != 0 && SocketStorage.getCountClients() != 0) {
                getChatUsers();
            }
        }

    }

    private synchronized void getChatUsers() {
        if(SocketStorage.getActualMapOfUsers()!=null){
            Map<String, User> mapUsers = SocketStorage.getActualMapOfUsers();
            User client = null;
            String clientNameKey=null;
            User agent = null;
            String agentNameKey=null;
            for (Map.Entry<String,User> entry : mapUsers.entrySet()) {
                if (client != null && agent != null) {
                    break;
                }
                User user=entry.getValue();
                String nameKey=entry.getKey();
                String userType=user.getUserType();
                if (client == null && userType.equals("client")&&user.getRecipient()==null) {
                    client = user;
                    clientNameKey=nameKey;
                }
                if (agent == null && userType.equals("agent")&&user.getRecipient()==null) {
                    agent = user;
                    agentNameKey=nameKey;
                }
            }
            SocketStorage.editCountAgents(-1);
            SocketStorage.editCountClients(-1);
            agent.setRecipient(clientNameKey);
            client.setRecipient(agentNameKey);
            Message message = new Message(String.format(TO_CONNECT,agent.getName()));
            client.getConnection().sendMessage(message);
            message = new Message(String.format(TO_CONNECT,client.getName()));
            agent.getConnection().sendMessage(message);
            LOGGER.info("Client "+client.getName()+" and agent "+agent.getName()+" was connected to chat.");
        }
    }

}
