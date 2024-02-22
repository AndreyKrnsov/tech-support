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
import techSupport.dto.Status;
import techSupport.dto.Task;

import java.io.IOException;
import java.io.Serial;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@WebServlet("/new_requests")
public class NewRequestsServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(NewRequestsServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

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
            UsersDAO usersDAO = new UsersDAO();
            int requestId = Integer.parseInt(request.getParameter("request-id"));
            Request userRequest = requestsDAO.getRequestById(requestId);

            Task task = new Task();
            task.setRequestId(userRequest.getRequestId());
            task.setDescription(userRequest.getDescription());
            task.setClientId(userRequest.getUserId());
            int staffId = usersDAO.getUserId((String) session.getAttribute("user"));
            task.setStaffId(staffId);
            task.setStatus(Status.OPEN);
            task.setCreateDate(Instant.now());
            logger.info(task.toString());
            if (staffId != 0) {
                tasksDAO.addTask(task);
            }
            usersDAO.closeConnection();
            tasksDAO.closeConnection();
            requestsDAO.closeConnection();
            response.sendRedirect("new_requests");
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
            RequestsDAO requestsDAO = new RequestsDAO();
            UsersDAO usersDAO = new UsersDAO();
            List<Request> requests = requestsDAO.getOpenRequests();
            if (requests != null) {
                requests.sort(Comparator.comparingInt(Request::getRequestId).reversed());
                for (Request userRequest : requests) {
                    userRequest.setInfo(usersDAO.getUserById(userRequest.getUserId()).getLogin());
                }
            }

            requestsDAO.closeConnection();
            request.setAttribute("requests", requests);
            request.getRequestDispatcher("new_requests.jsp").forward(request, response);
        }
    }
}