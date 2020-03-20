package model.dao;

import model.dto.Picture;
import util.ConnectionPool;

import java.sql.*;

public class PictureDao {
    private static final String selectOneQuery = "SELECT * FROM picture WHERE id=?;";
    private static final String addQuery = "INSERT INTO picture (fileName,picture) values (?,?);";
    private static final String addPictureToUserQuery = "UPDATE user SET picture_id=? WHERE id=?;";

    public PictureDao() {}

    public Picture get(int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(selectOneQuery);
            preparedStatement.setInt(1, id);
            preparedStatement.executeQuery();

            resultSet = preparedStatement.getResultSet();

            if (resultSet.next()) {
                Picture picture = new Picture();
                picture.setId(id);
                picture.setFileName(resultSet.getString("fileName"));
                picture.setImg(resultSet.getBytes("picture"));
                return picture;
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
        return null;
    }

    public boolean add(int accountId, Picture picture) {
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet generatedKeys = null;
        ResultSet resultSet = null;
        int id = -1;
        try {
            con = ConnectionPool.getConnectionPool().checkOut();

            preparedStatement = con.prepareStatement(addQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, picture.getFileName());
            preparedStatement.setBytes(2, picture.getImg());
            preparedStatement.executeUpdate();
            generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                id = generatedKeys.getInt(1);
                picture.setId(id);
            }
            if(id==-1) return false;

            preparedStatement = con.prepareStatement(addPictureToUserQuery);
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, accountId);
            return preparedStatement.executeUpdate()>0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                generatedKeys.close();
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                ConnectionPool.getConnectionPool().checkIn(con);
            }
        }
        return false;
    }
}
