package app.storage;

import app.entities.Message;
import app.entities.User;
import app.interfaces.Connection;
import app.utils.MessageConstant;
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
    private static Set<User> setOfRegistration;
    private static Map<String,User> mapOfUsers;
    private static AtomicLong countOfClients;
    private static AtomicLong countOfAgents;
    private static boolean isActive;

    private static Logger LOGGER = LogManager.getLogger(SocketStorage.class);

    public static void init(){
        setOfRegistration = new CopyOnWriteArraySet<>();
        mapOfUsers=new ConcurrentHashMap<>();
        countOfClients=new AtomicLong();
        countOfAgents=new AtomicLong();
        isActive=true;
    }

    public static boolean isActive() {
        return isActive;
    }

    public static boolean addUser(User user) {
        return setOfRegistration.add(user);
    }

    public static boolean isEmptySetOfRegistration(){
        return setOfRegistration.isEmpty();
    }


    public static boolean addToMapOfUsers(User user){
        String key=user.getUserType()+"#"+user.getName();
        boolean status=false;
        if(!mapOfUsers.containsKey(key)){
            mapOfUsers.put(key,user);
            status=true;
        }
        return status;
    }

    public static void removeUserFromMap(User user){
        String key=user.getUserType()+"#"+user.getName();
        mapOfUsers.remove(key);
    }

    public static void removeUserFromSetRegistration(User user){
        setOfRegistration.remove(user);
    }

    public static Connection getConnection(String key){
        return mapOfUsers.get(key).getConnection();
    }

    public static Map<String,User> getActualMapOfUsers(){
        Map<String,User> getMap=null;
        if(mapOfUsers!=null){
           getMap =new HashMap<>(mapOfUsers);
        }
        return getMap;
    }

    public static void editCountAgents(int number){
        if(number==1){
            countOfAgents.getAndIncrement();
        }else if(number==-1){
            countOfAgents.getAndDecrement();
        }
    }

    public static void editCountClients(int number){
        if(number==1){
            countOfClients.getAndIncrement();
        }else if(number==-1){
            countOfClients.getAndDecrement();
        }
    }

    public static long getCountAgents(){
        return countOfAgents.get();
    }

    public static long getCountClients(){
        return countOfClients.get();
    }

    public static void close() {
        Message message=new Message();
        message.setText(MessageConstant.SERVER_DOWN);
        for (User user:mapOfUsers.values()) {
            user.getConnection().sendMessage(message);
            try {
                user.getConnection().close();
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        }
        closeStorage();
    }
    public static User[] getUsersToRegistration(){
        return setOfRegistration.toArray(new User[0]);
    }

    public static User getUserFromMap(String key){
        return mapOfUsers.get(key);
    }
    private static void closeStorage(){
        setOfRegistration =null;
        mapOfUsers=null;
        countOfClients=null;
        countOfAgents=null;
    }
}
