package servlet;

import interfaces.Connection;
import storage.MemoryWebUser;
import threads.ThreadOfSendingMessage;
import utils.MessageUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.Session;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet(name = "ChatServlet" ,urlPatterns="/chat")
public class ChatServlet extends HttpServlet {
private Thread threadOfSendingMessage;
private MemoryWebUser userStorage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        threadOfSendingMessage=new Thread(new ThreadOfSendingMessage());
        threadOfSendingMessage.setName("Thread of sending message");
        threadOfSendingMessage.setDaemon(true);
        threadOfSendingMessage.start();
        MemoryWebUser.connectionStorage=new ConcurrentHashMap<>();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String protocol="ws";
        String hostName="localhost";
        String port="8080";
        String endpoint="/webChat_app_war_exploded/chat";

    String webSocketAddress=String.format("%s://%s:%s%s",protocol,hostName,port,endpoint);
    req.setAttribute("webSocketAddress",webSocketAddress);
    RequestDispatcher requestDispatcher=req.getRequestDispatcher("/chat.jsp");
    requestDispatcher.forward(req,resp);
    }

    @Override
    public void destroy() {
        threadOfSendingMessage.interrupt();
        HashMap<Session, Connection<String>> actualMapConnection=userStorage.getMapConnection();
        String closeReport= MessageUtils.createCloseReport();
        for (Connection<String> connection:actualMapConnection.values()) {
            connection.sendMessage(closeReport);
        }
    }
}
