package model.dao;

import model.dto.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import util.ConnectionPool;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserDao {

    private static final String selectAllQuery = "SELECT * FROM user";
    private static final String selectOneQuery = "SELECT * FROM user WHERE id=?";
    private static final String selectByUsernameQuery = "SELECT * FROM user WHERE username=?";
    private static final String addQuery = "INSERT INTO user (name, surname, username, password, email) VALUES (?, ?, ?, ?, ?)";
    private static final String deleteQuery = "DELETE FROM user WHERE id = ?";
    private static final String updateQuery = "UPDATE user SET name=?, surname=?, username=?, password=?, email=?, country=?, countryCode=?, region=?, city=?, loginCounter=?, picture_id=? WHERE id=?";
    private static final String countByUsernameQuery = "SELECT COUNT(*) as number FROM user WHERE username=?";
    private static final String countByEmailQuery = "SELECT COUNT(*) as number FROM user WHERE email=?";
    private static final String loginQuery = "UPDATE user SET isOnline=1, loginCounter=? WHERE id=?";
    private static final String insertToHistoryQuery = "INSERT INTO online_history (User_id, loginDatetime) VALUES (?,?)";
    private static final String[] logoutQuery = { "UPDATE user SET isOnline=0 WHERE id=?;",
                                                  "SELECT id FROM online_history oh WHERE oh.User_id=? ORDER BY oh.loginDatetime DESC LIMIT 1",
                                                  "UPDATE online_history SET logoutDatetime=? WHERE id=?" };


    public UserDao() {
    }

    public List<User> getAll() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        List<User> users = new ArrayList<>();
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            statement = connection.createStatement();
            statement.executeQuery(selectAllQuery);

            resultSet = statement.getResultSet();

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setSurname(resultSet.getString("surname"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setEmail(resultSet.getString("email"));
                user.setCountry(resultSet.getString("country"));
                user.setCountryCode(resultSet.getString("countryCode"));
                user.setRegion(resultSet.getString("region"));
                user.setLoginCounter(resultSet.getInt("loginCounter"));
                user.setCity(resultSet.getString("city"));
                Integer picture_id = resultSet.getInt("picture_id");
                if (resultSet.wasNull()) {
                    picture_id = null;
                }
                user.setPicture_Id(picture_id);
                user.setOnline(resultSet.getBoolean("isOnline"));
                user.setEnabled(resultSet.getBoolean("isEnabled"));
                users.add(user);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                Objects.requireNonNull(resultSet).close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                ConnectionPool.getConnectionPool().checkIn(connection);
            }
        }
        return users;
    }

    public int add(@NotNull User user) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(addQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getSurname());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getEmail());
            preparedStatement.executeUpdate();

            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                user.setId(id);
                return id;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                Objects.requireNonNull(resultSet).close();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                ConnectionPool.getConnectionPool().checkIn(connection);
            }
        }
        return -1;
    }

    public void remove(@NotNull User user) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                Objects.requireNonNull(preparedStatement).close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                ConnectionPool.getConnectionPool().checkIn(connection);
            }
        }
    }

    public boolean update(@NotNull User user) {
        Connection connection = null;
        PreparedStatement preparedStatement;
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getSurname());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getEmail());
            preparedStatement.setString(6, user.getCountry());
            preparedStatement.setString(7, user.getCountryCode());
            preparedStatement.setString(8, user.getRegion());
            preparedStatement.setString(9, user.getCity());
            preparedStatement.setInt(10, user.getLoginCounter());
            if (user.getPicture_Id() == null)
                preparedStatement.setNull(11, Types.INTEGER);
            else
                preparedStatement.setInt(11, user.getPicture_Id());
            preparedStatement.setInt(12, user.getId());
            int res = preparedStatement.executeUpdate();
            //"UPDATE user SET name=?, surname=?, username=?, password=?, email=?, country=?, countryCode=?, region=?, city=?, picture_id=? WHERE id=?";

            return res > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionPool.getConnectionPool().checkIn(connection);
        }
        return false;
    }

    @Nullable
    public User get(int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(selectOneQuery);
            preparedStatement.setInt(1, id);
            preparedStatement.executeQuery();

            resultSet = preparedStatement.getResultSet();

            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setSurname(resultSet.getString("surname"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setEmail(resultSet.getString("email"));
                user.setCountry(resultSet.getString("country"));
                user.setCountryCode(resultSet.getString("countryCode"));
                user.setRegion(resultSet.getString("region"));
                user.setLoginCounter(resultSet.getInt("loginCounter"));
                user.setCity(resultSet.getString("city"));
                Integer picture_id = resultSet.getInt("picture_id");
                if (resultSet.wasNull()) {
                    picture_id = null;
                }
                user.setPicture_Id(picture_id);
                user.setOnline(resultSet.getBoolean("isOnline"));
                user.setEnabled(resultSet.getBoolean("isEnabled"));
                return user;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                Objects.requireNonNull(resultSet).close();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                ConnectionPool.getConnectionPool().checkIn(connection);
            }
        }
        return user;
    }

    public User getByUsername(String username) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        User user = null;
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(selectByUsernameQuery);
            preparedStatement.setString(1, username);
            preparedStatement.executeQuery();

            resultSet = preparedStatement.getResultSet();

            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setSurname(resultSet.getString("surname"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setEmail(resultSet.getString("email"));
                user.setCountry(resultSet.getString("country"));
                user.setCountryCode(resultSet.getString("countryCode"));
                user.setRegion(resultSet.getString("region"));
                user.setLoginCounter(resultSet.getInt("loginCounter"));
                user.setCity(resultSet.getString("city"));
                Integer picture_id = resultSet.getInt("picture_id");
                if (resultSet.wasNull()) {
                    picture_id = null;
                }
                user.setPicture_Id(picture_id);
                user.setEnabled(resultSet.getBoolean("isEnabled"));
                user.setOnline(resultSet.getBoolean("isOnline"));
                return user;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                Objects.requireNonNull(resultSet).close();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                ConnectionPool.getConnectionPool().checkIn(connection);
            }
        }
        return user;
    }

    public boolean existsByUsername(String username) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(countByUsernameQuery);
            preparedStatement.setString(1, username);
            preparedStatement.executeQuery();

            resultSet = preparedStatement.getResultSet();

            if (resultSet.next()) {
                int number = resultSet.getInt("number");
                return number > 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                Objects.requireNonNull(resultSet).close();
                Objects.requireNonNull(preparedStatement).close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                ConnectionPool.getConnectionPool().checkIn(connection);
            }
        }
        return false;
    }

    public boolean existsByEmail(String email) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(countByEmailQuery);
            preparedStatement.setString(1, email);
            preparedStatement.executeQuery();

            resultSet = preparedStatement.getResultSet();

            if (resultSet.next()) {
                int number = resultSet.getInt("number");
                return number > 0;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                Objects.requireNonNull(resultSet).close();
                Objects.requireNonNull(preparedStatement).close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                ConnectionPool.getConnectionPool().checkIn(connection);
            }
        }
        return false;
    }

    public void login(@NotNull User user) {
        user.setOnline(true);
        user.setLoginCounter(user.getLoginCounter() + 1);

        Connection connection = null;
        PreparedStatement preparedStatement;

        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(loginQuery);
            preparedStatement.setInt(1, user.getLoginCounter());
            preparedStatement.setInt(2, user.getId());
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement(insertToHistoryQuery);
            preparedStatement.setInt(1, user.getId());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            preparedStatement.setString(2, formatter.format(LocalDateTime.now()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionPool.getConnectionPool().checkIn(connection);
        }
    }

    public void logout(@NotNull User user) {
        user.setOnline(false);

        Connection connection = null;
        PreparedStatement preparedStatement;

        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(logoutQuery[0]);
            preparedStatement.setInt(1, user.getId());
            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement(logoutQuery[1]);
            preparedStatement.setInt(1, user.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            int id = -1;
            if(resultSet.next()){
                id = resultSet.getInt("id");
            }

            preparedStatement = connection.prepareStatement(logoutQuery[2]);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            preparedStatement.setString(1, formatter.format(LocalDateTime.now()));
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionPool.getConnectionPool().checkIn(connection);
        }
    }

}
