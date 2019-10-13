package app.messages;

public class SendToRecipientMessage extends InternalMessage {
    public SendToRecipientMessage(String sender, InternalMessage message) {
        action = "SEND";
        text = sender+": "+message.getText();
    }
}
