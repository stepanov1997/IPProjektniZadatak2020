package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import model.beans.AccountBean;
import model.dao.AccountDao;
import model.dao.CommentDao;
import model.dao.PostDao;
import model.dto.Account;
import model.dto.Post;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class PostServletService extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		AccountBean accountBean = ((AccountBean)request.getSession().getAttribute("accountBean"));
		Account account = accountBean.getAccount();
		if(account == null)
		{
			JsonArray jsonArray = new JsonArray();
			response.getOutputStream().print(jsonArray.toString());
			return;
		}
		URL feedUrl = new URL("https://europa.eu/newsroom/calendar.xml_en?field_nr_events_by_topic_tid=151");
		SyndFeedInput input = new SyndFeedInput();
		SyndFeed feed = null;
		try {
			feed = input.build(new InputStreamReader(feedUrl.openStream()));
		} catch (FeedException e) {
			e.printStackTrace();
		}

		List<Object> posts = new ArrayList<>(new ArrayList<SyndEntry>(feed.getEntries()));
		PostDao postDao = new PostDao();
		posts.addAll(postDao.getAll());
		posts.sort(Post::postCompare);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		List<HashMap<String, Object>> result = new ArrayList<>();
		JsonArray jsonArray = new JsonArray();
		Gson gson = new Gson();
		for(Object obj : posts)
		{
			if(obj instanceof SyndEntry)
			{
				SyndEntry feedEntry = (SyndEntry)obj;
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("isRss", true);
				jsonObject.addProperty("title", feedEntry.getTitle());
				jsonObject.addProperty("author", feedEntry.getAuthor());
				jsonObject.addProperty("link", feedEntry.getLink());
				jsonObject.addProperty("description", feedEntry.getDescription().getValue());

				SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
				String dateTime = format.format(feedEntry.getPublishedDate());
				jsonObject.addProperty("date", dateTime);
				jsonArray.add(jsonObject);
			}
			else if(obj instanceof Post)
			{
				Post post = (Post)obj;
				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("isRss", false);
				jsonObject.addProperty("id", post.getId());
				jsonObject.addProperty("text", post.getText());

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
				String dateTime = formatter.format(post.getDateTime());
				jsonObject.addProperty("date", dateTime);

				AccountDao accountDao = new AccountDao();
				Account account1 = accountDao.get(post.getUser_id());
				jsonObject.addProperty("nameSurname", account1.getName() + " " + account1.getSurname() + " (" + account1.getUsername() + ")");
				jsonObject.addProperty("Picture_id", account1.getPicture_Id());
				jsonObject.addProperty("countryCode", account1.getCountryCode());
				var contentType = post.getContentTypeValue();
				if(contentType==null)
					jsonObject.addProperty("contentType", "textOnly");
				else {
					jsonObject.addProperty("contentType", contentType.left);
					jsonObject.addProperty("value", contentType.right);
				}
				JsonArray comments;
				CommentDao commentDao = new CommentDao();
				comments = gson.toJsonTree(commentDao
						.getFromPost(post.getId())
						.stream()
						.map(elem ->
						{
							JsonObject jsonPost = new JsonObject();
							jsonPost.addProperty("User_id", elem.getUser_id());
							jsonPost.addProperty("comment", elem.getComment());
							jsonPost.addProperty("Picture_id", elem.getPicture_id());
							String dateTimeComment = formatter.format(elem.getDateTime());
							jsonPost.addProperty("datetime", dateTimeComment);
							return jsonPost;
						}).collect(Collectors.toList()),
						new TypeToken<List<JsonObject>>() {}.getType())
						.getAsJsonArray();
				jsonObject.add("comments", comments);
				jsonArray.add(jsonObject);
			}
		}
		PrintWriter out = response.getWriter();
		out.print(jsonArray.toString());
	}
}
