package model.dao;

import model.dto.AssistanceCall;
import model.dto.Category;
import util.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao {
    private static final String getAllQuery = "SELECT * FROM categoryofcall";
    private static final String insertQuery = "INSERT INTO categoryofcall SET name=?";

    public List<Category> getAll() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        List<Category> list = new ArrayList<>();
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(getAllQuery);
            while (resultSet.next()) {
                Category category = new Category();
                category.setId(resultSet.getInt("id"));
                category.setName(resultSet.getString("name"));
                list.add(category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            list = new ArrayList<>();
        } finally {
            ConnectionPool.getConnectionPool().checkIn(connection);
        }
        return list;
    }

    public AssistanceCall get(int id) {
        return null;
    }

    public Integer add(Category category) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, category.getName());
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                Integer id = resultSet.getInt(1);
                category.setId(id);
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
