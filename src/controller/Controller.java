package controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Controller extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       if(request.getParameter("submit")!=null)
        {
            switch (request.getParameter("controller"))
            {
                case "account":
                {
                    request.getRequestDispatcher("AccountController").forward(request, response);
                }
                break;
                default:
                {

                }
                break;
            }
        }
        else
        {
            request.getSession().setAttribute("resultRegister","");
        }
    }
}
