package app.main;
import app.utils.MessageUtils;


public class ServerApp {

    public static void main(String[] args) {
        System.out.println("Server Console Chat v1.0");
        Server server = new Server();
        String command;
        while (!server.isExit()) {
            System.out.print("server: ");
            command = MessageUtils.getConsoleMessage();
            server.service(command);
        }
    }
}
