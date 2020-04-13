package model.dao;

import model.dto.DangerCategory;
import util.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DangerCategoryDao {

    private static final String getAllQuery = "SELECT * FROM dangercategory";
    private static final String getQuery = "SELECT * FROM dangercategory WHERE id=?";

    public List<DangerCategory> getAll() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        List<DangerCategory> list = new ArrayList<>();
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(getAllQuery);

            while (resultSet.next()) {
                DangerCategory dangerCategory = new DangerCategory();
                dangerCategory.setId(resultSet.getInt(1));
                dangerCategory.setName(resultSet.getString(2));
                list.add(dangerCategory);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionPool.getConnectionPool().checkIn(connection);
        }
        return list;
    }

    public DangerCategory get(int category_id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        DangerCategory dangerCategory = null;
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(getQuery);
            preparedStatement.setInt(1, category_id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                dangerCategory = new DangerCategory();
                dangerCategory.setId(resultSet.getInt(1));
                dangerCategory.setName(resultSet.getString(2));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            ConnectionPool.getConnectionPool().checkIn(connection);
        }
        return dangerCategory;
    }
}
