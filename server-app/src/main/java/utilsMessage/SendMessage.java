package utilsMessage;

import io.IO;
import model.User;

import java.net.Socket;

public class SendMessage {

    public static void sendStatus(User toUser, User fromUser, String message) {
        toUser.getPrintWriter().println(fromUser.getName().concat(message));
    }

    public static void sendMessage(User toUser, User fromUser, String message) {
        sendStatus(toUser, fromUser, ": ".concat(message));
    }

    public static void sendMessageSocket(Socket socket, String message){
        IO.getPrintWriterOfSocket(socket).println(message);
    }
}
