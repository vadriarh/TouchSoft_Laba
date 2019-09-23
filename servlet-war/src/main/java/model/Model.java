package model;

import entities.RemoteUser;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Model {
    private Map<HttpSession, RemoteUser> httpSessionRemoteUserMap;
    private static Model instance = new Model();

    public static Model getInstance() {
        return instance;
    }

    private Model() {
        httpSessionRemoteUserMap = new ConcurrentHashMap<>();
    }

    public void add(HttpSession session) throws IOException {
        httpSessionRemoteUserMap.put(session,new RemoteUser());
    }

    public boolean checkContainSession(HttpSession session){
        return httpSessionRemoteUserMap.containsKey(session);
    }

    public RemoteUser getRemoteUser(HttpSession session){
        return httpSessionRemoteUserMap.get(session);
    }

}
