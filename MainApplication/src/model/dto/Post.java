package model.dto;

import com.mysql.cj.conf.ConnectionUrlParser;
import com.rometools.rome.feed.synd.SyndEntry;

import java.sql.Date;
import java.time.LocalDateTime;

public class Post {
    private Integer id;
    private boolean withAttachment;
    private String text;
    private String link;
    private String youtubeLink;
    private LocalDateTime dateTime;
    private Integer User_id = null;
    private Integer Picture_id = null;
    private Integer Video_id = null;
    private String location = null;
    private boolean isEmergency = false;
    private Integer dangerCategory_id = null;

    public Post() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isWithAttachment() {
        return withAttachment;
    }

    public void setWithAttachment(boolean withAttachment) {
        this.withAttachment = withAttachment;
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

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
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

    public Integer getVideo_id() {
        return Video_id;
    }

    public void setVideo_id(Integer video_id) {
        Video_id = video_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isEmergency() {
        return isEmergency;
    }

    public void setEmergency(boolean emergency) {
        isEmergency = emergency;
    }

    public Integer getDangerCategory_id() {
        return dangerCategory_id;
    }

    public void setDangerCategory_id(Integer dangerCategory_id) {
        this.dangerCategory_id = dangerCategory_id;
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

    public static int postCompare(Object o1, Object o2)
    {
        if(!(((o1 instanceof SyndEntry) || (o1 instanceof Post)) && ((o2 instanceof SyndEntry) || (o2 instanceof Post))))
        {
            return 0;
        }
        if(o1 instanceof SyndEntry)
        {
            var entry1 = (SyndEntry)o1;
            if(o2 instanceof SyndEntry)
            {
                var entry2 = (SyndEntry)o2;
                return entry2.getPublishedDate().compareTo(entry1.getPublishedDate());
            }
            else
            {
                var entry2 = (Post)o2;
                return Date.valueOf(entry2.getDateTime().toLocalDate()).compareTo(entry1.getPublishedDate());
            }
        }
        else
        {
            var entry1 = (Post)o1;
            if(o2 instanceof SyndEntry)
            {
                var entry2 = (SyndEntry)o2;
                return entry2.getPublishedDate().compareTo(Date.valueOf(entry1.getDateTime().toLocalDate()));
            }
            else
            {
                var entry2 = (Post)o2;
                return entry2.getDateTime().compareTo(entry1.getDateTime());
            }
        }
    }
}
