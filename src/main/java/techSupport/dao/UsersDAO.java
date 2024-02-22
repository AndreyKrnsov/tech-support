package techSupport.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import techSupport.dto.Profile;
import techSupport.dto.Role;
import techSupport.dto.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsersDAO {

    private static final Logger logger = LoggerFactory.getLogger(UsersDAO.class);
    private Connection connection;

    public UsersDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/java_kursach";
            String username = "root";
            String password = "root";
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            logger.error("Ошибка при подключении к mysql: ", e);
            e.printStackTrace();
        }
    }

    public boolean checkIfUserExists(String login) {
        String sql = "SELECT COUNT(*) FROM users WHERE login = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addUser(User user) {
        String query;
        if (user.getProfile() != null){
            query = "INSERT INTO users (firstname, lastname, login, password, role, profile) VALUES (?, ?, ?, ?, ?, ?)";
        } else {
            query = "INSERT INTO users (firstname, lastname, login, password, role) VALUES (?, ?, ?, ?, ?)";
        }

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getLogin());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getRole().toString());
            if (user.getProfile() != null){
                preparedStatement.setString(6, user.getProfile().toString());
            }
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Ошибка при создании пользователя: ", e);
            e.printStackTrace();
        }
    }

    public User getUser(String login) {
        String query = "SELECT * FROM users WHERE login = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, login);
            ResultSet rs = preparedStatement.executeQuery();
            User user = new User();
            if (rs.next()) {
                user.setUserId(rs.getInt("user_id"));
                user.setFirstName(rs.getString("firstname"));
                user.setLastName(rs.getString("lastname"));
                user.setLogin(rs.getString("login"));
                user.setPassword(rs.getString("password"));
                user.setRole(Role.fromString(rs.getString("role")));
                user.setProfile(Profile.fromString(rs.getString("profile")));
            }
            return user;
        } catch (SQLException e) {
            logger.error("Ошибка получении пользователя из бд: ", e);
        }
        return null;
    }

    public User getUserById(int userId) {
        String query = "SELECT * FROM users WHERE user_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            ResultSet rs = preparedStatement.executeQuery();
            User user = new User();
            if (rs.next()) {
                user.setUserId(rs.getInt("user_id"));
                user.setFirstName(rs.getString("firstname"));
                user.setLastName(rs.getString("lastname"));
                user.setLogin(rs.getString("login"));
                user.setPassword(rs.getString("password"));
                user.setRole(Role.fromString(rs.getString("role")));
                user.setProfile(Profile.fromString(rs.getString("profile")));
            }
            return user;
        } catch (SQLException e) {
            logger.error("Ошибка получении пользователя по id: ", e);
        }
        return null;
    }

    public int getUserId(String login) {
        String query = "SELECT user_id FROM users WHERE login = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, login);
            ResultSet rs = preparedStatement.executeQuery();
            int userId = 0;
            if (rs.next()) {
                userId = rs.getInt("user_id");
            }
            return userId;
        } catch (SQLException e) {
            logger.error("Ошибка получении id пользователя: ", e);
        }
        return 0;
    }

    public List<User> getPerformers() {
        String query = "SELECT * FROM users WHERE role = 'PERFORMER'";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet rs = preparedStatement.executeQuery();
            List<User> users = new ArrayList<>();
            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setFirstName(rs.getString("firstname"));
                user.setLastName(rs.getString("lastname"));
                user.setLogin(rs.getString("login"));
                user.setPassword(rs.getString("password"));
                user.setRole(Role.fromString(rs.getString("role")));
                user.setProfile(Profile.fromString(rs.getString("profile")));
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            logger.error("Ошибка получении исполнителей из бд: ", e);
        }
        return null;
    }

    public List<User> getStaff() {
        String query = "SELECT * FROM users WHERE role = 'SUPPORT_STAFF'";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet rs = preparedStatement.executeQuery();
            List<User> users = new ArrayList<>();
            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setFirstName(rs.getString("firstname"));
                user.setLastName(rs.getString("lastname"));
                user.setLogin(rs.getString("login"));
                user.setPassword(rs.getString("password"));
                user.setRole(Role.fromString(rs.getString("role")));
                user.setProfile(Profile.fromString(rs.getString("profile")));
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            logger.error("Ошибка получении работников поддержки из бд: ", e);
        }
        return null;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
