package app.messages;

public class AnswerMessage extends InternalMessage {
    public AnswerMessage(InternalMessage message){
        action="ANSWER";
        text=message.getText();
    }
}
