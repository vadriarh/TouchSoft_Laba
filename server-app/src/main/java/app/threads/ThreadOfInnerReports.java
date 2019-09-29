package app.threads;

import app.entities.User;
import app.storage.SocketStorage;
import app.utils.MessageUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class ThreadOfInnerReports implements Runnable {
    private static Logger LOGGER = LogManager.getLogger(ThreadOfInnerReports.class);
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            User removeUser=null;
            if(SocketStorage.getUsersToRegistration().length!=0){
                for (User user:SocketStorage.getUsersToRegistration()) {
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
                SocketStorage.removeUserFromSetRegistration(removeUser);
                removeUser=null;
            }
            if(!SocketStorage.getActualMapOfUsers().isEmpty()){
                Map<String, User> actualMapOfUsers = SocketStorage.getActualMapOfUsers();
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
                SocketStorage.removeUserFromMap(removeUser);
            }
        }
    }
}
