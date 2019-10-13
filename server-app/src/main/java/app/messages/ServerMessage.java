package app.messages;

public class ServerMessage extends InternalMessage {
    public ServerMessage(String message) {
        action = "SEND";
        text = "SERVER: "+message;
    }
}
