package controller;

import com.google.gson.Gson;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import util.ConnectionPool;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.sql.*;
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
                    // Check that we have a file upload request
                    boolean isMultipart = ServletFileUpload.isMultipartContent(request);
                    java.io.PrintWriter out = response.getWriter();

                    if (!isMultipart) {
                        out.println("<html>");
                        out.println("<head>");
                        out.println("<title>Servlet upload</title>");
                        out.println("</head>");
                        out.println("<body>");
                        out.println("<p>No file uploaded</p>");
                        out.println("</body>");
                        out.println("</html>");
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

                    try {
                        // Parse the request to get file items.
                        List fileItems = upload.parseRequest(request);

                        // Process the uploaded file items
                        Iterator i = fileItems.iterator();

                        if (i.hasNext()) {
                            FileItem fi = (FileItem) i.next();
                            if (!fi.isFormField()) {
                                // Get the uploaded file parameters
                                String fieldName = fi.getFieldName();
                                String fileName = fi.getName();
                                String contentType = fi.getContentType();
                                boolean isInMemory = fi.isInMemory();
                                long sizeInBytes = fi.getSize();

                                img = fi.get();
                            }
                        }
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }

                    Connection con = null;
                    PreparedStatement pstatement = null;
                    try {
                        con = ConnectionPool.getConnectionPool().checkOut();

                        String queryString = "INSERT INTO picture set picture=?";

                        pstatement = con.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
                        pstatement.setBytes(1, img);
                        pstatement.executeUpdate();
                        ResultSet generatedKeys = pstatement.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            int id = generatedKeys.getInt(1);
                            Gson gson = new Gson();
                            Map<String, Object> inputMap = new HashMap<>();
                            inputMap.put("id", id);
                            String json = gson.toJson(inputMap);
                            out.print(json);
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
