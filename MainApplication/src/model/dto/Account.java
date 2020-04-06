package model.dto;

import java.util.Objects;

public class Account {
    private int id;
    private String name;
    private String surname;
    private String username;
    private String password;
    private String email;
    private Integer picture_Id = null;
    private String country;
    private String countryCode;
    private String region;
    private String city;
    private int loginCounter;
    private boolean isLogged;
    private boolean isRegistered;

    public Account() {
    }

    public Account(String name, String surname, String username, String password, String email) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPicture_Id() {
        return picture_Id;
    }

    public void setPicture_Id(Integer picture_Id) {
        this.picture_Id = picture_Id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getLoginCounter() {
        return loginCounter;
    }

    public void setLoginCounter(int loginCounter) {
        this.loginCounter = loginCounter;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return getId() == account.getId() &&
                getLoginCounter() == account.getLoginCounter() &&
                isLogged() == account.isLogged() &&
                isRegistered() == account.isRegistered() &&
                Objects.equals(getName(), account.getName()) &&
                Objects.equals(getSurname(), account.getSurname()) &&
                Objects.equals(getUsername(), account.getUsername()) &&
                Objects.equals(getPassword(), account.getPassword()) &&
                Objects.equals(getEmail(), account.getEmail()) &&
                Objects.equals(getPicture_Id(), account.getPicture_Id()) &&
                Objects.equals(getCountry(), account.getCountry()) &&
                Objects.equals(getCountryCode(), account.getCountryCode()) &&
                Objects.equals(getRegion(), account.getRegion()) &&
                Objects.equals(getCity(), account.getCity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getSurname(), getUsername(), getPassword(), getEmail(), getPicture_Id(), getCountry(), getCountryCode(), getRegion(), getCity(), getLoginCounter(), isLogged(), isRegistered());
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", picture_Id=" + picture_Id +
                ", country='" + country + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", region='" + region + '\'' +
                ", city='" + city + '\'' +
                ", loginCounter=" + loginCounter +
                ", isLogged=" + isLogged +
                ", isRegistered=" + isRegistered +
                '}';
    }
}
