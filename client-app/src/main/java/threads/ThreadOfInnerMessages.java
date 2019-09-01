package threads;

import utilsMessage.GetMessage;

import java.net.Socket;

public class ThreadOfInnerMessages implements Runnable {
    private Socket socket;

    public ThreadOfInnerMessages(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        String message;
        while (true) {
            message = GetMessage.getMessageSocket(socket);
            if (message != null){
                System.out.println(message);
            }
        }
    }
}
