package utilsMessage;

import io.IO;

import java.net.Socket;

public class SendMessage {
    public static void sendMessageSocket(Socket socket, String message){
        IO.getPrintWriterOfSocket(socket).println(message);
    }
}
