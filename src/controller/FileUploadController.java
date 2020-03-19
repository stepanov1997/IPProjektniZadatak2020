package controller;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class FileUploadController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("submit") != null && "upload".equals(request.getParameter("controller"))) {
            switch (request.getParameter("action")) {
                case "profilePicture": {
                    File file;
                    int maxFileSize = 5000 * 1024;
                    int maxMemSize = 5000 * 1024;
                    String filePath = "C:\\Users\\stepa\\data\\";
                    ServletOutputStream out = response.getOutputStream();

                    String contentType = request.getContentType();
                    if ((contentType.contains("multipart/form-data"))) {

                        DiskFileItemFactory factory = new DiskFileItemFactory();
                        factory.setSizeThreshold(maxMemSize);
                        factory.setRepository(new File("C:\\Users\\stepa\\data\\"));
                        ServletFileUpload upload = new ServletFileUpload(factory);
                        upload.setSizeMax(maxFileSize);
                        String fileName = "";
                        try {
                            List<FileItem> fileItems = upload.parseRequest(request);
                            for (FileItem fi : fileItems) {
                                if (!fi.isFormField()) {
                                    fileName = fi.getName();
                                    file = new File(filePath + fileName);

                                    fi.write(file);
                                    out.println("File " + fileName + " is successfully uploaded.");
                                }
                            }
                        } catch (Exception ex) {
                            System.out.println(ex);
                            out.println("File " + fileName + " is not successfully uploaded.");
                        }
                    }
                }
            }
        }
    }
}
