package app.threads;

import app.messages.Message;
import app.model.Connection;
import app.model.User;
import app.storage.SocketStorage;
import app.utils.MessageHandler;

import java.util.Map;

public class ThreadOfInnerReports implements Runnable {
    @Override
    public void run() {
        Connection connection;
        while (true) {
            if(SocketStorage.getActualMapOfUsers()!=null){
                Map<String, User> actualMapOfUsers = SocketStorage.getActualMapOfUsers();
                for (Map.Entry<String, User> entry :
                        actualMapOfUsers.entrySet()) {
                    connection=entry.getValue().getConnection();
                    Message message = (Message) connection.getMessage();
                    MessageHandler.service(message);
                }
            }
        }
    }
}
