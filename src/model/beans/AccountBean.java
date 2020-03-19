package model.beans;

import model.dao.AccountDao;
import model.dto.Account;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "account")
@SessionScoped
public class AccountBean {
    private Account account = new Account();
    private String usernameKey;
    private String emailKey;
    private AccountDao accountDao = new AccountDao();

    public String getEmailKey() {
        return emailKey;
    }

    public void setEmailKey(String emailKey) {
        this.emailKey = emailKey;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public boolean add()
    {
        return accountDao.add(account)!=-1;
    }

    public void remove()
    {
        accountDao.remove(account);
    }

    public boolean existsByUsername()
    {
        return accountDao.existsByUsername(usernameKey);
    }

    public boolean existsByEmail()
    {
        return accountDao.existsByUsername(usernameKey);
    }


    public String getUsernameKey() {
        return usernameKey;
    }

    public void setUsernameKey(String usernameKey) {
        this.usernameKey = usernameKey;
    }
}
