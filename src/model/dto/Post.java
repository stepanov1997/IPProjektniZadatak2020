package model.dto;

import com.mysql.cj.conf.ConnectionUrlParser;

import java.sql.Date;
import java.util.Objects;

public class Post {
    private Integer id;
    private String text;
    private String link;
    private String youtubeLink;
    private Date dateTime;
    private Integer User_id;
    private Integer Picture_id;
    private Integer Video_id;

    public Post() {
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Integer getPicture_id() {
        return Picture_id;
    }

    public void setPicture_id(Integer picture_id) {
        Picture_id = picture_id;
    }

    public Integer getVideo_id() {
        return Video_id;
    }

    public void setVideo_id(Integer video_id) {
        Video_id = video_id;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post)) return false;
        Post post = (Post) o;
        return Objects.equals(getId(), post.getId()) &&
                Objects.equals(getUser_id(), post.getUser_id()) &&
                Objects.equals(getText(), post.getText()) &&
                Objects.equals(getLink(), post.getLink()) &&
                Objects.equals(getPicture_id(), post.getPicture_id()) &&
                Objects.equals(getVideo_id(), post.getVideo_id()) &&
                Objects.equals(getYoutubeLink(), post.getYoutubeLink()) &&
                Objects.equals(getDateTime(), post.getDateTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUser_id(), getText(), getLink(), getPicture_id(), getVideo_id(), getYoutubeLink(), getDateTime());
    }

    public ConnectionUrlParser.Pair<String,String> getContentTypeValue()
    {
        ConnectionUrlParser.Pair<String,String> pair;
        if(link!=null && !link.isBlank())
        {
            return new ConnectionUrlParser.Pair<>("link",link);
        }
        if(youtubeLink!=null && !youtubeLink.isBlank())
        {
            return new ConnectionUrlParser.Pair<>("ytLink",youtubeLink);
        }
        if(Picture_id!=null)
        {
            return new ConnectionUrlParser.Pair<>("picture",String.valueOf(Picture_id));
        }
        if(Video_id!=null)
        {
            return new ConnectionUrlParser.Pair<>("video",String.valueOf(Video_id));
        }
        return null;
    }
}
