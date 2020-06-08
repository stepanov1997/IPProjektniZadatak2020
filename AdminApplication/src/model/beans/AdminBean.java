package model.beans;

import model.dao.AdministratorDao;
import model.dto.Administrator;
import util.SHA1;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@ManagedBean
@SessionScoped
public class AdminBean implements Serializable {
    private Administrator administrator = new Administrator();

    public AdminBean() {
        administrator.setUsername("");
        administrator.setPassword("");
    }

    public Administrator getAdministrator() {
        return administrator;
    }

    public void setAdministrator(Administrator administrator) {
        this.administrator = administrator;
    }

    public String login() throws IOException {
        boolean success = authenticate(administrator);
        if (success) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage("login-form:login-btn", new FacesMessage("You successfully logged in."));
            return "/admin_home.xhtml?faces-redirect=true";
        }
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage("login-form:login-btn", new FacesMessage("Wrong credentials."));
        return "";
    }

    private boolean authenticate(Administrator a) {
        if(a==null || a.getUsername()==null || a.getPassword()==null || "".equals(a.getUsername()) || "".equals(a.getPassword()))
            return false;
        AdministratorDao administratorDao = new AdministratorDao();
        List<Administrator> admins = administratorDao.getAll();
        Optional<Administrator> first = admins.stream().filter(e -> {
            var ad = e.getUsername().equals(a.getUsername()) &&
                    e.getPassword().equals(SHA1.encryptPassword(a.getPassword()));
            return ad;
        }).findFirst();
        Administrator admin = null;
        if(first.isPresent())
            admin = first.get();
        if(admin==null) return false;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        administrator = admin;
        session.setAttribute("imOnline", this);
        return true;
    }

    public String logout() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        session.setAttribute("imOnline",null);
        return "/login.xhtml?faces-redirect=true";
    }
}
