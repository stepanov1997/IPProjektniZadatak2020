package model.dao;

import model.dto.Call;
import util.ConnectionPool;

import java.io.Serializable;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CallDao implements Serializable {
    private static final String getAllQuery = "SELECT * FROM assistancecall";
    private static final String getQuery = "SELECT * FROM assistancecall WHERE id=?";
    private static final String insertQuery =
            "INSERT INTO assistancecall (name, datetime, location, description, author, phone, isBlocked, " +
                    "reportsCounter, urlPicture, CategoryOfCalls_id) VALUES (?,?,?,?,?,?,?,?,?,?)";
    private static final String updateQuery =
            "UPDATE assistancecall SET name=?, datetime=?, location=?, description=?, author=?, phone=?, isBlocked=?, " +
                    "reportsCounter=?, urlPicture=?, CategoryOfCalls_id=? WHERE id=?";
    private static final String reportQuery = "UPDATE assistancecall SET reportsCounter=reportsCounter+1 WHERE id=?";
    private static final String deleteQuery = "DELETE FROM assistancecall WHERE id=?";


    public List<Call> getAll()
    {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        List<Call> list = new ArrayList<>();
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(getAllQuery);
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            while (resultSet.next())
            {
                Call Call = new Call(
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
                list.add(Call);
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

    public Call get(int id)
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
                return new Call(
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

    public Integer add(Call Call)
    {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, Call.getName());
            preparedStatement.setString(2, dateTimeFormatter.format(Call.getDatetime()));
            preparedStatement.setString(3, Call.getLocation());
            preparedStatement.setString(4, Call.getDescription());
            preparedStatement.setString(5, Call.getAuthor());
            preparedStatement.setString(6, Call.getPhone());
            preparedStatement.setBoolean(7, Call.isBlocked());
            preparedStatement.setInt(8, Call.getReportsCounter());
            preparedStatement.setString(9, Call.getUrlPicture());
            preparedStatement.setInt(10, Call.getCategoryId());
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

    public boolean update(int id, Call Call)
    {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, Call.getName());
            preparedStatement.setString(2, dateTimeFormatter.format(Call.getDatetime()));
            preparedStatement.setString(3, Call.getLocation());
            preparedStatement.setString(4, Call.getDescription());
            preparedStatement.setString(5, Call.getAuthor());
            preparedStatement.setString(6, Call.getPhone());
            preparedStatement.setBoolean(7, Call.isBlocked());
            preparedStatement.setInt(8, Call.getReportsCounter());
            preparedStatement.setString(9, Call.getUrlPicture());
            preparedStatement.setInt(10, Call.getCategoryId());
            preparedStatement.setInt(11, id);
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
}
