package controller;

import model.dao.VideoDao;
import model.dto.Video;
import org.apache.commons.io.FilenameUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class VideoServletService extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if(request.getParameter("id")==null || "".equals(request.getParameter("id")))
        {
            response.getOutputStream().println("Bad parameter.");
            return;
        }

        int id = Integer.parseInt(request.getParameter("id"));

        VideoDao videoDao = new VideoDao();
        Video video = videoDao.get(id);
        String ext = FilenameUtils.getExtension(video.getFileName());
        response.setHeader("Content-Disposition", "attachment; filename=" + video.getFileName());
        response.setContentType("video/"+ext);
        response.setContentLength(video.getVideo().length);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(video.getVideo());
        BufferedInputStream bufferedInputStream = new BufferedInputStream(byteArrayInputStream);
        OutputStream out = response.getOutputStream();

        // Copy the contents of the file to the output stream
        byte[] buf = new byte[1024];
        int count;
        while ((count = bufferedInputStream.read(buf)) >= 0) {
            out.write(buf, 0, count);
        }
        out.flush();
        out.close();
        bufferedInputStream.close();
        byteArrayInputStream.close();
    }
}
