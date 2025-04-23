package controllers;

import jakarta.servlet.ServletException;

import java.io.Serial;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.entities.User;
import model.service.UserService;

import java.io.IOException;

@WebServlet("/RegisterController")
public class RegisterController extends HttpServlet {

    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.router(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.router(req, resp);
    }

    private void router(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Control logic
        String route = (req.getParameter("route") == null) ? "register" : req.getParameter("route");

        switch (route) {
            case "enter":
                this.enter(resp);
                break;
            case "save":
                this.save(req, resp);
                break;
            default:
                throw new IllegalArgumentException("Unknown route: " + route);
        }
    }

    private void enter(HttpServletResponse resp) throws IOException {
        resp.sendRedirect("jsp/REGISTER.jsp");
    }

    private void save(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            User user = parseUserFromRequest(req);
            UserService userService = new UserService();

            if (userService.createUser(user)) {
                req.setAttribute("messageType", "info");
                req.setAttribute("message", "User registered successfully! You can now log in.");
                req.getRequestDispatcher("jsp/LOGIN.jsp").forward(req, resp);
            } else {
                req.setAttribute("messageType", "error");
                req.setAttribute("message", "Failed to register the user. Please try again.");
                req.getRequestDispatcher("jsp/REGISTER.jsp").forward(req, resp);
            }
        } catch (IllegalArgumentException e) {
            req.setAttribute("messageType", "error");
            req.setAttribute("message", e.getMessage());
            req.getRequestDispatcher("jsp/REGISTER.jsp").forward(req, resp);
        }
    }

    private User parseUserFromRequest(HttpServletRequest req) {
        String dni = req.getParameter("txtDni");
        String password = req.getParameter("txtPassword");
        return new User(0, dni, password);
    }
}
