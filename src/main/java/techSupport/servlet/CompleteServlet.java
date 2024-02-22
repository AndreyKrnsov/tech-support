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

@WebServlet("/complete")
public class CompleteServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(CompleteServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("sessionExpired.jsp");
            return;
        } else if (session.getAttribute("role") != Role.ADMINISTRATOR.toString() &&
                session.getAttribute("role") != Role.SUPPORT_STAFF.toString()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Доступ запрещен");
            return;
        } else {
            TasksDAO tasksDAO = new TasksDAO();
            UsersDAO usersDAO = new UsersDAO();
            int staffId = usersDAO.getUserId((String) session.getAttribute("user"));
            List<Task> tasks = tasksDAO.getCompletedTasksByStaffId(staffId);
            if (tasks != null) {
                tasks.sort(Comparator.comparingInt(Task::getRequestId).reversed());
                for (Task task : tasks) {
                    User client = usersDAO.getUserById(task.getClientId());
                    task.setInfo(task.getInfo() + "/" + client.getLogin());
                }
            }
            List<User> performers = usersDAO.getPerformers();


            request.setAttribute("tasks", tasks);
            request.setAttribute("countTask", tasksDAO.getStatisticByStaffId(staffId).split("/")[0]);
            request.setAttribute("avgScore", tasksDAO.getStatisticByStaffId(staffId).split("/")[1]);
            usersDAO.closeConnection();
            tasksDAO.closeConnection();
            request.getRequestDispatcher("complete.jsp").forward(request, response);
        }
    }
}
