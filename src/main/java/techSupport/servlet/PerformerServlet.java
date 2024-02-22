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
import techSupport.dto.Request;
import techSupport.dto.Role;
import techSupport.dto.Task;
import techSupport.dto.User;

import java.io.IOException;
import java.io.Serial;
import java.util.Comparator;
import java.util.List;

@WebServlet("/performer")
public class PerformerServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(PerformerServlet.class);


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("sessionExpired.jsp");
        } else if (session.getAttribute("role") != Role.PERFORMER.toString()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Доступ запрещен");
            return;
        } else {
            TasksDAO tasksDAO = new TasksDAO();

            String comment = request.getParameter("comment");
            int taskId = Integer.parseInt(request.getParameter("task-id"));
            tasksDAO.completeTask(taskId, comment);

            tasksDAO.closeConnection();
            response.sendRedirect("performer");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("sessionExpired.jsp");
        } else if (session.getAttribute("role") != Role.PERFORMER.toString()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Доступ запрещен");
            return;
        } else {
            TasksDAO tasksDAO = new TasksDAO();
            UsersDAO usersDAO = new UsersDAO();
            List<Task> tasks = tasksDAO.getTaskByPerformerId(usersDAO.getUserId((String) session.getAttribute("user")));
            if (tasks != null) {
                tasks.sort(Comparator.comparingInt(Task::getTaskId).reversed());
                for (Task task : tasks) {
                    User staff = usersDAO.getUserById(task.getStaffId());
                    task.setInfo(staff.getFirstName() + "/" + staff.getLastName() + "/" + staff.getLogin());
                }
            }

            tasksDAO.closeConnection();
            usersDAO.closeConnection();
            request.setAttribute("tasks", tasks);
            request.getRequestDispatcher("performer.jsp").forward(request, response);
        }
    }

}
