package model;

import java.net.Socket;

public class Agent extends User {
    public Agent(String name, Socket socket) {
        super(name, socket);
    }

}
