package app.threads;

import app.handlers.MessageHandler;
import app.messages.InternalMessage;
import app.messages.ServerMessage;
import app.model.AgentUser;
import app.model.ClientUser;
import app.model.ServerUser;
import app.storage.MemoryStorage;
import app.utils.ServerProperty;
import app.utils.ServerStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Queue;

import static app.utils.MessageConstant.TO_CONNECT;

public class ThreadOfHandleIncomingMessages implements Runnable {
    private static Logger LOGGER = LogManager.getLogger(ThreadOfHandleIncomingMessages.class);
    private MemoryStorage storage;

    public ThreadOfHandleIncomingMessages() {
        storage = ServerProperty.getInstance().getStorage();
    }

    @Override
    public void run() {
        while (ServerStatus.isAlive()) {
            try {
                Thread.sleep(200);

            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage());
            }
            Queue<Map.Entry<ServerUser, InternalMessage>> queueOfMessages = storage.getQueueOfReceivingMessages();
            while (queueOfMessages.size() > 0) {
                Map.Entry<ServerUser, InternalMessage> entry = queueOfMessages.poll();
                MessageHandler.service(entry);

                connectFreeUsersToChat();
            }
        }
    }

    private void connectFreeUsersToChat() {
        if (storage.checkToConnectFreeUsers()) {
            AgentUser agentUser = storage.getFreeAgentUser();
            ClientUser clientUser = storage.getFreeClientUser();
            agentUser.setRecipient(clientUser);
            clientUser.setRecipient(agentUser);
            agentUser.sendMessage(new ServerMessage(String.format(TO_CONNECT, clientUser.getName())));
            for (InternalMessage messageFromClient : clientUser.getMessageHistory()
            ) {
                agentUser.sendMessage(messageFromClient);
            }
            clientUser.clearHistoryOfMessages();
            clientUser.sendMessage(new ServerMessage(String.format(TO_CONNECT, agentUser.getName())));

            LOGGER.info("Client " + clientUser.getName() + " and agent "
                    + agentUser.getName() + " was connected to chat.");
        }
    }
}
