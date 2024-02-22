package techSupport.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;
import techSupport.dao.UsersDAO;
import techSupport.dto.Profile;
import techSupport.dto.Role;
import techSupport.dto.User;

import java.io.IOException;
import java.io.Serial;

@WebServlet("/add_user")
public class AddUserServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("sessionExpired.jsp");
        } else if (session.getAttribute("role") != Role.ADMINISTRATOR.toString()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Доступ запрещен");
            return;
        } else {
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            Role role = Role.fromString(request.getParameter("role"));
            Profile profile = Profile.fromString(request.getParameter("profile"));
            String login = request.getParameter("login");
            String password = request.getParameter("password");

            if (firstName == null || lastName == null || login == null || password == null) {
                response.sendRedirect("add_user");
                return;
            }
            UsersDAO usersDAO = new UsersDAO();

            if (usersDAO.checkIfUserExists(login)) {
                request.setAttribute("errorMessage", "Пользователь с такой почтой уже существует.");
                request.getRequestDispatcher("add_user.jsp").forward(request, response);
                return;
            }

            // Создание нового пользователя
            User newUser = new User();
            newUser.setFirstName(firstName);
            newUser.setLastName(lastName);
            newUser.setLogin(login);
            newUser.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
            newUser.setRole(role);
            if (role == Role.PERFORMER) {
                newUser.setProfile(profile);
            }
            usersDAO.addUser(newUser);

            if (usersDAO.checkIfUserExists(login)) {
                request.setAttribute("successMessage", "Пользователь успешно создан");
                request.getRequestDispatcher("add_user.jsp").forward(request, response);
                return;
            }

            usersDAO.closeConnection();

            response.sendRedirect("add_user");
        }
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("sessionExpired.jsp");
        } else if (session.getAttribute("role") != Role.ADMINISTRATOR.toString()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Доступ запрещен");
        } else {
            request.getRequestDispatcher("add_user.jsp").forward(request, response);
        }
    }
}