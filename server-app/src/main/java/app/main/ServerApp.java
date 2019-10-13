package app.main;
import app.handlers.CommandHandler;
import app.utils.MessageUtils;
import app.utils.ServerStatus;


public class ServerApp {

    public static void main(String[] args) {
        System.out.println("Server Console Chat v1.0");
        CommandHandler handler=new CommandHandler();
        String command;
        while (!ServerStatus.isExit()) {
            System.out.print("COMMAND: ");
            command = MessageUtils.getConsoleMessage();
            handler.service(command);
        }
    }
}
