package app.threads;

import app.messages.Message;
import app.model.Connection;
import app.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import app.storage.SocketStorage;

import java.net.Socket;
import java.util.Map;

public class ThreadOfRegistration implements Runnable {
    private static Logger LOGGER = LogManager.getLogger(ThreadOfRegistration.class);

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            while (!SocketStorage.isEmptyQueue()) {
                Socket socket = SocketStorage.getConnection();
                User preparedUser = new User(socket);
                register(preparedUser);
            }
            if (SocketStorage.getCountAgents() != 0 && SocketStorage.getCountClients() != 0) {
                getChatUsers();
            }
        }

    }

    private void register(User user) {
        Connection connection = user.getConnection();
        connection.sendMessage(new Message("reg","server"));
        boolean isRegister = false;
        String report = null;
        while (!isRegister) {
            while(report==null){
                report = user.getConnection().getReport();
            }
            String[] parseReport = report.split(" ");
            if (parseReport[0].equals("/reg")) {
                String userType = parseReport[1];
                if (userType.equals("client") || userType.equals("agent")) {
                    if (userType.equals("client")) {
                        SocketStorage.editCountClients(1);
                    } else if (userType.equals("agent")) {
                        SocketStorage.editCountAgents(1);
                    }
                    user.setUserType(userType);
                    user.setName(parseReport[2]);
                    String key=user.getUserType()+"#"+user.getName();
                    if (SocketStorage.addToMapOfUsers(key,user)) {
                        connection.sendMessage(new Message("reg_success",key));
                        isRegister = true;
                        LOGGER.info("User "+user.getUserType()+" "+user.getName()+" was registered");
                    } else {
                        connection.sendMessage(new Message("reg_fail", "This user is exists."));
                    }
                } else {
                    connection.sendMessage(new Message("reg_fail", "Invalid user type"));
                }
            } else if (parseReport[0].equals("/exit")) {
                connection.sendMessage(new Message("reg_fail", "Registration cancelled. Goodbye."));
                break;
            } else {
                connection.sendMessage(new Message("reg_fail", "Please repeat registration"));
            }
        }
    }

    private synchronized void getChatUsers() {
        if(SocketStorage.getActualMapOfUsers()!=null){
            Map<String, User> mapUsers = SocketStorage.getActualMapOfUsers();
            User client = null;
            User agent = null;
            for (String name : mapUsers.keySet()) {
                if (client != null && agent != null) {
                    break;
                }
                if (client == null && name.startsWith("client")) {
                    client = mapUsers.get(name);
                }
                if (agent == null && name.startsWith("agent")) {
                    agent = mapUsers.get(name);
                }
            }
            SocketStorage.editCountAgents(-1);
            SocketStorage.editCountClients(-1);
            Message message = new Message("connect","");
            message.setName("agent#"+agent.getName());
            client.getConnection().sendMessage(message);
            message.setName("client#"+client.getName());
            agent.getConnection().sendMessage(message);
            LOGGER.info("Client "+client.getName()+" and agent "+agent.getName()+" was connected to chat.");
        }
    }

}
