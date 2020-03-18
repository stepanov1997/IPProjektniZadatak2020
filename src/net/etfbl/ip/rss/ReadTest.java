package net.etfbl.ip.rss;

public class ReadTest {
	public static void main(String[] args) {
		RSSFeedParser parser = new RSSFeedParser("http://www.b92.net/info/rss/kultura.xml");
		Feed feed = parser.readFeed();
		System.out.println(feed);
		for (FeedMessage message : feed.getMessages()) {
			System.out.println(message);

		}
	}
}
