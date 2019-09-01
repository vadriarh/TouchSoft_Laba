package threads;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.IO;
import utilsMessage.SendMessage;
import utilsMessage.ServerMessage;
import model.Agent;
import model.Client;
import model.User;
import java.io.*;
import java.net.Socket;

public class ThreadOfConnection implements Runnable {
    private Agent agent;
    private Client client;
    private boolean isCloseChat = false;
    private static Logger LOGGER = LogManager.getLogger(ThreadOfConnection.class);

    ThreadOfConnection(Agent agent, Client client) {
        this.agent = agent;
        this.client = client;
    }

    @Override
    public void run() {
        updateUsersBuffer(agent);
        updateUsersBuffer(client);
        connectChat();
        startChat();
    }

    private void startChat() {
        LOGGER.info(String.format("Create chat between Agent %s and Client %s",agent.getName(),client.getName()));
        while (!isCloseChat) {
            parseMessage(getMessage(client), client);
            parseMessage(getMessage(agent), agent);
        }
        LOGGER.info(String.format("Close chat between Agent %s and Client %s",agent.getName(),client.getName()));
    }


    private void connectChat() {
        SendMessage.sendStatus(client, agent, ServerMessage.WAS_CONNECTED);
        SendMessage.sendStatus(agent, client, ServerMessage.WAS_CONNECTED);
        SendMessage.sendMessageSocket(client.getSocket(), ServerMessage.WELCOME_MESSAGE);
        SendMessage.sendMessageSocket(agent.getSocket(), ServerMessage.WELCOME_MESSAGE);
    }

    private void leaveChat() {
        SendMessage.sendStatus(agent, client, ServerMessage.WAS_DISCONNECTED);
        SendMessage.sendStatus(client, agent, ServerMessage.WAS_DISCONNECTED);
        SendMessage.sendMessageSocket(agent.getSocket(),ServerMessage.WAIT_TO_CONNECT);
        SendMessage.sendMessageSocket(client.getSocket(),ServerMessage.WAIT_TO_CONNECT);
        ThreadOfCheckLists.addListStore(client);
        ThreadOfCheckLists.addListStore(agent);
        isCloseChat = true;

    }

    private void exitChat(User user) {
        if (user.getClass() == Agent.class) {
            SendMessage.sendStatus(client, agent, ServerMessage.WAS_EXITED);
            SendMessage.sendMessageSocket(client.getSocket(),ServerMessage.WAIT_TO_CONNECT);
        } else {
            SendMessage.sendStatus(agent, client, ServerMessage.WAS_EXITED);
            SendMessage.sendMessageSocket(agent.getSocket(),ServerMessage.WAIT_TO_CONNECT);
        }
        ThreadOfCheckLists.addListStore(user);
        isCloseChat = true;
    }


    private boolean isStoredBuffer(User user) {
        return user.getBufferedReader() != null && user.getPrintWriter() != null;
    }

    private void updateUsersBuffer(User user) {
        if (!isStoredBuffer(user)) {
            Socket userSocket = user.getSocket();
            user.setBufferedReader(IO.getBufferedReaderOfSocket(userSocket));
            user.setPrintWriter(IO.getPrintWriterOfSocket(userSocket));
        }
    }

    private String getMessage(User user) {
        String message = null;
        try {
            if (user.getBufferedReader().ready())
                message = user.getBufferedReader().readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }

    private boolean parseMessage(String message, User user) {
        if (message == null) {
            return false;
        }
        if (message.startsWith(ServerMessage.EXIT_COMMAND) || message.startsWith(ServerMessage.LEAVE_COMMAND)) {
            if (message.startsWith(ServerMessage.EXIT_COMMAND)) {
                exitChat(user);
            } else {
                leaveChat();
            }
        }
        if (user.getClass() == Agent.class) {
            SendMessage.sendMessage(client, agent, message);
        } else {
            SendMessage.sendMessage(agent, client, message);
        }
        return true;
    }


}