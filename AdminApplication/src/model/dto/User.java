package model.dto;

import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private String surname;
    private String username;
    private String password;
    private boolean isLogged;
    private boolean isRegistered;

    public User() {
    }

    public User(String name, String surname, String username, String password, boolean isLogged, boolean isRegistered) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.isLogged = isLogged;
        this.isRegistered = isRegistered;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

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

    public boolean isLogged() {
        return isLogged;
    }

    public void setLogged(boolean logged) {
        isLogged = logged;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }
}
