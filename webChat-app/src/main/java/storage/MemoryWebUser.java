package storage;

import interfaces.Connection;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryWebUser {
    public static ConcurrentHashMap<Session, Connection<String>> connectionStorage;

    public static HashMap<Session,Connection<String>> getMapConnection(){
        return new HashMap<>(connectionStorage);
    }

}
