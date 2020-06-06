package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import model.beans.UserBean;
import model.dao.*;
import model.dto.*;
import util.FileUploadFromRequest;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class NewsFeedController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("withAttachment") != null && Boolean.parseBoolean(request.getParameter("withAttachment"))) {
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
            post.setWithAttachment(true);

            post.setVideo_id(null);

            JsonObject jsonObject = new JsonObject();

            if (postDao.add(post)) {

                if (!"".equals(fileName)) {
                    if ("picture".equals(content)) {
                        PictureDao pictureDao = new PictureDao();
                        Picture picture = new Picture();
                        picture.setFileName(fileName);
                        picture.setImg(file);
                        pictureDao.addToPost(post, picture);
                    } else if ("video".equals(content)) {
                        VideoDao videoDao = new VideoDao();
                        Video video = new Video();
                        video.setFileName(fileName);
                        video.setVideo(file);
                        videoDao.addToPost(post, video);
                    }
                }
                request.getSession().setAttribute("userBean", userBean);
                jsonObject.addProperty("success", true);
                jsonObject.addProperty("withAttachment", true);
                jsonObject.addProperty("id", post.getId());
                jsonObject.addProperty("text", post.getText());

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
                String dateTime = formatter.format(post.getDateTime());
                jsonObject.addProperty("date", dateTime);

                jsonObject.addProperty("nameSurname", Objects.requireNonNull(user).getName() + " " + user.getSurname() + " (" + user.getUsername() + ")");
                jsonObject.addProperty("Picture_id", user.getPicture_Id());
                jsonObject.addProperty("countryCode", user.getCountryCode());
                var contentType = post.getContentTypeValue();
                if (contentType == null)
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
        } else {
            String text = request.getParameter("text2");
            String location = request.getParameter("location");
            boolean isEmergency = Boolean.parseBoolean(request.getParameter("isEmergency"));
            Integer category = Integer.parseInt(request.getParameter("category"));

            java.io.PrintWriter out = response.getWriter();

            UserBean userBean = (UserBean) request.getSession().getAttribute("userBean");
            User user = userBean.getUser();

            PostDao postDao = new PostDao();
            Post post = new Post();
            post.setUser_id(user.getId());
            post.setDateTime(LocalDateTime.now());
            post.setLocation(location);
            post.setWithAttachment(false);
            post.setEmergency(isEmergency);
            post.setDangerCategory_id(category);
            post.setText(text);
            post.setUser_id(user.getId());

            JsonObject jsonObject = new JsonObject();

            if (postDao.add(post)) {

                request.getSession().setAttribute("userBean", userBean);
                jsonObject.addProperty("success", true);
                jsonObject.addProperty("withAttachment", false);
                jsonObject.addProperty("id", post.getId());
                jsonObject.addProperty("text", post.getText());

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
                String dateTime = formatter.format(post.getDateTime());
                jsonObject.addProperty("date", dateTime);

                jsonObject.addProperty("nameSurname", Objects.requireNonNull(user).getName() + " " + user.getSurname() + " (" + user.getUsername() + ")");
                jsonObject.addProperty("Picture_id", user.getPicture_Id());
                jsonObject.addProperty("countryCode", user.getCountryCode());
                jsonObject.addProperty("location", location);
                jsonObject.addProperty("category", new DangerCategoryDao().get(category).getName());
                jsonObject.addProperty("isEmergency", post.isEmergency());
            } else {
                jsonObject.addProperty("success", false);
            }
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print(jsonObject.toString());

            if (isEmergency) {
                UserDao userDao = new UserDao();
                List<User> allUsers = userDao.getAll();
                allUsers.forEach(oneUser -> {
                    if(oneUser.getUsername().equals(user.getUsername()))
                        return;
                    if (oneUser.getNotificationType() == 0) {
                        NotificationDao notificationDao = new NotificationDao();
                        notificationDao.add(new Notification(null, oneUser.getId(), post.getId(), LocalDateTime.now()));
                    } else if (oneUser.getNotificationType() == 1) {
                        sentMailToUser(oneUser, post);
                    }
                });
            }
        }
    }

    private void sentMailToUser(User user, Post post) {
        ResourceBundle resourceBundle = PropertyResourceBundle.getBundle("util.Email");
        final String username = resourceBundle.getString("username");
        final String password = resourceBundle.getString("password");

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(user.getEmail())
            );
            message.setSubject("Administrator's application");
            DangerCategoryDao dangerCategoryDao = new DangerCategoryDao();
            UserDao userDao = new UserDao();
            String[] location = {post.getLocation().split(" ")[0], post.getLocation().split(" ")[1]};
            User postUser = userDao.get(post.getUser_id());
            String type = post.getContentTypeValue()!=null?post.getContentTypeValue().left.replace("ytLink", "Youtube link"):"post";
            message.setText("Dear " + user.getName() + " " + user.getSurname() + ","
                    + "\n\nYou have new notification:"
                    + " User " + postUser.getName() + " " +
                            postUser.getSurname() + " shared a post about potential danger."
                    + (post.getDangerCategory_id() != null ?
                    ("\n\n Danger category: " + dangerCategoryDao.get(post.getDangerCategory_id()).getName()) : "")
                    + "\n Datetime: " + DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm:ss").format(LocalDateTime.now())
                    + "\n Text: " + post.getText()
                    + "\n Location: " + "( "+post.getLocation().split(" ")[0]+" , "+post.getLocation().split(" ")[1]+" )"
                    + "\n\n Administrator");

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
