package model;

import java.net.Socket;

public class Client extends User {
    public Client(String name, Socket socket) {
        super(name, socket);
    }
}
