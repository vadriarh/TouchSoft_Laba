package threads;

import model.Agent;
import model.Client;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utilsMessage.GetMessage;
import utilsMessage.SendMessage;
import utilsMessage.ServerMessage;


import java.net.Socket;

public class ThreadOfRegistration implements Runnable {
    private Socket incomingSocket;
    private static Logger LOGGER = LogManager.getLogger(ThreadOfRegistration.class);

    public ThreadOfRegistration(Socket incomingSocket) {
        this.incomingSocket = incomingSocket;
    }

    @Override
    public void run() {
        registerIncomingSocket();
    }

    private synchronized boolean registerUser(String message, Socket incomingSocket) {
        String[] parseMessage=message.split(" ");
        String typeUser = parseMessage[1];
        String nameUser = parseMessage[2];
        if (typeUser.equalsIgnoreCase("client")) {
            ThreadOfCheckLists.addListStore(new Client(nameUser, incomingSocket));
            LOGGER.info("Create Client "+nameUser);
            return true;
        } else if (typeUser.equalsIgnoreCase("agent")) {
            ThreadOfCheckLists.addListStore(new Agent(nameUser, incomingSocket));
            LOGGER.info("Create Agent "+nameUser);
            return true;
        } else {
            System.out.println(ServerMessage.WRONG_USER);
            return false;
        }
    }

    private void registerIncomingSocket() {
        boolean isSuccessRegistration=false;
        while (!isSuccessRegistration){
            String incomingMessage=null;
            while(incomingMessage==null){
                incomingMessage= GetMessage.getMessageSocket(incomingSocket);
            }
            if (incomingMessage.startsWith(ServerMessage.REGISTRATION_COMMAND)) {
                if(registerUser(incomingMessage, incomingSocket)){
                    SendMessage.sendMessageSocket(incomingSocket,ServerMessage.REGISTERED_AND_WAIT);
                    isSuccessRegistration=true;
                }else{
                    SendMessage.sendMessageSocket(incomingSocket,ServerMessage.ERROR_REGISTRATION);
                }
            } else if(incomingMessage.equalsIgnoreCase(ServerMessage.EXIT_COMMAND)){
                SendMessage.sendMessageSocket(incomingSocket,ServerMessage.CLOSED_CLIENT_APP);
                isSuccessRegistration=true;
            } else{
                SendMessage.sendMessageSocket(incomingSocket,ServerMessage.ERROR_COMMAND);
            }
        }
    }
}
