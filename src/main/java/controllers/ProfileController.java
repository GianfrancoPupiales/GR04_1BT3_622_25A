package controllers;

import jakarta.servlet.ServletException;

import java.io.Serial;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.entities.Product;
import model.entities.Profile;
import model.entities.User;
import model.service.ProductService;
import model.service.ProfileService;
import model.service.UserService;

import java.io.IOException;
import java.util.List;

@WebServlet("/ProfileController")
public class ProfileController extends HttpServlet {

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
            case "show":
                this.show(req, resp);
                break;
            case "save":
                this.save(req, resp);
                break;
            default:
                throw new IllegalArgumentException("Unknown route: " + route);
        }
    }

    private void show(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String viewType = req.getParameter("show");
        ProfileService profileService = new ProfileService();
        Profile profile = new Profile();
        // Mostrar productos del usuario (MY_PRODUCT.jsp)
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        profile = profileService.getProfileByUserId(user.getUserId());
        req.setAttribute("profile", profile);
        req.getRequestDispatcher("jsp/MY_PROFILE.jsp").forward(req, resp);
        //resp.sendRedirect("jsp/MY_PROFILE.jsp");
    }

    private void save(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Profile profile = parseProfileFromRequest(req);
            ProfileService profileService = new ProfileService();

            if (profileService.createProfile(profile)) {
                req.setAttribute("messageType", "info");
                req.setAttribute("message", "User registered successfully! You can now log in.");
                req.getRequestDispatcher("jsp/LOGIN.jsp").forward(req, resp);
            } else {
                req.setAttribute("messageType", "error");
                req.setAttribute("message", "Failed to register the user. Please try again.");
                req.getRequestDispatcher("jsp/PROFILE_REGISTER.jsp").forward(req, resp);
            }
        } catch (IllegalArgumentException e) {
            req.setAttribute("messageType", "error");
            req.setAttribute("message", e.getMessage());
            req.getRequestDispatcher("jsp/PROFILE_REGISTER.jsp").forward(req, resp);
        }
    }

    private static User getUser(HttpServletRequest req) {
        HttpSession session = req.getSession();
        return (User) session.getAttribute("user");
    }

    private Profile parseProfileFromRequest(HttpServletRequest req) {
        String firstName = req.getParameter("firstName");
        String lastName = req.getParameter("lastName");
        String description = req.getParameter("description");
        return new Profile(0, firstName, lastName, "photo", description, getUser(req));
    }


}
