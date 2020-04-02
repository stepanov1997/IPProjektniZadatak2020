package controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import model.beans.AccountBean;
import model.dao.AccountDao;
import model.dao.CommentDao;
import model.dao.PictureDao;
import model.dto.Account;
import model.dto.Comment;
import model.dto.Picture;
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

public class PublishCommentController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer Post_id = Integer.valueOf(request.getParameter("Post_Id"));
        String commentString = request.getParameter("text");
        AccountBean accountBean = (AccountBean)request.getSession().getAttribute("accountBean");

        Gson gson = new Gson();
        Map<String, Object> inputMap = new HashMap<>();
        java.io.PrintWriter out = response.getWriter();

        boolean withImage  = Boolean.parseBoolean(request.getParameter("withImage"));

        String fileName = null;
        byte[] img = null;

        if(withImage) {
            try {
                var pair = FileUploadFromRequest.getFileFromRequest(request);
                fileName = pair.left;
                img = pair.right;
            } catch (Exception ex) {
                ex.printStackTrace();
                inputMap.put("success", false);
                String json = gson.toJson(inputMap);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                out.print(json);
                return;
            }
        }
        CommentDao commentDao = new CommentDao();
        Comment comment = new Comment();
        comment.setUser_id(accountBean.getAccount().getId());
        comment.setPost_id(Post_id);
        comment.setComment(commentString);
        comment.setDateTime(LocalDateTime.now());
        if(commentDao.add(comment)) {
            if(withImage){
                Picture picture = new Picture();
                picture.setFileName(fileName);
                picture.setImg(img);
                PictureDao pictureDao = new PictureDao();
                pictureDao.addToComment(comment, picture);
            }

            JsonObject jsonPost = new JsonObject();
            Account account2 = new AccountDao().get(comment.getUser_id());
            jsonPost.addProperty("nameSurname", account2.getName() + " " + account2.getSurname() + " ("+ account2.getUsername()+")");
            jsonPost.addProperty("ProfilePic_id", account2.getPicture_Id());
            jsonPost.addProperty("countryCode", account2.getCountryCode());
            jsonPost.addProperty("comment", comment.getComment());
            if(withImage)
                jsonPost.addProperty("Picture_id", comment.getPicture_id());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
            String dateTimeComment = formatter.format(comment.getDateTime());
            jsonPost.addProperty("datetime", dateTimeComment);

            out.print(jsonPost.toString());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
