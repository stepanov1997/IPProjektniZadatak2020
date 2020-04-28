package model.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class Notification {
    private Integer id;
    private Integer User_id;
    private Integer Post_id;
    private LocalDateTime dateTime;

    public Notification() {
    }

    public Notification(Integer id, Integer user_id, Integer post_id, LocalDateTime dateTime) {
        this.id = id;
        User_id = user_id;
        Post_id = post_id;
        this.dateTime = dateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return User_id;
    }

    public void setUser_id(Integer user_id) {
        User_id = user_id;
    }

    public Integer getPost_id() {
        return Post_id;
    }

    public void setPost_id(Integer post_id) {
        Post_id = post_id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notification)) return false;
        Notification that = (Notification) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getUser_id(), that.getUser_id()) &&
                Objects.equals(getPost_id(), that.getPost_id()) &&
                Objects.equals(getDateTime(), that.getDateTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUser_id(), getPost_id(), getDateTime());
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", User_id=" + User_id +
                ", Post_id=" + Post_id +
                ", dateTime=" + dateTime +
                '}';
    }
}
