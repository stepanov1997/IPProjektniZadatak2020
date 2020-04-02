package model.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class Comment {
    private LocalDateTime dateTime;
    private String comment;
    private Integer Post_id;
    private Integer User_id;
    private Integer Picture_id;

    public Comment() {
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getPost_id() {
        return Post_id;
    }

    public void setPost_id(Integer post_id) {
        Post_id = post_id;
    }

    public Integer getUser_id() {
        return User_id;
    }

    public void setUser_id(Integer user_id) {
        User_id = user_id;
    }

    public Integer getPicture_id() {
        return Picture_id;
    }

    public void setPicture_id(Integer picture_id) {
        Picture_id = picture_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment comment1 = (Comment) o;
        return Objects.equals(getDateTime(), comment1.getDateTime()) &&
                Objects.equals(getComment(), comment1.getComment()) &&
                Objects.equals(getPost_id(), comment1.getPost_id()) &&
                Objects.equals(getUser_id(), comment1.getUser_id()) &&
                Objects.equals(getPicture_id(), comment1.getPicture_id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDateTime(), getComment(), getPost_id(), getUser_id(), getPicture_id());
    }

    @Override
    public String toString() {
        return "Comment{" +
                "dateTime=" + dateTime +
                ", comment='" + comment + '\'' +
                ", Post_id=" + Post_id +
                ", User_id=" + User_id +
                ", Picture_id=" + Picture_id +
                '}';
    }
}
