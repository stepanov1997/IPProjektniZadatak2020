package rss.model;

public class FeedMessage {

	String title;
	String description;
    String link; 
    String author;
    String pubDate;
    String xLocation;
    String yLocation;
    String phone;

	public FeedMessage() {
	}

	public FeedMessage(String title, String description, String link, String author, String pubDate, String xLocation, String yLocation, String phone) {
		this.title = title;
		this.description = description;
		this.link = link;
		this.author = author;
		this.pubDate = pubDate;
		this.xLocation = xLocation;
		this.yLocation = yLocation;
		this.phone = phone;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public String getxLocation() {
		return xLocation;
	}

	public void setxLocation(String xLocation) {
		this.xLocation = xLocation;
	}

	public String getyLocation() {
		return yLocation;
	}

	public void setyLocation(String yLocation) {
		this.yLocation = yLocation;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "FeedMessage{" +
				"title='" + title + '\'' +
				", description='" + description + '\'' +
				", link='" + link + '\'' +
				", author='" + author + '\'' +
				", pubDate='" + pubDate + '\'' +
				", xLocation='" + xLocation + '\'' +
				", yLocation='" + yLocation + '\'' +
				", phone='" + phone + '\'' +
				'}';
	}
}
