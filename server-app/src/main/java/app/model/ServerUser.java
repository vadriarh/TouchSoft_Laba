package app.model;

import app.connections.Connection;
import app.messages.InternalMessage;

import java.io.IOException;

public class ServerUser extends User<InternalMessage> {
    ServerUser recipient;
    protected Connection<InternalMessage> connection;

    public ServerUser getRecipient() {
        return recipient;
    }

    public void setRecipient(ServerUser recipient) {
        this.recipient = recipient;
    }

    public void setConnection(Connection<InternalMessage> connection) {
        this.connection = connection;
    }

    public Connection<InternalMessage> getConnection() {
        return connection;
    }

    @Override
    public void close() throws IOException {
        connection.close();
    }

    @Override
    public void sendMessage(InternalMessage incomingMessage) {
        connection.sendContext(incomingMessage);
    }

    @Override
    public InternalMessage getMessage() throws IOException {
        return connection.getContext();
    }
}
