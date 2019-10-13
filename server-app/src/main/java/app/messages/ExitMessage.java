package app.messages;

public class ExitMessage extends InternalMessage {
    public ExitMessage(){
        action="EXIT";
        text="Server disabled";
    }
}
