package interfaces;

import java.io.Closeable;

public interface Connection<MessageType> extends Closeable {
    void sendMessage(MessageType message);
    MessageType getMessage();
    boolean isActive();
}
