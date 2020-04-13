package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import model.beans.UserBean;
import model.dao.PictureDao;
import model.dao.PostDao;
import model.dao.VideoDao;
import model.dto.Picture;
import model.dto.Post;
import model.dto.User;
import model.dto.Video;
import util.FileUploadFromRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class NewsFeedController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ytLink = request.getParameter("ytLink");
        String link = request.getParameter("link");
        String text = request.getParameter("text");
        String content = request.getParameter("content");

        Gson gson = new Gson();
        Map<String, Object> inputMap = new HashMap<>();
        java.io.PrintWriter out = response.getWriter();

        String fileName;
        byte[] file;
        try {
            var pair = FileUploadFromRequest.getFileFromRequest(request);
            fileName = pair.left;
            file = pair.right;
        } catch (Exception ex) {
            ex.printStackTrace();
            inputMap.put("success", false);
            String json = gson.toJson(inputMap);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print(json);
            return;
        }

        UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
        User user = userBean.getUser();

        PostDao postDao = new PostDao();
        Post post = new Post();
        post.setUser_id(user.getId());
        post.setDateTime(LocalDateTime.now());
        post.setYoutubeLink(ytLink);
        post.setLink(link);
        post.setText(text);
        post.setUser_id(user.getId());

        post.setVideo_id(null);

        JsonObject jsonObject = new JsonObject();

        if (postDao.add(post)) {

            if (!"".equals(fileName)) {
                if("picture".equals(content)){
                    PictureDao pictureDao = new PictureDao();
                    Picture picture = new Picture();
                    picture.setFileName(fileName);
                    picture.setImg(file);
                    pictureDao.addToPost(post, picture);
                }
                else if("video".equals(content))
                {
                    VideoDao videoDao = new VideoDao();
                    Video video = new Video();
                    video.setFileName(fileName);
                    video.setVideo(file);
                    videoDao.addToPost(post, video);
                }
            }
            request.getSession().setAttribute("userBean", userBean);
            jsonObject.addProperty("success", true);
            jsonObject.addProperty("id", post.getId());
            jsonObject.addProperty("text", post.getText());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
            String dateTime = formatter.format(post.getDateTime());
            jsonObject.addProperty("date", dateTime);

            jsonObject.addProperty("nameSurname", Objects.requireNonNull(user).getName() + " " + user.getSurname() + " (" + user.getUsername() + ")");
            jsonObject.addProperty("Picture_id", user.getPicture_Id());
            jsonObject.addProperty("countryCode", user.getCountryCode());
            var contentType = post.getContentTypeValue();
            if(contentType==null)
                jsonObject.addProperty("contentType", "textOnly");
            else {
                jsonObject.addProperty("contentType", contentType.left);
                jsonObject.addProperty("value", contentType.right);
            }
        } else {
            jsonObject.addProperty("success", false);
        }
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(jsonObject.toString());
    }
}
