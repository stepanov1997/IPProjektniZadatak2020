package model.beans;

import model.dao.UserDao;
import model.dto.User;

public class UserBean {
    private User user = new User();
    private String usernameKey;
    private String emailKey;
    private UserDao userDao = new UserDao();

    public String getEmailKey() {
        return emailKey;
    }

    public void setEmailKey(String emailKey) {
        this.emailKey = emailKey;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean add()
    {
        return userDao.add(user)!=-1;
    }

    public void remove()
    {
        userDao.remove(user);
    }

    public boolean existsByUsername()
    {
        return userDao.existsByUsername(usernameKey);
    }

    public boolean existsByEmail()
    {
        return userDao.existsByEmail(usernameKey);
    }


    public String getUsernameKey() {
        return usernameKey;
    }

    public void setUsernameKey(String usernameKey) {
        this.usernameKey = usernameKey;
    }
}
