package app.storage;

import app.messages.Message;
import app.model.Connection;
import app.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class SocketStorage {
    private static ArrayList<Socket> listOfRegistration;
    private static Map<String,User> mapOfUsers;
    private static AtomicLong countOfClients;
    private static AtomicLong countOfAgents;

    private static Logger LOGGER = LogManager.getLogger(SocketStorage.class);

    public static void init(){
        listOfRegistration = new ArrayList<>();
        mapOfUsers=new HashMap<>();
        countOfClients=new AtomicLong();
        countOfAgents=new AtomicLong();
    }

    public static synchronized boolean addConnection(Socket socket) {
        return listOfRegistration.add(socket);
    }

    public static boolean isEmptyQueue(){
        return listOfRegistration.isEmpty();
    }

    public static synchronized Socket getConnection() {
        Socket socket=listOfRegistration.get(0);
        listOfRegistration.remove(socket);
        return socket;
    }


    public static synchronized boolean addToMapOfUsers(String key,User user){
        boolean status=false;
        if(!mapOfUsers.containsKey(key)){
            mapOfUsers.put(key,user);
            status=true;
        }
        return status;
    }

    public static synchronized void removeUserFromMap(String key){
        mapOfUsers.remove(key);
    }

    public static synchronized Connection getConnection(String key){
        return mapOfUsers.get(key).getConnection();
    }

    public static synchronized Map<String,User> getActualMapOfUsers(){
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
        message.setStatus("server_down");
        message.setName("server");
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
    private static void closeStorage(){
        listOfRegistration =null;
        mapOfUsers=null;
        countOfClients=null;
        countOfAgents=null;
    }
}
