package model.beans;

import model.dao.AdminUserDao;
import model.dto.User;
import util.SHA1;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.Serializable;
import java.util.List;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@ManagedBean
@ViewScoped
public class AdminUserBean implements Serializable {
    private List<AdminUserBean> users;
    private final AdminUserDao userDao = new AdminUserDao();
    private User user = new User();
    private String numberOfActive = "0";
    private String numberOfRegistered = "0";

    public AdminUserBean() {
    }

    public AdminUserBean(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<AdminUserBean> getUsers() {
        return users;
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

    public void setUsers(List<AdminUserBean> users) {
        this.users = users;
    }

    public void importUsers() {
        users = userDao.getAll().stream().map(e-> {
            AdminUserBean adminUserBean = new AdminUserBean();
            adminUserBean.setUser(e);
            return adminUserBean;
        }).collect(Collectors.toList());
    }

    public String giveAccess() {
        System.out.println("access");
        if (userDao.giveAccess(user)) {
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage("usersForm:giveAccess", new FacesMessage("You successfully gave access to an account "+user.getUsername()));
            return "";
        }
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage("usersForm:giveAccess", new FacesMessage("You unsuccessfully gave access to an account " + user.getUsername()));
        return "";
    }

    public String blockUser() {
        System.out.println("blockUser");
        FacesContext context = FacesContext.getCurrentInstance();
        if (userDao.blockUser(user)) {
            context.addMessage("usersForm:blockUsr", new FacesMessage("You successfully blocked an account."));
        } else {
            context.addMessage("usersForm:blockUsr", new FacesMessage("You unsuccessfully blocked an account."));
        }
        return "";
    }

    public String resetPassword() {
        AdminUserDao adminUserDao = new AdminUserDao();
        String password = userDao.resetPassword(user);
        if (password != null && !password.isBlank()) {
            sentMailToUser(user, password);
            user.setPassword(SHA1.encryptPassword(password));
            adminUserDao.update(user);
            FacesContext context = FacesContext.getCurrentInstance();
            context.addMessage("usersForm:resetPwd", new FacesMessage("Password changed and sent via mail to user."));
            return "";
        }
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage("usersForm:resetPwd", new FacesMessage("Password has not changed."));
        return "";
    }

    private void sentMailToUser(User user, String userPassword) {
        ResourceBundle resourceBundle = PropertyResourceBundle.getBundle("util.Email");
        final String username = resourceBundle.getString("username");
        final String password = resourceBundle.getString("password");

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(user.getEmail())
            );
            message.setSubject("Administrator's application");
            message.setText("Dear "+ user.getName() + " " + user.getSurname() + ","
                    + "\n\n We changed your password!"
                    + "\n Your new password is: " + userPassword
                    + "\n\n Administrator");

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
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
