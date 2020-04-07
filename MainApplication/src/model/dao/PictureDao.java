package model.dao;

import model.dto.User;
import model.dto.Comment;
import model.dto.Picture;
import model.dto.Post;
import util.ConnectionPool;

import java.sql.*;
import java.time.format.DateTimeFormatter;

public class PictureDao {
    private static final String selectOneQuery = "SELECT * FROM picture WHERE id=?;";
    private static final String addQuery = "INSERT INTO picture (fileName,picture) values (?,?);";
    private static final String addPictureToUserQuery = "UPDATE user SET picture_id=? WHERE id=?;";
    private static final String addPictureToPostQuery = "UPDATE post SET Picture_id=? WHERE id=?;";
    private static final String addPictureToCommentQuery = "UPDATE comment SET Picture_id=? WHERE User_id=? AND Post_id=? AND datetime=?;";

    public PictureDao() {}

    public Picture get(int id) {
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
                Picture picture = new Picture();
                picture.setId(id);
                picture.setFileName(resultSet.getString("fileName"));
                picture.setImg(resultSet.getBytes("picture"));
                return picture;
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

    public boolean addToUser(User user, Picture picture) {
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet generatedKeys = null;
        ResultSet resultSet = null;
        int id = -1;
        try {
            con = ConnectionPool.getConnectionPool().checkOut();

            preparedStatement = con.prepareStatement(addQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, picture.getFileName());
            preparedStatement.setBytes(2, picture.getImg());
            preparedStatement.executeUpdate();
            generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                id = generatedKeys.getInt(1);
                picture.setId(id);
            }
            if(id==-1) return false;

            preparedStatement = con.prepareStatement(addPictureToUserQuery);
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, user.getId());
            user.setPicture_Id(id);
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

    public boolean addToComment(Comment comment, Picture picture) {
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet generatedKeys = null;
        int id = -1;
        try {
            con = ConnectionPool.getConnectionPool().checkOut();

            preparedStatement = con.prepareStatement(addQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, picture.getFileName());
            preparedStatement.setBytes(2, picture.getImg());
            preparedStatement.executeUpdate();
            generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                id = generatedKeys.getInt(1);
                picture.setId(id);
            }
            if(id==-1) return false;

            preparedStatement = con.prepareStatement(addPictureToCommentQuery);
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, comment.getUser_id());
            preparedStatement.setInt(3, comment.getPost_id());
            preparedStatement.setString(4, comment.getDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            comment.setPicture_id(id);
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

    public boolean addToPost(Post post, Picture picture) {
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet generatedKeys = null;
        int id = -1;
        try {
            con = ConnectionPool.getConnectionPool().checkOut();

            preparedStatement = con.prepareStatement(addQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, picture.getFileName());
            preparedStatement.setBytes(2, picture.getImg());
            preparedStatement.executeUpdate();
            generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                id = generatedKeys.getInt(1);
                picture.setId(id);
            }
            if(id==-1) return false;

            preparedStatement = con.prepareStatement(addPictureToPostQuery);
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, post.getId());
            post.setPicture_id(id);
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
