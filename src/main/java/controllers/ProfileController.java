package controllers;

import jakarta.servlet.ServletException;

import java.io.Serial;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.entities.Profile;
import model.entities.User;
import model.service.FileStorageService;
import model.service.ProfileService;

import java.io.IOException;

@WebServlet("/ProfileController")
@MultipartConfig
public class ProfileController extends HttpServlet {

    @Serial
    private static final long serialVersionUID = 1L;

    //AGREGAR
    private static final String SUCCESS = "success";
    private static final String ERROR = "error";
    private static final String PROFILE_UPDATED = "Profile updated successfully.";
    private static final String PROFILE_UPDATE_ERROR = "Error updating the profile.";
    private static final String UNEXPECTED_ERROR = "An unexpected error occurred.";
    private static final String REQUIRED_FIELDS_ERROR = "All required fields must be filled out.";

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
            case "editForm":
                this.showEditForm(req, resp);
                break;
            case "edit":
                this.edit(req, resp);
                break;
            case "public":
                this.showPublicProfile(req, resp);
                break;
            default:
                throw new IllegalArgumentException("Unknown route: " + route);
        }
    }

    private void showPublicProfile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String idParam = req.getParameter("id");
            if (idParam == null || idParam.isEmpty()) {
                throw new IllegalArgumentException("El parámetro 'id' es obligatorio.");
            }

            int userId = Integer.parseInt(idParam);
            ProfileService profileService = new ProfileService();
            Profile profile = profileService.getFullProfileWithProducts(userId);

            if (profile == null) {
                req.setAttribute("messageType", "error");
                req.setAttribute("message", "El perfil no existe.");
                req.getRequestDispatcher("jsp/HOME.jsp").forward(req, resp);
                return;
            }

            req.setAttribute("profile", profile);
            req.setAttribute("from", req.getParameter("from"));
            req.getRequestDispatcher("jsp/PROFILE.jsp").forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("messageType", "error");
            req.setAttribute("message", "Ocurrió un error al cargar el perfil.");
            req.getRequestDispatcher("jsp/HOME.jsp").forward(req, resp);
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
        return new Profile(0, firstName, lastName, null, description, getUser(req));
    }


    private void showEditForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getUser(req);
        ProfileService profileService = new ProfileService();
        Profile profile = profileService.getProfileByUserId(user.getUserId());

        if (profile == null || profile.getIdProfile() == 0) {
            throw new IllegalArgumentException("The profile does not exist or has an invalid ID.");
        }

        req.setAttribute("profile", profile);
        req.getRequestDispatcher("jsp/MY_PROFILE.jsp?route=editForm").forward(req, resp);
    }


    private void edit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProfileService profileService = new ProfileService();
        try {
            String idParam = req.getParameter("id");
            validateId(idParam);

            int id = Integer.parseInt(req.getParameter("id"));
            String firstName = req.getParameter("firstName");
            String lastName = req.getParameter("lastName");
            String description = req.getParameter("description");

            User user = getUser(req);

            if (isRequiredFieldEmpty(firstName, lastName)) {
                handleValidationError(req, resp, profileService, user);
                return;
            }

            Part photoPart = req.getPart("photoFile");
            String photo = saveProfilePhoto(photoPart);

            Profile profileUpdate = new Profile(id, firstName, lastName, photo, description, user);
            System.out.println("Profile update: " + profileUpdate.getPhoto());
            boolean success = profileService.updateProfile(profileUpdate);

            handleUpdateResult(req, profileService, user, success);
            req.getRequestDispatcher("jsp/MY_PROFILE.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("messageType", "error");
            req.setAttribute("message", "An unexpected error occurred.");
            req.getRequestDispatcher("jsp/MY_PROFILE.jsp").forward(req, resp);
        }
    }

    private void handleValidationError(HttpServletRequest req, HttpServletResponse resp, ProfileService service, User user)
            throws ServletException, IOException {
        req.setAttribute("messageType", ERROR);
        req.setAttribute("message", REQUIRED_FIELDS_ERROR);
        req.setAttribute("profile", service.getProfileByUserId(user.getUserId()));
        req.getRequestDispatcher("jsp/MY_PROFILE.jsp").forward(req, resp);
    }

    private void handleUpdateResult(HttpServletRequest req, ProfileService service, User user, boolean success) {
        req.setAttribute("messageType", success ? SUCCESS : ERROR);
        req.setAttribute("message", success ? PROFILE_UPDATED : PROFILE_UPDATE_ERROR);
        req.setAttribute("profile", service.getProfileByUserId(user.getUserId()));
    }

    private static boolean isRequiredFieldEmpty(String firstName, String lastName) {
        return firstName == null || firstName.trim().isEmpty() ||
                lastName == null || lastName.trim().isEmpty();
    }

    private static void validateId(String idParam) {
        if (idParam == null || idParam.isEmpty()) {
            throw new IllegalArgumentException("The id parameter is required.");
        }
    }


    private String saveProfilePhoto(Part photoPart) throws IOException {
        String photo = null;
        String uploadPath = getServletContext().getRealPath("/images");

        FileStorageService photoStorageService = new FileStorageService(uploadPath);
        photo = photoStorageService.savePhoto(photoPart);

        return photo;
    }
}
