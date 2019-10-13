package app.handlers;

import app.utils.ServerProperty;
import app.utils.ServerStatus;

public class CommandHandler {
    private CommandHandlerUtilities utilities;

    public CommandHandler() {
        utilities = new CommandHandlerUtilities();
    }

    public void service(String command) {
        if (command.equals("")) return;
        if (command.startsWith("/init")) {
            utilities.init(command);
        } else if (command.equals("/start")) {
            utilities.startServer();
        } else if (command.equals("/stop")) {
            utilities.stopServer();
        } else if (command.equals("/exit")) {
            utilities.exit();
        } else if (command.equals("/?") || command.equals("/help")) {
            utilities.help();
        } else if (command.equals("/status")) {
            System.out.println("Server status:");
            if (ServerStatus.isInit()) {
                int serverPort = ServerProperty.getInstance().getServerPort();
                System.out.println("server is init to port#" + serverPort);
                if (ServerStatus.isAlive()) {
                    System.out.println("server is started");
                } else {
                    System.out.println("server not started");
                }
            } else {
                System.out.println("server not initialization");
            }
        } else {
            utilities.wrongCommand();
        }
    }
}
