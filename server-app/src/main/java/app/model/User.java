package app.model;


import java.net.Socket;

public class User {
    private String name;
    private String userType;
    private final Socket socket;
    private final Connection connection;


    public User(Socket socket) {
        this.socket = socket;
        this.connection=new SocketStringConnection(socket);
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
}
