package model.dao;

import model.dto.Post;
import model.dto.Video;
import util.ConnectionPool;

import java.sql.*;

public class VideoDao {
    private static final String selectOneQuery = "SELECT * FROM video WHERE id=?;";
    private static final String addQuery = "INSERT INTO video (fileName,video) values (?,?);";
    private static final String addPictureToPostQuery = "UPDATE post SET Video_id=? WHERE id=?;";
    public VideoDao() {}

    public Video get(int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(selectOneQuery);
            preparedStatement.setInt(1, id);
            preparedStatement.executeQuery();

            resultSet = preparedStatement.getResultSet();

            if (resultSet.next()) {
                Video video = new Video();
                video.setId(id);
                video.setFileName(resultSet.getString("fileName"));
                video.setVideo(resultSet.getBytes("video"));
                return video;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                resultSet.close();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                ConnectionPool.getConnectionPool().checkIn(connection);
            }
        }
        return null;
    }

    public boolean addToPost(Post post, Video video) {
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet generatedKeys = null;
        int id = -1;
        try {
            con = ConnectionPool.getConnectionPool().checkOut();

            preparedStatement = con.prepareStatement(addQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, video.getFileName());
            preparedStatement.setBytes(2, video.getVideo());
            preparedStatement.executeUpdate();
            generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                id = generatedKeys.getInt(1);
                video.setId(id);
            }
            if(id==-1) return false;

            preparedStatement = con.prepareStatement(addPictureToPostQuery);
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, post.getId());
            post.setVideo_id(id);
            return preparedStatement.executeUpdate()>0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                generatedKeys.close();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                ConnectionPool.getConnectionPool().checkIn(con);
            }
        }
        return false;
    }
}
