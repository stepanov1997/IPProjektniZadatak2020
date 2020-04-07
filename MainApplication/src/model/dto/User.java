package model.dto;

import java.util.Objects;

public class User {
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
    private boolean isOnline;
    private boolean isEnabled;

    public User() {
    }

    public User(String name, String surname, String username, String password, String email) {
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

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId() == user.getId() &&
                getLoginCounter() == user.getLoginCounter() &&
                isOnline() == user.isOnline() &&
                isEnabled() == user.isEnabled() &&
                Objects.equals(getName(), user.getName()) &&
                Objects.equals(getSurname(), user.getSurname()) &&
                Objects.equals(getUsername(), user.getUsername()) &&
                Objects.equals(getPassword(), user.getPassword()) &&
                Objects.equals(getEmail(), user.getEmail()) &&
                Objects.equals(getPicture_Id(), user.getPicture_Id()) &&
                Objects.equals(getCountry(), user.getCountry()) &&
                Objects.equals(getCountryCode(), user.getCountryCode()) &&
                Objects.equals(getRegion(), user.getRegion()) &&
                Objects.equals(getCity(), user.getCity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getSurname(), getUsername(), getPassword(), getEmail(), getPicture_Id(), getCountry(), getCountryCode(), getRegion(), getCity(), getLoginCounter(), isOnline(), isEnabled());
    }

    @Override
    public String toString() {
        return "User{" +
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
                ", isOnline=" + isOnline +
                ", isEnabled=" + isEnabled +
                '}';
    }
}
