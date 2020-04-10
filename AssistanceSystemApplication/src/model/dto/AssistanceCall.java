package model.dto;
import rss.model.FeedMessage;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AssistanceCall implements Serializable {
    private Integer id;
    private String name;
    private LocalDateTime datetime;
    private String location;
    private String description;
    private String urlPicture;
    private String author;
    private String phone;
    private boolean isBlocked;
    private boolean isReported;
    private Integer categoryId;

    public AssistanceCall() {
    }

    public AssistanceCall(Integer id, String name, LocalDateTime datetime, String location, String description, String urlPicture, String author, String phone, boolean isBlocked, boolean isReported, Integer categoryId) {
        this.id = id;
        this.name = name;
        this.datetime = datetime;
        this.location = location;
        this.description = description;
        this.urlPicture = urlPicture;
        this.author = author;
        this.phone = phone;
        this.isBlocked = isBlocked;
        this.isReported = isReported;
        this.categoryId = categoryId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlPicture() {
        return urlPicture;
    }

    public void setUrlPicture(String urlPicture) {
        this.urlPicture = urlPicture;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public boolean isReported() {
        return isReported;
    }

    public void setReported(boolean reported) {
        isReported = reported;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "AssistanceCall{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", datetime=" + datetime +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", urlPicture='" + urlPicture + '\'' +
                ", author='" + author + '\'' +
                ", phone='" + phone + '\'' +
                ", isBlocked=" + isBlocked +
                ", isReported=" + isReported +
                ", categoryId=" + categoryId +
                '}';
    }

    public FeedMessage mapToFeedMessage()
    {
        return new FeedMessage(
                name,
                description,
                urlPicture,
                author,
                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss").format(datetime),
                location.split(" ")[0],
                location.split(" ")[1],
                phone
        );
    }
}
