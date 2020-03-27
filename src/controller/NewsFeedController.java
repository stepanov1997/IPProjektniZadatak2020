package controller;

import com.google.gson.Gson;
import model.beans.AccountBean;
import model.dao.PictureDao;
import model.dao.PostDao;
import model.dto.Account;
import model.dto.Picture;
import model.dto.Post;
import util.FileUploadFromRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class NewsFeedController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ytLink = request.getParameter("ytLink");
        String link = request.getParameter("link");
        String text = request.getParameter("text");

        Gson gson = new Gson();
        Map<String, Object> inputMap = new HashMap<>();
        java.io.PrintWriter out = response.getWriter();

        String fileName;
        byte[] img;
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

        AccountBean accountBean = (AccountBean) request.getSession().getAttribute("accountBean");
        Account account = accountBean.getAccount();

        PostDao postDao = new PostDao();
        Post post = new Post();
        post.setUser_id(account.getId());
        post.setDateTime(LocalDateTime.now());
        post.setYoutubeLink(ytLink);
        post.setLink(link);
        post.setText(text);
        post.setUser_id(account.getId());

        post.setVideo_id(null);


        if (postDao.add(post)) {

            if (fileName != null && "".equals(fileName)) {
                PictureDao pictureDao = new PictureDao();
                Picture picture = new Picture();
                picture.setFileName(fileName);
                picture.setImg(img);
                pictureDao.addToPost(post, picture);
            }

            request.getSession().setAttribute("accountBean", accountBean);
            inputMap.put("success", true);
            inputMap.put("dateTime", post.getDateTime().toLocalDate());
            inputMap.put("text", post.getText());
            inputMap.put("Picture_id", account.getPicture_Id());
            inputMap.put("countryCode", account.getCountryCode());
            var contentType = post.getContentTypeValue();
            if (contentType == null)
                inputMap.put("contentType", "textOnly");
            else {
                inputMap.put("contentType", contentType.left);
                inputMap.put("value", contentType.right);
            }
        } else {
            inputMap.put("success", false);
        }
        String json = gson.toJson(inputMap);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(json);
    }
}
