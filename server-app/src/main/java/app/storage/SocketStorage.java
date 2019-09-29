package app.storage;

import app.entities.Message;
import app.entities.User;
import app.interfaces.Connection;
import app.utils.MessageConstant;
import app.utils.MessageUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicLong;

public class SocketStorage {
    private static Logger LOGGER = LogManager.getLogger(SocketStorage.class);

    private static SocketStorage instance;

    private Set<User> setOfRegistration;
    private Map<String,User> mapOfUsers;
    private AtomicLong countOfClients;
    private AtomicLong countOfAgents;
    private boolean isActive;

    private SocketStorage(){
        setOfRegistration = new CopyOnWriteArraySet<>();
        mapOfUsers=new ConcurrentHashMap<>();
        countOfClients=new AtomicLong();
        countOfAgents=new AtomicLong();
    }

    public static SocketStorage getInstance(){
        if(instance==null){
            instance=new SocketStorage();
        }
        return instance;
    }


    public boolean isActive() {
        return isActive;
    }

    public boolean addUser(User user) {
        return setOfRegistration.add(user);
    }

    public boolean isEmptySetOfRegistration(){
        return setOfRegistration.isEmpty();
    }


    public boolean addToMapOfUsers(User user){
        String key= MessageUtils.createKeyOfTypeAndName(user);
        boolean status=false;

        if(!mapOfUsers.containsKey(key)&&mapOfUsers!=null){
            mapOfUsers.put(key,user);
            status=true;
        }
        return status;
    }

    public void removeUserFromMap(User user){
        String key=MessageUtils.createKeyOfTypeAndName(user);
        if(mapOfUsers!=null)mapOfUsers.remove(key);
    }

    public void removeUserFromSetRegistration(User user){
        setOfRegistration.remove(user);
    }

    public Connection getConnection(String key){
        return mapOfUsers.get(key).getConnection();
    }

    public Map<String,User> getActualMapOfUsers(){
        Map<String,User> getMap=null;
        if(mapOfUsers!=null){
           getMap =new HashMap<>(mapOfUsers);
        }
        return getMap;
    }

    public void editCountAgents(int number){
        if(number==1){
            countOfAgents.getAndIncrement();
        }else if(number==-1){
            countOfAgents.getAndDecrement();
        }
    }

    public void editCountClients(int number){
        if(number==1){
            countOfClients.getAndIncrement();
        }else if(number==-1){
            countOfClients.getAndDecrement();
        }
    }

    public long getCountAgents(){
        return countOfAgents.get();
    }

    public long getCountClients(){
        return countOfClients.get();
    }

    public void close() {
        Message message=new Message();
        message.setText(MessageConstant.SERVER_DOWN);
        setOfRegistration.addAll(mapOfUsers.values());
        mapOfUsers=new ConcurrentHashMap<>();
        for (User user:setOfRegistration) {
            user.getConnection().sendMessage(message);
            try {
                user.getConnection().close();
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        }
        setOfRegistration=new CopyOnWriteArraySet<>();

    }
    public User[] getUsersToRegistration(){
        return setOfRegistration.toArray(new User[0]);
    }

    public User getUserFromMap(String key){
        return mapOfUsers.get(key);
    }
}
