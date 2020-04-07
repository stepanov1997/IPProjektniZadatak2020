package controller;

import model.dao.PictureDao;
import model.dto.Picture;
import org.apache.commons.io.FilenameUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class PicturesServletService extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        if(request.getParameter("id")==null || "".equals(request.getParameter("id")))
        {
            response.getOutputStream().println("Bad parameter.");
            return;
        }

        int id = Integer.parseInt(request.getParameter("id"));

        PictureDao pictureDao = new PictureDao();
        Picture picture = pictureDao.get(id);
        String ext = FilenameUtils.getExtension(picture.getFileName());
        if(ext.equals("svg")) ext+="+xml";
        response.setHeader("Content-Disposition", "attachment; filename=" + picture.getFileName());
        response.setContentType("image/"+ext);
        response.setContentLength(picture.getImg().length);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(picture.getImg());
        BufferedInputStream bufferedInputStream = new BufferedInputStream(byteArrayInputStream);
        OutputStream out = response.getOutputStream();

        // Copy the contents of the file to the output stream
        byte[] buf = new byte[1024];
        int count = 0;
        while ((count = bufferedInputStream.read(buf)) >= 0) {
            out.write(buf, 0, count);
        }
        out.flush();
        out.close();
        bufferedInputStream.close();
        byteArrayInputStream.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

}