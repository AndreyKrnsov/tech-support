package techSupport.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import techSupport.dao.RequestsDAO;
import techSupport.dao.TasksDAO;
import techSupport.dao.UsersDAO;
import techSupport.dto.*;

import java.io.IOException;
import java.io.Serial;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@WebServlet("/tasks")
public class TasksServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(TasksServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("sessionExpired.jsp");
        } else if (session.getAttribute("role") != Role.ADMINISTRATOR.toString() &&
                session.getAttribute("role") != Role.SUPPORT_STAFF.toString()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Доступ запрещен");
            return;
        } else {
            TasksDAO tasksDAO = new TasksDAO();
            RequestsDAO requestsDAO = new RequestsDAO();
            String action = request.getParameter("action");

            if (action == null) {
                logger.error("Не удалось получить данные на странице tasks (post)");
                request.getRequestDispatcher("tasks.jsp").forward(request, response);
                return;
            } else if (action.equals("description")) {
                String description = request.getParameter("description");
                int taskId = Integer.parseInt(request.getParameter("task-id"));
                tasksDAO.updateDescription(taskId, description);
            }  else if (action.equals("performer")) {
                int performerId = Integer.parseInt(request.getParameter("performer-id"));
                int taskId = Integer.parseInt(request.getParameter("task-id"));
                tasksDAO.updatePerformerId(taskId, performerId);
            } else if (action.equals("complete")) {
                String comment = request.getParameter("comment");
                int taskId = Integer.parseInt(request.getParameter("task-id"));
                Task task = tasksDAO.getTaskById(taskId);
                requestsDAO.completeRequest(task.getRequestId(), comment);
            }


            tasksDAO.closeConnection();
            requestsDAO.closeConnection();
            response.sendRedirect("tasks");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("sessionExpired.jsp");
        } else if (session.getAttribute("role") != Role.ADMINISTRATOR.toString() &&
                session.getAttribute("role") != Role.SUPPORT_STAFF.toString()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Доступ запрещен");
            return;
        } else {
            TasksDAO tasksDAO = new TasksDAO();
            UsersDAO usersDAO = new UsersDAO();
            RequestsDAO requestsDAO = new RequestsDAO();
            List<Task> tasks = tasksDAO.getTaskByStaffId(usersDAO.getUserId((String) session.getAttribute("user")));
            if (tasks != null) {
                tasks.sort(Comparator.comparingInt(Task::getRequestId).reversed());
                for (Task task : tasks) {
                    User client = usersDAO.getUserById(task.getClientId());
                    Request userRequest = requestsDAO.getRequestById(task.getRequestId());
                    if (task.getPerformerId() != 0) {
                        User performer = usersDAO.getUserById(task.getPerformerId());
                        task.setInfo(client.getLogin() + "/" +
                                userRequest.getNumber() + "/" +
                                performer.getFirstName() + " " +  performer.getLastName() + " (" + Profile.toText(performer.getProfile()) + ")"
                        );
                    } else {
                        task.setInfo(client.getLogin() + "/" +
                                userRequest.getNumber()
                        );
                    }

                }
            }
            List<User> performers = usersDAO.getPerformers();

            usersDAO.closeConnection();
            tasksDAO.closeConnection();
            requestsDAO.closeConnection();
            request.setAttribute("tasks", tasks);
            request.setAttribute("performers", performers);
            request.getRequestDispatcher("tasks.jsp").forward(request, response);
        }
    }
}
