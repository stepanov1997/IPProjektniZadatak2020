package model.dao;

import model.dto.Comment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import util.ConnectionPool;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CommentDao {

    private static final String selectAllQuery = "SELECT * FROM comment";
    private static final String selectAllFromPost = "SELECT * FROM comment WHERE Post_id=? ORDER BY dateTime DESC;";
    private static final String selectByUserAndPostQuery = "SELECT * FROM comment WHERE User_id=? AND Post_id=?";
    private static final String addQuery = "INSERT INTO comment (dateTime, comment, Post_id, User_id, Picture_id) VALUES (?, ?, ?, ?, ?)";
    private static final String deleteQuery = "DELETE FROM comment WHERE User_id=? AND Post_id=? AND dateTime=?";
    private static final String updateQuery = "UPDATE comment SET comment=?, Picture_id=? WHERE User_id=? AND Post_id=? AND dateTime=?";


    public CommentDao() {
    }

    public List<Comment> getFromPost(int Post_id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Comment comment = null;
        List<Comment> listComments = new ArrayList<>();

        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(selectAllFromPost);
            preparedStatement.setInt(1, Post_id);
            preparedStatement.executeQuery();

            resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                comment = new Comment();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String res = resultSet.getString("dateTime");
                LocalDateTime dateTime = LocalDateTime.parse(res, formatter);

                comment.setDateTime(dateTime);
                comment.setComment(resultSet.getString("comment"));
                comment.setUser_id(resultSet.getInt("User_id"));
                comment.setPost_id(resultSet.getInt("Post_id"));
                comment.setPicture_id(resultSet.getInt("Picture_id"));
                listComments.add(comment);
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
        return listComments;
    }

    public List<Comment> getAll() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        List<Comment> comments = new ArrayList<>();
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            statement = connection.createStatement();
            statement.executeQuery(selectAllQuery);

            resultSet = statement.getResultSet();

            while (resultSet.next()) {
                //(dateTime, comment, Post_id, User_id, Picture_id) VALUES (?, ?, ?, ?, ?)";
                Comment comment = new Comment();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
                LocalDateTime dateTime = LocalDateTime.parse(resultSet.getString("dateTime"), formatter);

                comment.setDateTime(dateTime);
                comment.setComment(resultSet.getString("comment"));
                comment.setPost_id(resultSet.getInt("Post_id"));
                comment.setUser_id(resultSet.getInt("User_id"));
                comment.setPicture_id(resultSet.getInt("Picture_id"));
                comments.add(comment);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                resultSet.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                ConnectionPool.getConnectionPool().checkIn(connection);
            }
        }
        return comments;
    }

    public boolean add(@NotNull Comment comment) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(addQuery);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
            String datetime = formatter.format(comment.getDateTime());

            preparedStatement.setString(1, datetime);
            preparedStatement.setString(2, comment.getComment());
            preparedStatement.setInt(3, comment.getPost_id());
            preparedStatement.setInt(4, comment.getUser_id());
            preparedStatement.setInt(5, comment.getPicture_id());
            preparedStatement.executeUpdate();

            return true;
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
        return false;
    }

    public boolean remove(@NotNull Comment comment) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, comment.getPost_id());
            preparedStatement.setInt(2, comment.getUser_id());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
            String datetime = formatter.format(comment.getDateTime());

            preparedStatement.setString(3, datetime);
            int rowsDeleted = preparedStatement.executeUpdate();
            return rowsDeleted > 0;
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
        return false;
    }

    public boolean update(@NotNull Comment comment) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, comment.getComment());

            if (comment.getPicture_id() == null)
                preparedStatement.setNull(2, Types.INTEGER);
            else
                preparedStatement.setInt(2, comment.getPicture_id());

            preparedStatement.setInt(3, comment.getUser_id());
            preparedStatement.setInt(4, comment.getPost_id());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
            String datetime = formatter.format(comment.getDateTime());

            preparedStatement.setString(5, datetime);
            int res = preparedStatement.executeUpdate();
            return res > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionPool.getConnectionPool().checkIn(connection);
        }
        return false;
    }

    @Nullable
    public List<Comment> get(int User_id, int Post_id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Comment comment = null;
        List<Comment> listComments = new ArrayList<>();

        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(selectByUserAndPostQuery);
            preparedStatement.setInt(1, User_id);
            preparedStatement.setInt(2, Post_id);
            preparedStatement.executeQuery();

            resultSet = preparedStatement.getResultSet();

            listComments = new ArrayList<>();
            while (resultSet.next()) {
                comment = new Comment();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
                LocalDateTime dateTime = LocalDateTime.parse(resultSet.getString("dateTime"), formatter);

                comment.setDateTime(dateTime);
                comment.setComment(resultSet.getString("comment"));
                comment.setUser_id(resultSet.getInt("User_id"));
                comment.setPost_id(resultSet.getInt("Post_id"));
                comment.setPicture_id(resultSet.getInt("Picture_id"));
                listComments.add(comment);
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
        return listComments;
    }
}
