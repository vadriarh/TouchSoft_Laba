package app.messages;

public class CloseChatMessage extends ServerMessage {
    public CloseChatMessage(String recipient) {
        super(String.format("Chat between you and %s is closed", recipient));
    }
}
