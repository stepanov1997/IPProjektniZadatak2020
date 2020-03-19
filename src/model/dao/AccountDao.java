package model.dao;

import model.dto.Account;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AccountDao {
    private static List<Account> accounts;

    static {
        accounts = new ArrayList<>(Arrays.asList(new Account("Kristijan", "Stepanov", "kiki", "kiki", "kristijan.stepanov95@gmail.com"),
                new Account("Milan", "Medic", "medo", "medo", "milan.medic@gmail.com"),
                new Account("Gorana", "Golubović", "goca", "goca", "gorana.golubovic@gmail.com"),
                new Account("Milica", "Milakovic", "cimi", "cimi", "milica.milakovic@gmail.com"),
                new Account("Petar", "Mihajlović", "pero", "pero", "petar.mihajlovic@gmail.com")));
    }

    public AccountDao() {
    }

    public void addAccount(Account account) {
        accounts.add(account);
        for (var acc : accounts) {
            System.out.println(acc);
        }
        System.out.println();
    }

    public void removeAccount(Account account) {
        accounts.remove(account);
    }

    public boolean updateAccount(String username, Account account) {
        Account found = findAccount(account.getUsername());
        if (found != null) {
            if (account.getName() != null) account.setName(account.getName());
            if (account.getSurname() != null) account.setName(account.getSurname());
            if (account.getPassword() != null) account.setName(account.getPassword());
            if (account.getEmail() != null) account.setName(account.getEmail());
            return true;
        }
        return false;
    }

    private Account findAccount(String username) {
        if (accounts.stream().anyMatch(e -> e.getUsername().equals(username)))
            return accounts.stream().filter(e -> e.getUsername().equals(username)).findFirst().get();
        return null;
    }

    public boolean existsByUsername(String username)
    {
        return accounts.stream().anyMatch(account -> account.getUsername().equals(username));
    }

    public boolean existsByEmail(String email)
    {
        return accounts.stream().anyMatch(account -> account.getEmail().equals(email));
    }
}
