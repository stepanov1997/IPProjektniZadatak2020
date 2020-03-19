package model.dao;

import model.dto.Account;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import util.ConnectionPool;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDao {

    private static final String selectAllQuery = "SELECT * FROM user";
    private static final String selectOneQuery = "SELECT * FROM user WHERE id=?";
    private static final String addQuery = "INSERT INTO user (name, surname, username, password, email) VALUES (?, ?, ?, ?, ?)";
    private static final String deleteQuery = "DELETE FROM user WHERE id = ?";
    private static final String updateQuery = "UPDATE user SET name=?, surname=?, username=?, password=?, email=?, picture=?, country=?, region=?, city=? WHERE id=?";
    private static final String countByUsernameQuery = "SELECT COUNT(*) as number FROM user WHERE username=?";
    private static final String countByEmailQuery = "SELECT COUNT(*) as number FROM user WHERE email=?";

    public AccountDao() {}

    public List<Account> getAll()
    {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        List<Account> accounts = new ArrayList<>();
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            statement = connection.createStatement();
            statement.executeQuery(selectAllQuery);

            resultSet = statement.getResultSet();

            if (resultSet.next()) {
                Account account = new Account();
                account.setId(resultSet.getInt("id"));
                account.setName(resultSet.getString("name"));
                account.setSurname(resultSet.getString("surname"));
                account.setUsername(resultSet.getString("username"));
                account.setPassword(resultSet.getString("password"));
                account.setCountry(resultSet.getString("country"));
                account.setRegion(resultSet.getString("region"));
                account.setCity(resultSet.getString("city"));
                account.setPicture_Id(resultSet.getInt("picture_id"));
                accounts.add(account);
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
        return accounts;
    }
    public int add(@NotNull Account account) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(addQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, account.getName());
            preparedStatement.setString(2, account.getSurname());
            preparedStatement.setString(3, account.getUsername());
            preparedStatement.setString(4, account.getPassword());
            preparedStatement.setString(5, account.getEmail());
            preparedStatement.executeUpdate();

            resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                int id = resultSet.getInt(1);
                account.setId(id);
                return id;
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
        return -1;
    }

    public boolean remove(@NotNull Account account) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, account.getId());
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

    public boolean update(@NotNull Account account) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(updateQuery);
            //UPDATE user SET name=?, surname=?, username=?, password=?, email=?, country=?, region=?, city=? WHERE id=?
            preparedStatement.setString(1, account.getName());
            preparedStatement.setString(2, account.getSurname());
            preparedStatement.setString(3, account.getUsername());
            preparedStatement.setString(4, account.getPassword());
            preparedStatement.setString(5, account.getEmail());
            preparedStatement.setString(6, account.getCountry());
            preparedStatement.setString(7, account.getRegion());
            preparedStatement.setString(8, account.getCity());
            preparedStatement.setInt(9, account.getId());
            int res = preparedStatement.executeUpdate();
            return res > 0;
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

    @Nullable
    private Account get(int id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Account account = null;
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(selectOneQuery);
            preparedStatement.setInt(1, id);
            preparedStatement.executeQuery();

            resultSet = preparedStatement.getResultSet();

            if (resultSet.next()) {
                account = new Account();
                account.setName(resultSet.getString("name"));
                account.setSurname(resultSet.getString("surname"));
                account.setUsername(resultSet.getString("username"));
                account.setPassword(resultSet.getString("password"));
                account.setCountry(resultSet.getString("country"));
                account.setRegion(resultSet.getString("region"));
                account.setCity(resultSet.getString("city"));
                account.setPicture_Id(resultSet.getInt("picture_id"));
                return account;
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
        return account;
    }

    public boolean existsByUsername(String username) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Account account = null;
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(countByUsernameQuery);
            preparedStatement.setString(1, username);
            preparedStatement.executeQuery();

            resultSet = preparedStatement.getResultSet();

            if (resultSet.next()) {
                int number = resultSet.getInt("number");
                return number>0;
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

    public boolean existsByEmail(String email) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Account account = null;
        try {
            connection = ConnectionPool.getConnectionPool().checkOut();
            preparedStatement = connection.prepareStatement(countByEmailQuery);
            preparedStatement.setString(1, email);
            preparedStatement.executeQuery();

            resultSet = preparedStatement.getResultSet();

            if (resultSet.next()) {
                int number = resultSet.getInt("number");
                return number>0;
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
}
