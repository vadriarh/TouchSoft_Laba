package servlet;

import model.Model;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "/")
public class ChatServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session=request.getSession();
        Model model=Model.getInstance();
        System.out.println(request.getAttribute("text"));
        if(model.checkContainSession(session)){
            String text=request.getParameter("text");
            if(!text.equals("")){
                model.getRemoteUser(session).sendMessage(text);
            }
        }else{
            Model.getInstance().add(session);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
