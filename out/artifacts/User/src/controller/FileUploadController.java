package controller;

import com.google.gson.Gson;
import model.beans.UserBean;
import model.dao.PictureDao;
import model.dto.User;
import model.dto.Picture;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FileUploadController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("submit") != null && "upload".equals(request.getParameter("controller"))) {
            switch (request.getParameter("action")) {
                case "profilePicture": {
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
                            FileItem fi = (FileItem) i.next();
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
                    UserBean userBean = (UserBean)request.getSession().getAttribute("userBean");
                    User user = userBean.getUser();
                    if (pictureDao.addToUser(user, picture)) {
                        request.getSession().setAttribute("userBean", userBean);
                        inputMap.put("success", true);
                        inputMap.put("id", picture.getId());
                    } else {
                        inputMap.put("success", false);
                    }
                    String json = gson.toJson(inputMap);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    out.print(json);
                }
            }
        }
    }
}
