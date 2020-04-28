package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import model.beans.UserBean;
import model.dao.CommentDao;
import model.dao.DangerCategoryDao;
import model.dao.PostDao;
import model.dao.UserDao;
import model.dto.Post;
import model.dto.User;

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
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PostServletService extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		UserBean userBean = ((UserBean)request.getSession().getAttribute("userBean"));
		User user = userBean.getUser();
		if(user == null)
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

		List<Object> posts = new ArrayList<>(new ArrayList<>(Objects.requireNonNull(feed).getEntries()));
		PostDao postDao = new PostDao();
		posts.addAll(postDao.getAll());
		posts.sort(Post::postCompare);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
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

				UserDao userDao = new UserDao();
				User user1 = userDao.get(post.getUser_id());
				jsonObject.addProperty("nameSurname", Objects.requireNonNull(user1).getName() + " " + user1.getSurname() + " (" + user1.getUsername() + ")");
				jsonObject.addProperty("Picture_id", user1.getPicture_Id());
				jsonObject.addProperty("countryCode", user1.getCountryCode());
				var contentType = post.getContentTypeValue();
				jsonObject.addProperty("withAttachment", post.isWithAttachment());
				if(post.isWithAttachment())
				{
					if(contentType==null)
						jsonObject.addProperty("contentType", "textOnly");
					else {
						jsonObject.addProperty("contentType", contentType.left);
						jsonObject.addProperty("value", contentType.right);
					}
				}
				else
				{
					if(post.getLocation()!=null)
						jsonObject.addProperty("location", post.getLocation());
					if(post.getDangerCategory_id()!=null)
					{
						var result = new DangerCategoryDao().get(post.getDangerCategory_id());
						jsonObject.addProperty("category", result.getName());
					}
					jsonObject.addProperty("isEmergency", post.isEmergency());
				}

				JsonArray comments;
				CommentDao commentDao = new CommentDao();
				comments = gson.toJsonTree(commentDao
						.getFromPost(post.getId())
						.stream()
						.sorted((a,b) -> a.getDateTime().isAfter(b.getDateTime())?1:-1)
						.map(elem ->
						{
							JsonObject jsonPost = new JsonObject();
							User user2 = new UserDao().get(elem.getUser_id());
							jsonPost.addProperty("nameSurname", user2.getName() + " " + user2.getSurname() + " ("+ user2.getUsername()+")");
							jsonPost.addProperty("ProfilePic_id", user2.getPicture_Id());
							jsonPost.addProperty("countryCode", user2.getCountryCode());
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
