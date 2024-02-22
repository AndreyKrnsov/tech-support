package techSupport.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import techSupport.dto.Status;
import techSupport.dto.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TasksDAO {
    private static final Logger logger = LoggerFactory.getLogger(TasksDAO.class);
    private Connection connection;

    public TasksDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/java_kursach";
            String username = "root";
            String password = "root";
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            logger.error("Ошибка при подключении к mysql: " + e.getMessage());
        }
    }


    public void addTask(Task task) {
        String taskQuery = "INSERT INTO tasks (description, request_id, client_id, staff_id, status, create_date) VALUES (?, ?, ?, ?, ?, ?)";
        String requestQuery = "UPDATE requests SET status = 'IN_PROGRESS' WHERE request_id = ?";

        try (PreparedStatement taskPreparedStatement = connection.prepareStatement(taskQuery);
        PreparedStatement requestPreparedStatement = connection.prepareStatement(requestQuery)) {
            setAutoCommit(connection, false);
            taskPreparedStatement.setString(1, task.getDescription());
            taskPreparedStatement.setInt(2, task.getRequestId());
            taskPreparedStatement.setInt(3, task.getClientId());
            taskPreparedStatement.setInt(4, task.getStaffId());
            taskPreparedStatement.setString(5, task.getStatus().toString());
            taskPreparedStatement.setObject(6, task.getCreateDate());
            taskPreparedStatement.executeUpdate();

            requestPreparedStatement.setInt(1, task.getRequestId());
            requestPreparedStatement.executeUpdate();

        } catch (SQLException e) {
            rollbackTransaction(connection);
            logger.error("Ошибка при создании задачи: ", e);
        } finally {
            setAutoCommit(connection, true);
        }
    }

    public List<Task> getTaskByStaffId(int staffId) {
        String query = "SELECT * FROM tasks INNER JOIN requests ON tasks.request_id = requests.request_id " +
                "WHERE tasks.staff_id = ? AND requests.status != 'COMPLETE'";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, staffId);
            ResultSet rs = preparedStatement.executeQuery();
            List<Task> tasks = new ArrayList<>();
            while (rs.next()) {
                Task task = new Task();
                task.setTaskId(rs.getInt("task_id"));
                task.setDescription(rs.getString("description"));
                task.setRequestId(rs.getInt("request_id"));
                task.setClientId(rs.getInt("client_id"));
                task.setStaffId(rs.getInt("staff_id"));
                task.setPerformerId(rs.getInt("performer_id"));
                task.setStatus(Status.fromString(rs.getString("tasks.status")));
                Timestamp timestamp = rs.getTimestamp("create_date");
                if (timestamp != null) {
                    task.setCreateDate(timestamp.toInstant());
                }
                task.setComment(rs.getString("comment"));
                tasks.add(task);
            }
            return tasks;
        } catch (SQLException e) {
            logger.error("Ошибка при получении задач сотрудника поддержки (" + staffId + "): ", e);
        }
        return null;
    }

    public List<Task> getCompletedTasksByStaffId(int staffId) {
        String query = "SELECT * FROM tasks INNER JOIN requests ON tasks.request_id = requests.request_id " +
                "WHERE tasks.staff_id = ? AND requests.status = 'COMPLETE'";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, staffId);
            ResultSet rs = preparedStatement.executeQuery();
            List<Task> tasks = new ArrayList<>();
            while (rs.next()) {
                Task task = new Task();
                task.setTaskId(rs.getInt("task_id"));
                task.setDescription(rs.getString("description"));
                task.setRequestId(rs.getInt("request_id"));
                task.setClientId(rs.getInt("client_id"));
                task.setStaffId(rs.getInt("staff_id"));
                task.setPerformerId(rs.getInt("performer_id"));
                task.setStatus(Status.fromString(rs.getString("tasks.status")));
                task.setInfo(rs.getInt("requests.score") + "/" + rs.getString("requests.number"));
                Timestamp timestamp = rs.getTimestamp("create_date");
                if (timestamp != null) {
                    task.setCreateDate(timestamp.toInstant());
                }
                task.setComment(rs.getString("comment"));
                tasks.add(task);
            }
            return tasks;
        } catch (SQLException e) {
            logger.error("Ошибка при получении задач сотрудника поддержки (" + staffId + "): ", e);
        }
        return null;
    }

    public Task getTaskById(int taskId) {
        String query = "SELECT * FROM tasks WHERE task_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, taskId);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                Task task = new Task();
                task.setTaskId(rs.getInt("task_id"));
                task.setDescription(rs.getString("description"));
                task.setRequestId(rs.getInt("request_id"));
                task.setClientId(rs.getInt("client_id"));
                task.setStaffId(rs.getInt("staff_id"));
                task.setPerformerId(rs.getInt("performer_id"));
                task.setStatus(Status.fromString(rs.getString("tasks.status")));
                Timestamp timestamp = rs.getTimestamp("create_date");
                if (timestamp != null) {
                    task.setCreateDate(timestamp.toInstant());
                }
                task.setComment(rs.getString("comment"));
                return task;
            }
        } catch (SQLException e) {
            logger.error("Ошибка при получении задачи (" + taskId + "): ", e);
        }
        return null;
    }

    public List<Task> getTaskByPerformerId(int performerId) {
        String query = "SELECT * FROM tasks WHERE performer_id = ? AND status != 'COMPLETE'";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, performerId);
            ResultSet rs = preparedStatement.executeQuery();
            List<Task> tasks = new ArrayList<>();
            while (rs.next()) {
                Task task = new Task();
                task.setTaskId(rs.getInt("task_id"));
                task.setDescription(rs.getString("description"));
                task.setRequestId(rs.getInt("request_id"));
                task.setClientId(rs.getInt("client_id"));
                task.setStaffId(rs.getInt("staff_id"));
                task.setPerformerId(rs.getInt("performer_id"));
                task.setStatus(Status.fromString(rs.getString("tasks.status")));
                Timestamp timestamp = rs.getTimestamp("create_date");
                if (timestamp != null) {
                    task.setCreateDate(timestamp.toInstant());
                }
                task.setComment(rs.getString("comment"));
                tasks.add(task);
            }
            return tasks;
        } catch (SQLException e) {
            logger.error("Ошибка при получении задач исполнителя (" + performerId + "): ", e);
        }
        return null;
    }

    public void updateDescription(int taskId, String description) {
        String query = "UPDATE tasks SET description = ? WHERE task_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, description);
            preparedStatement.setInt(2, taskId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Ошибка при обновлении описания задачи (" + taskId + "): ", e);
        }
    }

    public void completeTask(int taskId, String comment) {
        String query = "UPDATE tasks SET comment = ?, status='COMPLETE' WHERE task_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, comment);
            preparedStatement.setInt(2, taskId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Ошибка при обновлении комментария решения задачи (" + taskId + "): ", e);
        }
    }

    public void updatePerformerId(int taskId, int performerId) {
        String query = "UPDATE tasks SET performer_id = ?, status='IN_PROGRESS' WHERE task_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, performerId);
            preparedStatement.setInt(2, taskId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Ошибка при обновлении ид исполнителя задачи (" + taskId + "): ", e);
        }
    }

    public String getStatisticByStaffId(int stuffId) {
        String countQuery = "SELECT COUNT(*) as count FROM tasks INNER JOIN requests ON tasks.request_id = requests.request_id " +
                "WHERE tasks.staff_id = ? AND requests.status = 'COMPLETE'";
        String avgQuery = "SELECT AVG(score) as average FROM tasks INNER JOIN requests ON tasks.request_id = requests.request_id " +
                "WHERE tasks.staff_id = ? AND requests.status = 'COMPLETE' AND requests.score != 0";
        String stat = "";
        try (PreparedStatement countPreparedStatement = connection.prepareStatement(countQuery);
             PreparedStatement avgPreparedStatement = connection.prepareStatement(avgQuery)) {
            countPreparedStatement.setInt(1, stuffId);
            avgPreparedStatement.setInt(1, stuffId);
            ResultSet rs = countPreparedStatement.executeQuery();
            if (rs.next()) {
                stat = String.valueOf(rs.getInt("count"));
            }
            rs = avgPreparedStatement.executeQuery();
            if (rs.next()) {
                stat += "/" + String.format("%.1f", rs.getDouble("average"));
            }
        } catch (SQLException e) {
            logger.error("Ошибка при получении статистики сотрудника (" + stuffId + "): ", e);
        }
        return stat;
    }

    public String getStatisticByAllStaff() {
        String countQuery = "SELECT COUNT(*) as count FROM tasks INNER JOIN requests ON tasks.request_id = requests.request_id " +
                "WHERE requests.status = 'COMPLETE'";
        String avgQuery = "SELECT AVG(score) as average FROM tasks INNER JOIN requests ON tasks.request_id = requests.request_id " +
                "WHERE requests.status = 'COMPLETE' AND requests.score != 0";
        String stat = "";
        try (PreparedStatement countPreparedStatement = connection.prepareStatement(countQuery);
             PreparedStatement avgPreparedStatement = connection.prepareStatement(avgQuery)) {
            ResultSet rs = countPreparedStatement.executeQuery();
            if (rs.next()) {
                stat = String.valueOf(rs.getInt("count"));
            }
            rs = avgPreparedStatement.executeQuery();
            if (rs.next()) {
                stat += "/" + String.format("%.1f", rs.getDouble("average"));
            }
        } catch (SQLException e) {
            logger.error("Ошибка при получении статистики сотрудников: ", e);
        }
        return stat;
    }


    //  default
    public static void setAutoCommit(Connection connection, boolean isAutoCommit) {
        if (connection != null) {
            try {
                connection.setAutoCommit(isAutoCommit);
            } catch (SQLException e) {
                logger.error("Ошибка при setAutoCommit: " + e.getMessage());
            }
        }
    }

    public static void rollbackTransaction(Connection connection) {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                logger.error("Ошибка при откате транзакции: " + e.getMessage());
            }
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
