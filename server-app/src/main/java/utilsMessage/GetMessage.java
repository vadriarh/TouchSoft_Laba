package utilsMessage;

import io.IO;

import java.io.IOException;
import java.net.Socket;

public class GetMessage {
    public static String getMessageSocket(Socket socket){
        String message=null;
        try {
            if (IO.getBufferedReaderOfSocket(socket).ready()){
                message=IO.getBufferedReaderOfSocket(socket).readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }

    public static String getSystemMessage() {
        String message = null;
        try {
            message = IO.getBufferedReaderOfSystem().readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }
}
