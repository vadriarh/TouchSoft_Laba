package app.messages;

public class SendMessage extends InternalMessage {
    public SendMessage(String command) {
        action = "SEND";
        text = command;
    }
}
