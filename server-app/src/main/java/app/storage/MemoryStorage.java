package app.storage;

import app.connections.Connection;
import app.messages.ExitMessage;
import app.messages.InternalMessage;
import app.model.AgentUser;
import app.model.ClientUser;
import app.model.ServerUser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArraySet;

public class MemoryStorage {
    private static Logger LOGGER = LogManager.getLogger(MemoryStorage.class);

    private static MemoryStorage instance;

    private CopyOnWriteArraySet<ServerUser> setOfRegistration;
    private CopyOnWriteArraySet<ServerUser> setOfAllUsers;
    private ConcurrentHashMap<String, AgentUser> mapOfFreeAgents;
    private ConcurrentHashMap<String, ClientUser> mapOfFreeClients;
    private ConcurrentHashMap<String, AgentUser> mapOfBusyAgents;
    private ConcurrentHashMap<String, ClientUser> mapOfBusyClients;
    private ConcurrentLinkedQueue<Map.Entry<ServerUser, InternalMessage>> queueOfReceivingMessages;

    private MemoryStorage() {
        setOfRegistration = new CopyOnWriteArraySet<>();
        setOfAllUsers = new CopyOnWriteArraySet<>();
        mapOfFreeAgents = new ConcurrentHashMap<>();
        mapOfFreeClients = new ConcurrentHashMap<>();
        mapOfBusyAgents = new ConcurrentHashMap<>();
        mapOfBusyClients = new ConcurrentHashMap<>();
        queueOfReceivingMessages = new ConcurrentLinkedQueue<>();
        LOGGER.debug("Storage initialized");
    }

    public static MemoryStorage getInstance() {
        if (instance == null) {
            instance = new MemoryStorage();
        }
        return instance;
    }

    public CopyOnWriteArraySet<ServerUser> getSetOfRegistration() {
        return setOfRegistration;
    }

    public CopyOnWriteArraySet<ServerUser> getSetOfAllUsers() {
        return setOfAllUsers;
    }

    public void addMessageToQueueForProcessing(ServerUser user, InternalMessage receivingMessage) {
        if (receivingMessage == null) return;
        Map.Entry<ServerUser, InternalMessage> entry = new AbstractMap.SimpleEntry<>(user, receivingMessage);
        queueOfReceivingMessages.add(entry);
    }

    public ConcurrentLinkedQueue<Map.Entry<ServerUser, InternalMessage>> getQueueOfReceivingMessages() {
        return queueOfReceivingMessages;
    }

    public AgentUser getAgentUser(String name) {
        if (mapOfBusyAgents.containsKey(name)) return mapOfBusyAgents.get(name);
        if (mapOfFreeAgents.containsKey(name)) return mapOfFreeAgents.get(name);
        return null;
    }

    public ClientUser getClientUser(String name) {
        if (mapOfBusyClients.containsKey(name)) return mapOfBusyClients.get(name);
        if (mapOfFreeClients.containsKey(name)) return mapOfFreeClients.get(name);
        return null;
    }

    public void movingUserToFreeUsers(ServerUser user) {
        if (user instanceof AgentUser) {
            moveAgentToFreeMap((AgentUser) user);
        } else if (user instanceof ClientUser) {
            moveClientToFreeMap((ClientUser) user);
        }
    }

    private void moveClientToFreeMap(ClientUser client) {
        mapOfFreeClients.put(client.getName(), client);
        mapOfBusyClients.remove(client.getName());
    }

    private void moveAgentToFreeMap(AgentUser agent) {
        mapOfFreeAgents.put(agent.getName(), agent);
        mapOfBusyAgents.remove(agent.getName());
    }

    public void removeUserFromMap(ServerUser user) {
        String name = user.getName();
        if (name != null) {
            setOfAllUsers.remove(user);
            if (user instanceof AgentUser) {
                mapOfBusyAgents.remove(name);
                mapOfFreeAgents.remove(name);
            } else if (user instanceof ClientUser) {
                mapOfBusyClients.remove(name);
                mapOfFreeClients.remove(name);
            }
        }
        setOfRegistration.remove(user);
    }

    public void addUserToStorage(ServerUser user) {
        setOfAllUsers.add(user);
        if (user instanceof AgentUser) {
            mapOfFreeAgents.put(user.getName(), (AgentUser) user);
        } else if (user instanceof ClientUser) {
            mapOfBusyClients.put(user.getName(), (ClientUser) user);
        }
    }

    public boolean checkForAddingUserToStorage(ServerUser user) {
        String nameUser = user.getName();
        boolean addedToAllUsers = !setOfAllUsers.contains(user);

        boolean addedToUsersMap = false;
        if (user instanceof AgentUser) {
            addedToUsersMap = !mapOfFreeAgents.containsKey(nameUser)
                    && !mapOfBusyAgents.containsKey(nameUser);
        } else if (user instanceof ClientUser) {
            addedToUsersMap = !mapOfFreeClients.containsKey(nameUser)
                    && !mapOfBusyClients.containsKey(nameUser);
        }

        return addedToUsersMap && addedToAllUsers;
    }

    public boolean checkToConnectFreeUsers() {
        return mapOfFreeClients.size() > 0 && mapOfFreeAgents.size() > 0;
    }

    public AgentUser getFreeAgentUser() {
        String key = mapOfFreeAgents.keys().nextElement();
        AgentUser agentUser = mapOfFreeAgents.get(key);
        mapOfFreeAgents.remove(key);
        mapOfBusyAgents.put(key, agentUser);
        return agentUser;
    }

    public ClientUser getFreeClientUser() {
        String key = mapOfFreeClients.keys().nextElement();
        ClientUser clientUser = mapOfFreeClients.get(key);
        mapOfFreeClients.remove(key);
        mapOfBusyClients.put(key, clientUser);
        return clientUser;
    }


    public void addNewUser(ServerUser user) {
        setOfRegistration.add(user);
    }


    public void close() {
        ExitMessage closeMessage = new ExitMessage();
        for (ServerUser user : setOfAllUsers) {
            user.sendMessage(closeMessage);
            try {
                user.close();
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }
}
