package model.dao;

import model.dto.AssistanceCall;
import util.ConnectionPool;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AssistanceCallDao {
    private static final String getAllQuery = "SELECT * FROM AssistanceCall";
    private static final String getQuery = "SELECT * FROM AssistanceCall WHERE id=?";
    private static final String insertQuery =
            "INSERT INTO AssistanceCall (name, datetime, location, description, author, phone, isBlocked, " +
                    "reportsCounter, urlPicture, CategoryOfCalls_id) VALUES (?,?,?,?,?,?,?,?,?,?)";
    private static final String updateQuery =
            "UPDATE AssistanceCall SET name=?, datetime=?, location=?, description=?, author=?, phone=?, isBlocked=?, " +
                    "reportsCounter=?, urlPicture=?, CategoryOfCalls_id=? WHERE id=?";
    private static final String reportQuery = "UPDATE AssistanceCall SET reportsCounter=reportsCounter+1 WHERE id=?";
    private static final String blockQuery = "UPDATE AssistanceCall SET isBlocked=1 WHERE id=?";
    private static final String deleteQuery = "DELETE FROM AssistanceCall WHERE id=?";


    public List<AssistanceCall> getAll()
    {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        List<AssistanceCall> list = new ArrayList<>();
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(getAllQuery);
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            while (resultSet.next())
            {
                AssistanceCall assistanceCall = new AssistanceCall(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        LocalDateTime.parse(resultSet.getString("datetime"), dateTimeFormatter),
                        resultSet.getString("location"),
                        resultSet.getString("description"),
                        resultSet.getString("urlPicture"),
                        resultSet.getString("author"),
                        resultSet.getString("phone"),
                        resultSet.getBoolean("isBlocked"),
                        resultSet.getInt("reportsCounter"),
                        resultSet.getInt("categoryofcalls_id")
                );
                list.add(assistanceCall);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            list = new ArrayList<>();
        }
        finally {
            ConnectionPool.getConnectionPool().checkIn(connection);
        }
        return list;
    }

    public AssistanceCall get(int id)
    {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(getQuery);
            resultSet = preparedStatement.executeQuery();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            if (resultSet.next())
            {
                return new AssistanceCall(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        LocalDateTime.parse(resultSet.getString("datetime"), dateTimeFormatter),
                        resultSet.getString("location"),
                        resultSet.getString("description"),
                        resultSet.getString("urlPicture"),
                        resultSet.getString("author"),
                        resultSet.getString("phone"),
                        resultSet.getBoolean("isBlocked"),
                        resultSet.getInt("reportsCounter"),
                        resultSet.getInt("categoryofcalls_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            ConnectionPool.getConnectionPool().checkIn(connection);
        }
        return null;
    }

    public Integer add(AssistanceCall assistanceCall)
    {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, assistanceCall.getName());
            preparedStatement.setString(2, dateTimeFormatter.format(assistanceCall.getDatetime()));
            preparedStatement.setString(3, assistanceCall.getLocation());
            preparedStatement.setString(4, assistanceCall.getDescription());
            preparedStatement.setString(5, assistanceCall.getAuthor());
            preparedStatement.setString(6, assistanceCall.getPhone());
            preparedStatement.setBoolean(7, assistanceCall.isBlocked());
            preparedStatement.setInt(8, assistanceCall.getReportsCounter());
            preparedStatement.setString(9, assistanceCall.getUrlPicture());
            preparedStatement.setInt(10, assistanceCall.getCategoryId());
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if(resultSet.next())
                return resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            ConnectionPool.getConnectionPool().checkIn(connection);
        }
        return null;
    }

    public boolean delete(int id)
    {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate()>0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            ConnectionPool.getConnectionPool().checkIn(connection);
        }
        return false;
    }

    public boolean update(int id, AssistanceCall assistanceCall)
    {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, assistanceCall.getName());
            preparedStatement.setString(2, dateTimeFormatter.format(assistanceCall.getDatetime()));
            preparedStatement.setString(3, assistanceCall.getLocation());
            preparedStatement.setString(4, assistanceCall.getDescription());
            preparedStatement.setString(5, assistanceCall.getAuthor());
            preparedStatement.setString(6, assistanceCall.getPhone());
            preparedStatement.setBoolean(7, assistanceCall.isBlocked());
            preparedStatement.setInt(8, assistanceCall.getReportsCounter());
            preparedStatement.setString(9, assistanceCall.getUrlPicture());
            preparedStatement.setInt(10, assistanceCall.getCategoryId());
            return preparedStatement.executeUpdate()>0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            ConnectionPool.getConnectionPool().checkIn(connection);
        }
        return false;
    }

    public boolean reportCall(int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(reportQuery);
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate()>0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            ConnectionPool.getConnectionPool().checkIn(connection);
        }
        return false;
    }

    public boolean block(int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(blockQuery);
            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate()>0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            ConnectionPool.getConnectionPool().checkIn(connection);
        }
        return false;
    }
}
