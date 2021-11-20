package dao;

import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private String jdbcURL = "jdbc:mysql://localhost:3306/sakila?useSSl=false";
    private String jdbcUsername = "root";
    private String jdbcPassword = "3301";

    private static final String INSERT_USERS_SQL = "INSERT INTO users" + " (name, email, contract) VALUES "
            + " (?, ?, ?)";
    private static final String SELECT_USER_BY_ID = "select id, name, email, contract from users where id = ?";
    private static final String SELECT_ALL_USERS = "select * from users";
    private static final String DELETE_USERS_SQL = "delete from users where id = ?;";
    private static final String UPDATE_USERS_SQL = "update users set name = ?, email = ?, contract = ? where id = ?;";

    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }

    //Create user
    public void insertUser(User user)  throws SQLException {
        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getContract());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //Select user by id
    public User selectUser(int id) {
        User user = null;
        //установление соединения - establishing a connection
        try(Connection connection = getConnection();
            //создать заявление, используя объект подключения - create a statement using connection  object
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);) {
            preparedStatement.setInt(1, id);
            //выполнить запрос или обновить запрос - execute the query or update query
            ResultSet rs = preparedStatement.executeQuery();

            //обработать объект ResultSet - process the ResultSet object
            while (rs.next()) {
                String name = rs.getString("name");
                String email = rs.getString("email");
                String contract = rs.getString("contract");
                user = new User(id, name, email, contract);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
    //Select users
    public List<User> selectAllUsers() {
        List<User> users = new ArrayList<>();
        //установление соединения - establishing a connection
        try(Connection connection = getConnection();
            //создать заявление, используя объект подключения - create a statement using connection  object
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);) {
            //выполнить запрос или обновить запрос - execute the query or update query
            ResultSet rs = preparedStatement.executeQuery();

            //обработать объект ResultSet - process the ResultSet object
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                String contract = rs.getString("contract");
                users.add(new User(id, name, email, contract));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    //Delete user
    public boolean deleteUser(int id) throws SQLException {
        boolean rowDeleted;
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE_USERS_SQL)) {
            statement.setInt(1, id);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }
    //Update user
    public boolean updateUser(User user) throws SQLException {
        boolean rowUpdated;
        try(Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE_USERS_SQL)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getContract());
            statement.setInt(4, user.getId());

            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }
}