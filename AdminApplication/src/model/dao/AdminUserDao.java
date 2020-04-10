package model.dao;

import model.dto.Administrator;
import model.dto.History;
import model.dto.User;
import util.ConnectionPool;

import javax.faces.context.FacesContext;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class AdminUserDao extends UserDao {

    private static final String selectAdminByUsernameQuery = "SELECT * FROM administrator WHERE username=?";
    private static final String giveAccessQuery = "UPDATE user SET isEnabled=1 WHERE id=?";
    private static final String blockAccessQuery = "UPDATE user SET isEnabled=0 WHERE id=?";
    private static final String countActiveUsersQuery = "SELECT count(*) FROM user WHERE isOnline=1;";
    private static final String countRegisteredUsersQuery = "SELECT count(*) FROM user";

    public Administrator getAdminByUsername(String username) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Administrator admin = null;
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(selectAdminByUsernameQuery);
            preparedStatement.setString(1, username);
            preparedStatement.executeQuery();

            resultSet = preparedStatement.getResultSet();

            if (resultSet.next()) {
                admin = new Administrator();

                admin.setUsername(resultSet.getString("username"));
                admin.setPassword(resultSet.getString("password"));
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
        return admin;
    }

    public boolean giveAccess(User user) {
        user.setEnabled(true);
        Connection connection = null;
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            PreparedStatement preparedStatement = connection.prepareStatement(giveAccessQuery);
            preparedStatement.setInt(1, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean blockUser(User user) {
        user.setEnabled(false);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(blockAccessQuery);
            preparedStatement.setInt(1, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            ConnectionPool.getConnectionPool().checkIn(connection);
        }
        return true;
    }

    public String resetPassword(User user) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<8; i++)
        {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public Integer getNumberOfActiveUsers() {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(countActiveUsersQuery);

            if(resultSet.next())
            {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            ConnectionPool.getConnectionPool().checkIn(connection);
        }
        return null;
    }

    public Integer getNumberOfRegistredUsers() {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(countRegisteredUsersQuery);

            if(resultSet.next())
            {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            ConnectionPool.getConnectionPool().checkIn(connection);
        }
        return null;
    }

    public History get24hoursHistory() {
        Connection connection = null;
        PreparedStatement statement = null;
        HashMap<LocalDateTime, Integer> historyMap = new HashMap<>();
        try {
            String sql = Files.readString(Paths.get(FacesContext.getCurrentInstance().getExternalContext().getResource("/WEB-INF/resources/history1.sql").toURI()));
            connection = ConnectionPool.getConnectionPool().checkOut();
            var st = connection.createStatement();
            ResultSet resultSet = st.executeQuery(sql);
            while(resultSet.next())
            {
                int hour = resultSet.getInt("hour");
                int times = resultSet.getInt("times");
                LocalDateTime dateTime;
                if(LocalDateTime.now().getHour()<hour)
                    dateTime = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(hour, 0));
                else
                    dateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(hour, 0));
                historyMap.put(dateTime, times);
            }
        } catch (SQLException | URISyntaxException | IOException e) {
            e.printStackTrace();
        } finally {
            ConnectionPool.getConnectionPool().checkIn(connection);
        }

        var result = historyMap.entrySet().stream().sorted((a, b) -> a.getKey().isBefore(b.getKey()) ? 1 : -1).collect(Collectors.toList());
        return new History(result, "Hour", "Logins");
    }
}
