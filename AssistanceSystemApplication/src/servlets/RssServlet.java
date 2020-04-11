package servlets;

import model.dao.AssistanceCallDao;
import rss.model.Feed;
import rss.writer.RSSFeedWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

public class RssServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AssistanceCallDao assistanceCallDao = new AssistanceCallDao();
        Feed rssFeeder = Feed.createStandardFeed();
        assistanceCallDao.getAll().forEach(e -> rssFeeder.getMessages().add(e.mapToFeedMessage()));
        RSSFeedWriter writer = new RSSFeedWriter(rssFeeder);
        byte[] bytes = null;
        try {
            bytes = writer.write();
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setContentType(MediaType.APPLICATION_XML);
        if (bytes != null) {
            response.getOutputStream().write(bytes);
        }
    }
}
