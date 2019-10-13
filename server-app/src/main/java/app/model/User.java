package app.model;

import java.io.Closeable;
import java.io.IOException;

public abstract class User<MessageType> implements Closeable {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract void sendMessage(MessageType incomingMessage);

    public abstract MessageType getMessage() throws IOException;
}
