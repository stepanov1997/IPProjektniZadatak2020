package model.dao;

import model.dto.Comment;
import model.dto.Notification;
import model.dto.User;
import org.jetbrains.annotations.NotNull;
import util.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class NotificationDao {

    private static final String selectAllQuery = "SELECT * FROM notifications;";
    private static final String selectAllForUserQuery =
            "SELECT * FROM notifications where User_id=?;";
    private static final String addQuery = "INSERT INTO notifications (User_id, Post_id, datetime) VALUES (?, ?, ?)";
    private static final String deleteQuery = "DELETE FROM notifications WHERE User_id=? AND Post_id=?";

    public List<Notification> selectAll() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Notification> listNotifications = new ArrayList<>();

        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(selectAllQuery);
            preparedStatement.executeQuery();

            resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                var notification = new Notification();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String res = resultSet.getString("datetime");
                LocalDateTime dateTime = LocalDateTime.parse(res, formatter);

                notification.setId(resultSet.getInt("id"));
                notification.setDateTime(dateTime);
                notification.setUser_id(resultSet.getInt("User_id"));
                notification.setPost_id(resultSet.getInt("Post_id"));
                listNotifications.add(notification);
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
        return listNotifications;
    }

    public List<Notification> selectAllForUser(User user) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Notification> listNotifications = new ArrayList<>();

        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(selectAllForUserQuery);
            preparedStatement.setInt(1, user.getId());
            preparedStatement.executeQuery();

            resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                var notification = new Notification();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String res = resultSet.getString("datetime");
                LocalDateTime dateTime = LocalDateTime.parse(res, formatter);

                notification.setId(resultSet.getInt("id"));
                notification.setDateTime(dateTime);
                notification.setUser_id(resultSet.getInt("User_id"));
                notification.setPost_id(resultSet.getInt("Post_id"));
                listNotifications.add(notification);
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
        return listNotifications;
    }

    public boolean add(@NotNull Notification notification) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(addQuery);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String datetime = formatter.format(notification.getDateTime());


            preparedStatement.setInt(1, notification.getUser_id());
            preparedStatement.setInt(2, notification.getPost_id());
            preparedStatement.setString(3, datetime);
            preparedStatement.executeUpdate();

            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
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
}
