package app.handlers;

import app.messages.*;
import app.model.AgentUser;
import app.model.ClientUser;
import app.model.ServerUser;
import app.storage.MemoryStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Map;

import static app.utils.MessageConstant.*;

public class MessageHandler {
    private static ServerUser user;
    private static String userName;
    private static ServerUser recipient;
    private static String recipientName;
    private static String userClass;
    private static String recipientClass;
    private static String text;
    private static InternalMessage message;
    private static MemoryStorage storage = MemoryStorage.getInstance();
    private static Logger LOGGER = LogManager.getLogger(MessageHandler.class);

    public static void service(Map.Entry<ServerUser, InternalMessage> entry) {
        user = entry.getKey();
        recipient=user.getRecipient();
        userName=user.getName();
        message = entry.getValue();
        text=message.getText();
        getRecipientFromMessage();
        String action = message.getAction();
        switch (action) {
            case "SERVICE":
                parseServiceAction();
                break;
            case "SEND":
                sendMessageToRecipient();
                break;
            default:
                break;
        }
    }

    private static String identifyUsersClass(ServerUser user) {
        if (user.getClass() == AgentUser.class) {
            return "Agent";
        } else if (user.getClass() == ClientUser.class) {
            return "Client";
        }
        return null;
    }

    private static void getRecipientFromMessage() {
        if (recipient == null) {
            return;
        }
        recipient = user.getRecipient();
        recipientName=recipient.getName();
        recipientClass=identifyUsersClass(recipient);
    }

    private static void parseServiceAction() {

        if (text.startsWith("/")) {
            if (text.equals("/leave")) {
                leaveUser();
            } else if (text.equals("/exit")) {
                exitUser();
            } else if (text.startsWith("/reg ")) {
                registerUser();
            } else {
                user.sendMessage(new ServerMessage(WRONG_COMMAND));
            }
        }
    }

    private static void leaveUser() {
        if (recipient == null) {
            user.sendMessage(new ServerMessage(CHAT_IS_OFF));
            user.sendMessage(new ServerMessage(LACK_OF_INTERLOCUTORS));
            user.sendMessage(new ServerMessage(WAIT_TO_CONNECTION));
            return;
        }
        leaveChatOperations(user);
        leaveChatOperations(recipient);

        LOGGER.info(String.format(SERVER_INFO_CLOSE_CHAT,
                userClass, userName, recipientClass, recipientName));
    }

    private static void leaveChatOperations(ServerUser leavingUser) {
        String recipientName=leavingUser.getRecipient().getName();
        leavingUser.sendMessage(new CloseChatMessage(recipientName));
        leavingUser.setRecipient(null);
        if(leavingUser.getClass()==AgentUser.class){
            storage.movingUserToFreeUsers(leavingUser);
        }
    }

    private static void exitUser() {
        storage.removeUserFromMap(user);
        if (recipient == null) {
            user.sendMessage(new ServerMessage(SERVER_DOWN));//TODO
            if (userName == null) {
                LOGGER.info(UNREGISTERED_EXIT_THE_APP);
            } else {
                LOGGER.info(String.format(EXIT_THE_APP, userClass, userName));
            }
        } else {
            leaveChatOperations(recipient);
            LOGGER.info(String.format(SERVER_INFO_CLOSE_CHAT,
                    userClass, userName, recipientClass, recipientName));
        }
        try {
            user.close();
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
    }


    private static void sendMessageToRecipient() {
        if (!suitableForSendingMessages()) return;
        SendToRecipientMessage sendingMessage =new SendToRecipientMessage(userName,message);
        user.sendMessage(new AnswerMessage(sendingMessage));
        recipient.sendMessage(sendingMessage);
    }

    private static boolean suitableForSendingMessages() {
        if (userName == null) {
            user.sendMessage(new ServerMessage(NO_REGISTRATION));
            user.sendMessage(new ServerMessage(INVITATION_TO_REGISTER));
            return false;
        } else if (recipient == null) {
            if (user instanceof ClientUser) {
                SendToRecipientMessage sendingMessage =new SendToRecipientMessage(userName,message);
                ((ClientUser) user).addMessageToHistory(sendingMessage);
                user.sendMessage(new ServerMessage(ADDING_MESSAGE_TO_HISTORY));
                storage.movingUserToFreeUsers(user);
            } else {
                user.sendMessage(new ServerMessage(LACK_OF_INTERLOCUTORS));
                user.sendMessage(new ServerMessage(WAIT_TO_CONNECTION));
            }
            return false;
        }
        return true;
    }

    private static void registerUser() {
        if (userName == null) {
            String[] partsCommands = text.split(" ");
            if (partsCommands.length > 2) {
                userClass = partsCommands[1];
                if (userClass.equals("client") || userClass.equals("agent")) {
                    userName = text.replace("/reg " + userClass + " ", "");
                    if (userClass.equals("agent")) {
                        ServerUser registeredUser = new AgentUser();
                        registerOperations(registeredUser);
                    } else {
                        ServerUser registeredUser = new ClientUser();
                        registerOperations(registeredUser);
                    }
                } else {
                    user.sendMessage(new ServerMessage(WRONG_TYPE_USER));
                    user.sendMessage(new ServerMessage(REPEAT_REGISTRATION));
                }
            } else {
                user.sendMessage(new ServerMessage(REG_ERROR));
                user.sendMessage(new ServerMessage(WRONG_COMMAND));
                user.sendMessage(new ServerMessage(REPEAT_REGISTRATION));
            }
        } else {
            user.sendMessage(new ServerMessage(BEEN_REGISTERED));
        }
    }

    private static boolean tryingRecordTheNewUser(ServerUser registerUser) {
        boolean addingToStorage = storage.checkForAddingUserToStorage(registerUser);
        if (addingToStorage) {
            storage.addUserToStorage(registerUser);
            return true;
        } else {
            return false;
        }
    }

    private static void registerOperations(ServerUser registeredUser) {
        registeredUser.setName(userName);
        registeredUser.setConnection(user.getConnection());
        if (tryingRecordTheNewUser(registeredUser)) {
            user.sendMessage(new ServerMessage(String.format(REG_SUCCESS, userClass, userName)));
            LOGGER.info(String.format(SERVER_INFO_REG_SUCCESS, userClass, userName));
            storage.removeUserFromMap(user);
        } else {
            user.sendMessage(new ServerMessage(REG_ERROR));
            user.sendMessage(new ServerMessage(BUSY_NAME));
            user.sendMessage(new ServerMessage(REPEAT_REGISTRATION));
            LOGGER.info(String.format(SERVER_INFO_REG_ERROR, userClass, userName));
        }
    }
}
