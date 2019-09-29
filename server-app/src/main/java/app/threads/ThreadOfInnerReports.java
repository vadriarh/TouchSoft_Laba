package app.threads;

import app.entities.User;
import app.storage.SocketStorage;
import app.utils.MessageUtils;
import app.utils.ServerProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class ThreadOfInnerReports implements Runnable {
    private static Logger LOGGER = LogManager.getLogger(ThreadOfInnerReports.class);
    private SocketStorage storage;

    @Override
    public void run() {
        storage= ServerProperty.getInstance().getStorage();
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            User removeUser=null;
            if(storage.getUsersToRegistration().length!=0){
                for (User user:storage.getUsersToRegistration()) {
                    if(user.getConnection().isActive()){
                        try{
                            MessageUtils.handleMessageFromUserConnection(user);
                        }catch (Exception e){
                            LOGGER.error(e.getMessage());
                        }
                    }else{
                        removeUser=user;
                    }
                }
            }
            if(removeUser!=null){
                LOGGER.info("Miss and remove inactive connection "+removeUser.getConnection());
                storage.removeUserFromSetRegistration(removeUser);
                removeUser=null;
            }
            if(!storage.getActualMapOfUsers().isEmpty()){
                Map<String, User> actualMapOfUsers = storage.getActualMapOfUsers();
                for (User user :actualMapOfUsers.values()) {
                    if(user.getConnection().isActive()){
                        try{
                            MessageUtils.handleMessageFromUserConnection(user);
                        }catch (Exception e){
                            LOGGER.error(e.getMessage());
                        }
                    }else{
                        removeUser=user;
                    }
                }
            }
            if(removeUser!=null){
                LOGGER.info("Miss and remove inactive user"+removeUser);
                storage.removeUserFromMap(removeUser);
            }
        }
    }
}
