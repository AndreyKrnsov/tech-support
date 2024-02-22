package techSupport.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;
import techSupport.dao.UsersDAO;
import techSupport.dto.Role;
import techSupport.dto.User;

import java.io.IOException;
import java.io.Serial;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Serial
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        if (firstName == null || lastName == null || login == null || password == null) {
            response.sendRedirect("register");
            return;
        }
        UsersDAO usersDAO = new UsersDAO();

        if (usersDAO.checkIfUserExists(login)) {
            request.setAttribute("errorMessage", "Пользователь с таким логином уже существует.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        // Создание нового пользователя
        User newUser = new User();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setLogin(login);
        newUser.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        newUser.setRole(Role.CLIENT);

        usersDAO.addUser(newUser);
        usersDAO.closeConnection();

        // Перенаправление на страницу входа после успешной регистрации
        response.sendRedirect("login");
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }
}
