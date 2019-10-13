package app.messages;

public class ServiceMessage extends InternalMessage {
    public ServiceMessage(String command) {
        action = "SERVICE";
        text = command;
    }
}
