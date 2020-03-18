package net.etfbl.ip.rss;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class WriteTest {

    public static void main(String[] args) {
        // create the rss feed
        String copyright = "Copyright hold by IP2019";
        String title = "RSS test info";
        String description = "RSS test info";
        String language = "en";
        String link = "https://www.etf.unibl.org";
        Calendar cal = new GregorianCalendar();
        Date creationDate = cal.getTime();
        SimpleDateFormat date_format = new SimpleDateFormat(
                "EEE', 'dd' 'MMM' 'yyyy' 'HH:mm:ss' 'Z", Locale.US);
        String pubdate = date_format.format(creationDate);
        Feed rssFeeder = new Feed(title, link, description, language,
                copyright, pubdate);

        // now add one example entry
        FeedMessage feed = new FeedMessage();
        feed.setTitle("RSSFeed");
        feed.setDescription("This is a description");
        feed.setAuthor("IP ETFBL");
        feed.setGuid("https://etf.unibl.org/index.php/sr-RS/novosti/584-ii-2017-20");
        feed.setLink("https://etf.unibl.org/index.php/sr-RS/novosti/584-ii-2017-20");
        rssFeeder.getMessages().add(feed);

        // now write the file
        RSSFeedWriter writer = new RSSFeedWriter(rssFeeder, "articles.rss");
        try {
            writer.write();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
