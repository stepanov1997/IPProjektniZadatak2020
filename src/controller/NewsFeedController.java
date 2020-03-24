package controller;

import com.google.gson.Gson;
import model.beans.AccountBean;
import model.dao.PictureDao;
import model.dao.PostDao;
import model.dto.Account;
import model.dto.Picture;
import model.dto.Post;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
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

        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        java.io.PrintWriter out = response.getWriter();

        if (!isMultipart) {
            inputMap.put("success", false);
            String json = gson.toJson(inputMap);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print(json);
            return;
        }

        DiskFileItemFactory factory = new DiskFileItemFactory();

        // maximum size that will be stored in memory
        int maxMemSize = 5 * 1024 * 1024;
        factory.setSizeThreshold(maxMemSize);

        // Location to save data that is larger than maxMemSize.
        factory.setRepository(new File("c:\\temp"));

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);

        // maximum file size to be uploaded.
        int maxFileSize = 5 * 1024 * 1024;
        upload.setSizeMax(maxFileSize);

        byte[] img = null;
        String fileName = null;

        try {
            // Parse the request to get file items.
            List<FileItem> fileItems = upload.parseRequest(request);

            // Process the uploaded file items
            Iterator<FileItem> i = fileItems.iterator();

            if (i.hasNext()) {
                FileItem fi = i.next();
                if (!fi.isFormField()) {
                    fileName = fi.getName();
                    img = fi.get();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            inputMap.put("success", false);
            String json = gson.toJson(inputMap);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            out.print(json);
            return;
        }

        PictureDao pictureDao = new PictureDao();
        Picture picture = new Picture();
        picture.setFileName(fileName);
        picture.setImg(img);
        AccountBean accountBean = (AccountBean)request.getSession().getAttribute("accountBean");
        Account account = accountBean.getAccount();

        PostDao postDao = new PostDao();
        Post post = new Post();
        post.setUser_id(account.getId());
        post.setDateTime(LocalDateTime.now());
        post.setYoutubeLink(ytLink);
        post.setLink(link);
        post.setText(text);
        post.setUser_id(account.getId());
        post.setPicture_id(null);
        post.setVideo_id(null);


        if (postDao.add(post)) {
            request.getSession().setAttribute("accountBean", accountBean);
            inputMap.put("success", true);
            inputMap.put("dateTime", post.getDateTime().toLocalDate());
            inputMap.put("text", post.getText());
            inputMap.put("Picture_id", account.getPicture_Id());
            inputMap.put("countryCode", account.getCountryCode());
            var contentType = post.getContentTypeValue();
            if(contentType==null)
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
