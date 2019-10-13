package app.model;

import app.messages.InternalMessage;

import java.util.ArrayList;
import java.util.List;

public class ClientUser extends ServerUser {
    private List<InternalMessage> listOfMessageHistory;

    public ClientUser() {
        this.listOfMessageHistory = new ArrayList<>();
    }

    public void addMessageToHistory(InternalMessage receivedMessage) {
        if (recipient == null) {
            listOfMessageHistory.add(receivedMessage);
        }
    }

    public List<InternalMessage> getMessageHistory() {
        return listOfMessageHistory;
    }

    public void clearHistoryOfMessages() {
        listOfMessageHistory = new ArrayList<>();
    }

}
