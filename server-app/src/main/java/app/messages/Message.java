package app.messages;

public class Message{
    private String name;
    private String status;
    private String text;

    public Message() {
    }

    public Message(String status, String text) {
        this.status = status;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
