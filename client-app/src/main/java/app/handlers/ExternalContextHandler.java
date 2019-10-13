package app.handlers;

import app.messages.InternalMessage;
import app.model.ConsoleChat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExternalContextHandler {
    private static Logger LOGGER = LogManager.getLogger(ExternalContextHandler.class);

    public static void service(InternalMessage gettingContext) {
        String action = gettingContext.getAction();
        String text = gettingContext.getText();
        switch (action) {
            case "SEND":
                showAndLogMessage(text);
                break;
            case "ANSWER":
                LOGGER.info(text);
                break;
            case "EXIT":
                showAndLogMessage(text);
                ConsoleChat.setExited(true);
                break;
        }
    }

    private static void showAndLogMessage(String text) {
        System.out.println(text);
        LOGGER.info(text);
    }
}
