package app.entities;


import app.interfaces.Connection;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

public class User implements Closeable {
    private String name;
    private String userType;
    private String recipient;
    private Connection connection;


    public User(Socket socket) {
        this.connection = new SocketConnection(socket);
    }

    public String getRecipient() { //return string {userType#userName}
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public Connection getConnection() {
        return connection;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) &&
                Objects.equals(userType, user.userType) &&
                Objects.equals(recipient, user.recipient) &&
                Objects.equals(connection, user.connection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, userType, recipient, connection);
    }

    @Override
    public void close() throws IOException {
        connection.close();
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", userType='" + userType + '\'' +
                ", recipient='" + recipient + '\'' +
                ", connection=" + connection +
                '}';
    }
}
