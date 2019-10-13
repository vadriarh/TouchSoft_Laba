package web.servlet;

import web.connections.Connection;
import web.messages.InternalMessage;
import web.storage.MemoryWebUser;
import web.threads.ThreadOfSendingMessage;

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

@WebServlet(name = "ChatServlet", urlPatterns = "/chat")
public class ChatServlet extends HttpServlet {
    private MemoryWebUser userStorage;

    @Override
    public void init(ServletConfig config) throws ServletException {
        Thread threadOfSendingMessage = new Thread(new ThreadOfSendingMessage());
        threadOfSendingMessage.setName("Thread of sending message");
        threadOfSendingMessage.start();
        userStorage = MemoryWebUser.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("/chat.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    public void destroy() {
        HashMap<Session, Connection<InternalMessage>> actualMapConnection = userStorage.getMapConnection();
        for (Session userSession : actualMapConnection.keySet()) {
            try {
                userSession.getBasicRemote().sendText("Server disabled");
            } catch (IOException e) {
                e.printStackTrace();
            }
            userStorage.closeCurrentSession(userSession);
        }
    }
}
