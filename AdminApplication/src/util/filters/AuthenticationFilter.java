package util.filters;

import model.beans.AdminBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthenticationFilter implements Filter {

    @Override
    public void init(FilterConfig config) throws ServletException {
        // If you have any <init-param> in web.xml, then you could get them
        // here by config.getInitParameter("name") and assign it as field.
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);

        String pageRequested = request.getRequestURI().toString();
        AdminBean adminBean = (AdminBean)((HttpServletRequest) req).getSession().getAttribute("adminBean");
        try {
            if (session == null) {
                session = request.getSession(true); // will create a new session
                response.sendRedirect("login.xhtml");
            } else if ((adminBean==null || adminBean.getAdministrator()==null) && (!pageRequested.contains("login.xhtml"))) {
                response.sendRedirect("login.xhtml");
            } else {
                chain.doFilter(request, response);
            }
        } catch (Exception e) {
            System.out.println("Error :" + e);
        }
    }

    @Override
    public void destroy() {
    }

}