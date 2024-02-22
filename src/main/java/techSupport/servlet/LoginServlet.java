package techSupport.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;
import techSupport.dao.UsersDAO;
import techSupport.dto.User;

import java.io.IOException;
import java.io.Serial;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Serial
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        String login = request.getParameter("login");
        String password = request.getParameter("password");

        UsersDAO usersDAO = new UsersDAO();
        User user = usersDAO.getUser(login);
        if (user == null) {
            request.setAttribute("errorMessage", "Неверный логин или пароль.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        } else if (user.getPassword() == null) {
            request.setAttribute("errorMessage", "Неверный логин или пароль.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        } else if (!BCrypt.checkpw(password, user.getPassword())) {
            request.setAttribute("errorMessage", "Неверный логин или пароль.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();

        session.setAttribute("user", login);
        session.setAttribute("role", user.getRole().toString());
        if (user.getRole().toString().equals("CLIENT")) {
            response.sendRedirect("client");
        } else if (user.getRole().toString().equals("SUPPORT_STAFF")) {
            response.sendRedirect("new_requests");
        } else if (user.getRole().toString().equals("PERFORMER")) {
            response.sendRedirect("performer");
        } else if (user.getRole().toString().equals("ADMINISTRATOR")) {
            response.sendRedirect("add_user");
        } else {
            response.sendRedirect("login");
        }

    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }
}
