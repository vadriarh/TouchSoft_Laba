package web.interfaces;

import java.io.Closeable;
import java.io.IOException;

public interface Connection<MessageType> extends Closeable {
    void sendMessage(MessageType message);
    MessageType getMessage() throws IOException;
    boolean isActive();
}
