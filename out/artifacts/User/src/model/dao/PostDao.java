package model.dao;

import model.dto.User;
import model.dto.Post;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import util.ConnectionPool;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PostDao {

    private static final String selectAllQuery = "SELECT * FROM post";
    private static final String selectByUserQuery = "SELECT * FROM post WHERE User_id=?";
    private static final String addQuery = "INSERT INTO post (User_id, text, link, Picture_id, Video_id, youtubeLink, dateTime) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String deleteQuery = "DELETE FROM post WHERE id = ?";
    private static final String updateQuery = "UPDATE post SET User_id=?, text=?, link=?, Picture_id=?, Video_id=?, youtubeLink=?, dateTime=? WHERE id=?";

    public PostDao() {
    }

    public List<Post> getAll() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        List<Post> posts = new ArrayList<>();
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            statement = connection.createStatement();
            statement.executeQuery(selectAllQuery);

            resultSet = statement.getResultSet();

            while (resultSet.next()) {
                Post post = new Post();
                post.setId(resultSet.getInt("id"));
                post.setUser_id(resultSet.getInt("User_id"));
                post.setText(resultSet.getString("text"));
                post.setLink(resultSet.getString("link"));
                Integer Picture_id = resultSet.getInt("Picture_id");
                if(resultSet.wasNull())
                    Picture_id = null;
                post.setPicture_id(Picture_id);
                Integer Video_id = resultSet.getInt("Video_id");
                if(resultSet.wasNull())
                    Video_id = null;
                post.setVideo_id(Video_id);
                post.setYoutubeLink(resultSet.getString("youtubeLink"));

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime dateTime = LocalDateTime.parse(resultSet.getString("dateTime"), formatter);
                post.setDateTime(dateTime);
                posts.add(post);
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
        return posts;
    }

    public boolean add(Post post) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(addQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, post.getUser_id());
            preparedStatement.setString(2, post.getText());
            preparedStatement.setString(3, post.getLink());
            if(post.getPicture_id()==null)
                preparedStatement.setNull(4, Types.INTEGER);
            else
                preparedStatement.setInt(4, post.getPicture_id());

            if(post.getVideo_id()==null)
                preparedStatement.setNull(5, Types.INTEGER);
            else
                preparedStatement.setInt(5, post.getVideo_id());
            preparedStatement.setString(6, post.getYoutubeLink());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String currentDateTime = formatter.format(post.getDateTime());
            preparedStatement.setString(7, currentDateTime);
            preparedStatement.executeUpdate();

            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                post.setId(id);
                return true;
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
        return false;
    }

    public boolean remove(@NotNull User user) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, user.getId());
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

    public boolean update(@NotNull Post post) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            //User_id=?, text=?, link=?, Picture_id=?, Video_id=?, youtubeLink=?, dateTime=? WHERE id=?"
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setInt(1, post.getUser_id());
            preparedStatement.setString(2, post.getText());
            preparedStatement.setString(3, post.getLink());
            if (post.getPicture_id() == null)
                preparedStatement.setNull(4, Types.INTEGER);
            else
                preparedStatement.setInt(4, post.getPicture_id());

            if (post.getVideo_id() == null)
                preparedStatement.setNull(5, Types.INTEGER);
            else
                preparedStatement.setInt(5, post.getVideo_id());

            preparedStatement.setString(6, post.getYoutubeLink());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String currentDateTime = formatter.format(post.getDateTime());
            preparedStatement.setString(7, currentDateTime);
            preparedStatement.setInt(8, post.getId());

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
    public Post get(int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Post post = null;
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(selectByUserQuery);
            preparedStatement.setInt(1, id);
            preparedStatement.executeQuery();

            resultSet = preparedStatement.getResultSet();

            if (resultSet.next()) {
                post = new Post();
                post.setId(resultSet.getInt("id"));
                post.setUser_id(resultSet.getInt("User_id"));
                post.setText(resultSet.getString("text"));
                post.setLink(resultSet.getString("link"));
                post.setPicture_id(resultSet.getInt("Picture_id"));
                post.setVideo_id(resultSet.getInt("Video_id"));
                post.setYoutubeLink(resultSet.getString("youtubeLink"));

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime dateTime = LocalDateTime.parse(resultSet.getString("dateTime"), formatter);
                post.setDateTime(dateTime);
                return post;
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
        return post;
    }
}