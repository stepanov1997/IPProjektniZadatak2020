package net.etfbl.lab.beans;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import net.etfbl.lab.dao.KorisnikDAO;
import net.etfbl.lab.dto.Korisnik;

@ManagedBean(name = "userBean")
@SessionScoped
public class UserBean {

	private String username;
	private String password;
	private Korisnik user;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setUser(Korisnik user) {
		this.user = user;
	}

	public Korisnik getUser() {
		return this.user;
	}

	public String login() {
		user = KorisnikDAO.login(username, password);
		if (user != null) {
			HttpSession sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
			sesija.setAttribute("user", user);
			return "pregledObaveza.xhtml?faces-redirect=true";
		}
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage("login-form:login-btn", new FacesMessage("Pogresni parametri"));
		return "";
	}

	public String logout() {
		HttpSession sesija = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		sesija.removeAttribute("user");
		return "login.xhtml?faces-redirect=true";
	}

}
