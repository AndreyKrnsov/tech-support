package techSupport.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import techSupport.dto.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RequestsDAO {
    private static final Logger logger = LoggerFactory.getLogger(RequestsDAO.class);
    private Connection connection;

    public RequestsDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/java_kursach";
            String username = "root";
            String password = "root";
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            logger.error("Ошибка при подключении к mysql: ", e);
        }
    }

    public void addRequest(Request request) {
        String query = "INSERT INTO requests (description, user_id, number, create_date, status) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, request.getDescription());
            preparedStatement.setInt(2, request.getUserId());
            preparedStatement.setString(3, request.getNumber());
            preparedStatement.setObject(4, request.getCreateDate());
            preparedStatement.setString(5, request.getStatus().toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Ошибка при создании заявки: ", e);
        }
    }

    public List<Request> getOpenRequests() {
        String query = "SELECT * FROM requests WHERE status = 'open'";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet rs = preparedStatement.executeQuery();
            List<Request> requests = new ArrayList<>();
            while (rs.next()) {
                Request request = new Request();
                request.setRequestId(rs.getInt("request_id"));
                request.setDescription(rs.getString("description"));
                request.setUserId(rs.getInt("user_id"));
                request.setNumber(rs.getString("number"));
                Timestamp timestamp = rs.getTimestamp("create_date");
                if (timestamp != null) {
                    request.setCreateDate(timestamp.toInstant());
                }
                request.setStatus(Status.fromString(rs.getString("status")));
                request.setComment(rs.getString("comment"));
                request.setScore(rs.getInt("score"));
                requests.add(request);
            }
            return requests;
        } catch (SQLException e) {
            logger.error("Ошибка при получении отправленных запросов: ", e);
        }
        return null;
    }

    public List<Request> getRequestsByUserId(int userId) {
        String query = "SELECT * FROM requests WHERE user_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            ResultSet rs = preparedStatement.executeQuery();
            List<Request> requests = new ArrayList<>();
            while (rs.next()) {
                Request request = new Request();
                request.setRequestId(rs.getInt("request_id"));
                request.setDescription(rs.getString("description"));
                request.setUserId(rs.getInt("user_id"));
                request.setNumber(rs.getString("number"));
                Timestamp timestamp = rs.getTimestamp("create_date");
                if (timestamp != null) {
                    request.setCreateDate(timestamp.toInstant());
                }
                request.setStatus(Status.fromString(rs.getString("status")));
                request.setComment(rs.getString("comment"));
                request.setScore(rs.getInt("score"));
                requests.add(request);
            }
            return requests;
        } catch (SQLException e) {
            logger.error("Ошибка при получении запросов клиента (" + userId + "): ", e);
        }
        return null;
    }

    public Request getRequestById(int requestId) {
        String query = "SELECT * FROM requests WHERE request_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, requestId);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                Request request = new Request();
                request.setRequestId(rs.getInt("request_id"));
                request.setDescription(rs.getString("description"));
                request.setUserId(rs.getInt("user_id"));
                request.setNumber(rs.getString("number"));
                Timestamp timestamp = rs.getTimestamp("create_date");
                if (timestamp != null) {
                    request.setCreateDate(timestamp.toInstant());
                }
                request.setStatus(Status.fromString(rs.getString("status")));
                request.setComment(rs.getString("comment"));
                request.setScore(rs.getInt("score"));
                return request;
            }
            return null;
        } catch (SQLException e) {
            logger.error("Ошибка при получении запроса (" + requestId + "): ", e);
        }
        return null;
    }

    public void updateStatus(int requestId, String status) {
        String query = "UPDATE requests SET status = ? WHERE request_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, status);
            preparedStatement.setInt(2, requestId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Ошибка при обновлении статуса заявки (" + requestId + "): ", e);
        }
    }

    public void completeRequest(int requestId, String comment) {
        String query = "UPDATE requests SET comment = ?, status='COMPLETE' WHERE request_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, comment);
            preparedStatement.setInt(2, requestId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Ошибка при обновлении комментария заявки (" + requestId + "): ", e);
        }
    }


    public void updateScore(int requestId, int score) {
        String query = "UPDATE requests SET score = ? WHERE request_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, score);
            preparedStatement.setInt(2, requestId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Ошибка при обновлении оценки заявки (" + requestId + "): ", e);
        }
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
