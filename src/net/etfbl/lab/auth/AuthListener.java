package net.etfbl.lab.auth;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthListener implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws ServletException, IOException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		HttpSession session = request.getSession(false);
		String loginURI = request.getContextPath() + "/login.xhtml";
		String uploadURI = request.getContextPath() + "/upload.xhtml";
		String rssURI = request.getContextPath() + "/rss";

		// smatramo da ako postoji user u sesiji onda moze pristupiti aplikaciji. Za
		// ovaj primjer nisu obradjeni dodatni sigurnosni elementi
		if (loginURI.equals(request.getRequestURI().toString()) 
				|| uploadURI.equals(request.getRequestURI().toString())
				|| rssURI.equals(request.getRequestURI().toString()) 
				|| (session!=null && session.getAttribute("user") != null)) {
			chain.doFilter(request, response);
		} else {
			response.sendRedirect(loginURI);
		}

	}

	@Override
	public void destroy() {

	}

}
