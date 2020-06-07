package model.dao;

import model.dto.DngCategory;
import util.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DngCategoryDao {

    private static final String getAllQuery = "SELECT * FROM dangercategory";
    private static final String getQuery = "SELECT * FROM dangercategory WHERE id=?";
    private static final String insertQuery = "INSERT INTO dangercategory SET name=?";;

    public List<DngCategory> getAll() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        List<DngCategory> list = new ArrayList<>();
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(getAllQuery);

            while (resultSet.next()) {
                DngCategory DngCategory = new DngCategory();
                DngCategory.setId(resultSet.getInt(1));
                DngCategory.setName(resultSet.getString(2));
                list.add(DngCategory);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionPool.getConnectionPool().checkIn(connection);
        }
        return list;
    }

    public DngCategory get(int category_id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        DngCategory DngCategory = null;
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(getQuery);
            preparedStatement.setInt(1, category_id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                DngCategory = new DngCategory();
                DngCategory.setId(resultSet.getInt(1));
                DngCategory.setName(resultSet.getString(2));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionPool.getConnectionPool().checkIn(connection);
        }
        return DngCategory;
    }

    public Integer add(DngCategory dngCategory) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, dngCategory.getName());
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                Integer id = resultSet.getInt(1);
                dngCategory.setId(id);
                return id;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectionPool.getConnectionPool().checkIn(connection);
        }
        return null;
    }
}
