package web.connections;

import java.io.Closeable;
import java.io.IOException;

public interface Connection<ContextType> extends Closeable {
    void sendContext(ContextType contextToSend);

    ContextType getContext() throws IOException;

    boolean isClosed();
}
