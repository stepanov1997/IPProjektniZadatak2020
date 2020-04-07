package controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Controller extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       if(request.getParameter("submit")!=null)
        {
            switch (request.getParameter("controller"))
            {
                case "user":
                {
                    request.getRequestDispatcher("UserController").forward(request, response);
                }
                break;
                case "upload":
                {
                    request.getRequestDispatcher("FileUploadController").forward(request, response);
                }
                break;
                default:
                {
                    response.getOutputStream().println("Hello world");
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
