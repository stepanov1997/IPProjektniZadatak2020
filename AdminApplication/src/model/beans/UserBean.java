package model.beans;

import model.dao.UserDao;
import model.dto.User;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.List;

@ManagedBean
@ViewScoped
public class UserBean implements Serializable {
    private List<UserBean> users;
    private UserDao userDao = new UserDao();
    private User user = new User();
    private String numberOfActive = "0";
    private String numberOfRegistered = "0";


    public UserBean() {
    }

    public UserBean(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<UserBean> getUsers() {
        return users;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public String getNumberOfActive() {
        return numberOfActive;
    }

    public void setNumberOfActive(String numberOfActive) {
        this.numberOfActive = numberOfActive;
    }

    public String getNumberOfRegistered() {
        return numberOfRegistered;
    }

    public void setNumberOfRegistered(String numberOfRegistered) {
        this.numberOfRegistered = numberOfRegistered;
    }

    public void setUsers(List<UserBean> users) {
        this.users = users;
    }

    public void importUsers() {
        users = userDao.getUsers();
    }

    public String giveAccess() {
        System.out.println("access");
        if (userDao.giveAccess(user)) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage("admin_home:giveAccess", new FacesMessage("You successfully gave access to an account "+user.getUsername()));
            return "";
        }
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage("admin_home:giveAccess", new FacesMessage("You unsuccessfully gave access to an account " + user.getUsername()));
        return "";
    }

    public String blockUser() {
        System.out.println("blockUser");
        FacesContext context = FacesContext.getCurrentInstance();
        if (userDao.blockUser(user)) {
            context.addMessage("admin_home:blockUsr", new FacesMessage("You successfully blocked an account."));
        } else {
            context.addMessage("admin_home:blockUsr", new FacesMessage("You unsuccessfully blocked an account."));
        }
        return "";
    }

    public String resetPassword() {
        System.out.println("reset");
        String password = userDao.resetPassword(user);
        if (password != null && !password.isBlank()) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage("admin_home:resetPwd", new FacesMessage("Password changed to: '" + password + "'"));
            return "";
        }
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage("admin_home:resetPwd", new FacesMessage("Password has not changed."));
        return "";
    }

    public void getNumberOfActiveUsers()
    {
        Integer number = userDao.getNumberOfActiveUsers();
        if(number==null) numberOfActive = String.valueOf(0);
        numberOfActive = String.valueOf(number);
    }

    public void getNumberOfRegistredUsers()
    {
        Integer number = userDao.getNumberOfRegistredUsers();
        if(number==null) numberOfRegistered = String.valueOf(0);
        numberOfRegistered = String.valueOf(number);
    }
}
