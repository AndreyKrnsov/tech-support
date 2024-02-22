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
import techSupport.dao.UsersDAO;
import techSupport.dto.Request;
import techSupport.dto.Role;
import techSupport.dto.Status;
import techSupport.dto.User;

import java.io.IOException;
import java.io.Serial;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@WebServlet("/client")
public class ClientServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(ClientServlet.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("sessionExpired.jsp");
            return;
        } else if (session.getAttribute("role") != Role.ADMINISTRATOR.toString() &&
                session.getAttribute("role") != Role.CLIENT.toString() &&
                session.getAttribute("role") != Role.SUPPORT_STAFF.toString() &&
                session.getAttribute("role") != Role.PERFORMER.toString()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Доступ запрещен");
            return;
        } else {
            String action = request.getParameter("action");

            if (action == null) {
                logger.error("Не удалось получить данные на странице client (post)");
                request.getRequestDispatcher("client.jsp").forward(request, response);
                return;
            }

            RequestsDAO requestsDAO = new RequestsDAO();
            UsersDAO usersDAO = new UsersDAO();
            User user = usersDAO.getUser((String) session.getAttribute("user"));

            if (action.equals("create request")) {
                String number = request.getParameter("number");
                String description = request.getParameter("description");
                Request newRequest = new Request();
                newRequest.setDescription(description);
                newRequest.setUserId(user.getUserId());
                newRequest.setNumber(number);
                newRequest.setCreateDate(Instant.now());
                newRequest.setStatus(Status.OPEN);
                requestsDAO.addRequest(newRequest);
                usersDAO.closeConnection();
                requestsDAO.closeConnection();
                response.sendRedirect("client");
                return;
            } else if (action.equals("set score")) {
                int requestId = Integer.parseInt(request.getParameter("request-id"));
                int score = Integer.parseInt(request.getParameter("score"));
                requestsDAO.updateScore(requestId, score);
                response.sendRedirect("client");
                usersDAO.closeConnection();
                requestsDAO.closeConnection();
                return;
            }
        }
        response.sendRedirect("client");


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("sessionExpired.jsp");
        } else if (session.getAttribute("role") != Role.ADMINISTRATOR.toString() &&
                session.getAttribute("role") != Role.CLIENT.toString() &&
                session.getAttribute("role") != Role.SUPPORT_STAFF.toString() &&
                session.getAttribute("role") != Role.PERFORMER.toString()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Доступ запрещен");
            return;
        } else {
            RequestsDAO requestsDAO = new RequestsDAO();
            UsersDAO usersDAO = new UsersDAO();
            User user = usersDAO.getUser((String) session.getAttribute("user"));
            List<Request> requests = requestsDAO.getRequestsByUserId(user.getUserId());
            if (requests != null) {
                requests.sort(Comparator.comparingInt(Request::getRequestId).reversed());
            }


            usersDAO.closeConnection();
            requestsDAO.closeConnection();
            request.setAttribute("requests", requests);
            request.getRequestDispatcher("client.jsp").forward(request, response);
        }
    }
}
