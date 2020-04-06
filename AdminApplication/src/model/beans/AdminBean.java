package model.beans;

import model.dao.AdministratorDao;
import model.dto.Administrator;

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
            return "ok";
        }
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage("login:login-btn", new FacesMessage("Pogresni parametri"));
        return "";
    }

    private boolean authenticate(Administrator a) {
        AdministratorDao administratorDao = new AdministratorDao();
        List<Administrator> admins = administratorDao.getAll();
        Optional<Administrator> first = admins.stream().filter(e -> e.getUsername().equals(a.getUsername()) &&
                e.getPassword().equals(a.getPassword())).findFirst();
        Administrator admin = null;
        if(first.isPresent())
            admin = first.get();
        if(admin==null) return false;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        administrator = admin;
        session.setAttribute("adminBean", this);
        return true;
    }
}
