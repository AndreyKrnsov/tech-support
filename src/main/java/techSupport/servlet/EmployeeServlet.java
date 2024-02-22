package techSupport.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import techSupport.dao.TasksDAO;
import techSupport.dao.UsersDAO;
import techSupport.dto.Role;
import techSupport.dto.Task;
import techSupport.dto.User;

import java.io.IOException;
import java.io.Serial;
import java.util.Comparator;
import java.util.List;

@WebServlet("/employee")
public class EmployeeServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(EmployeeServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("sessionExpired.jsp");
            return;
        } else if (session.getAttribute("role") != Role.ADMINISTRATOR.toString()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Доступ запрещен");
            return;
        } else {
            TasksDAO tasksDAO = new TasksDAO();
            UsersDAO usersDAO = new UsersDAO();
            List<User> employees = usersDAO.getStaff();
            if (employees != null) {
                employees.sort(Comparator.comparingInt(User::getUserId).reversed());
                for (User employee : employees) {
                    employee.setInfo(tasksDAO.getStatisticByStaffId(employee.getUserId()));
                }
            }

            request.setAttribute("employees", employees);
            request.setAttribute("countTask", tasksDAO.getStatisticByAllStaff().split("/")[0]);
            request.setAttribute("avgScore", tasksDAO.getStatisticByAllStaff().split("/")[1]);
            usersDAO.closeConnection();
            tasksDAO.closeConnection();
            request.getRequestDispatcher("employee.jsp").forward(request, response);
        }
    }
}