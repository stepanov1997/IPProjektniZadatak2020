package model.dao;

import model.dto.AssistanceCall;
import model.dto.CategoryCall;
import util.ConnectionPool;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryCallDao implements Serializable {
    private static final String getAllQuery = "SELECT * FROM categoryofcall";
    private static final String insertQuery = "INSERT INTO categoryofcall SET name=?";

    public List<CategoryCall> getAll() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        List<CategoryCall> list = new ArrayList<>();
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(getAllQuery);
            while (resultSet.next()) {
                CategoryCall CategoryCall = new CategoryCall();
                CategoryCall.setId(resultSet.getInt("id"));
                CategoryCall.setName(resultSet.getString("name"));
                list.add(CategoryCall);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            list = new ArrayList<>();
        } finally {
            ConnectionPool.getConnectionPool().checkIn(connection);
        }
        return list;
    }

    public Integer add(CategoryCall CategoryCall) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, CategoryCall.getName());
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                Integer id = resultSet.getInt(1);
                CategoryCall.setId(id);
                return id;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionPool.getConnectionPool().checkIn(connection);
        }
        return null;
    }

    public boolean delete(int id) {
        return false;
    }

    public boolean update(int id, AssistanceCall assistanceCall) {
        return false;
    }
}
