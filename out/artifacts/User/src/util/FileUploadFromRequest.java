package util;

import com.mysql.cj.conf.ConnectionUrlParser;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Iterator;
import java.util.List;

public class FileUploadFromRequest {
    public static ConnectionUrlParser.Pair<String, byte[]> getFileFromRequest(HttpServletRequest request) throws Exception {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);

        if (!isMultipart) {
            throw new Exception();
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
        return new ConnectionUrlParser.Pair<>(fileName, img);
    }
}
