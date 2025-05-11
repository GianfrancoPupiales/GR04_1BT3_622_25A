package controllers;

import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.servlet.http.HttpSession;
import model.entities.Favorite;
import model.service.FavoriteService;
import model.service.ProductService;
import model.service.UserService;
import model.entities.Product;
import model.entities.User;

import java.io.IOException;
import java.io.Serial;
import java.util.List;

@WebServlet("/FavoriteController")
public class FavoriteController extends HttpServlet {
    private static EntityManagerFactory entityManagerFactory;
    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.route(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.route(req, resp);
    }

    private void route(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("route");

        switch (action) {
            case "add":
                this.addFavorite(req, resp);
                break;

            case "listFavorites":
                this.listFavorites(req, resp);
                break;

            default:
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }

    private void addFavorite(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int idProduct = Integer.parseInt(req.getParameter("idProduct"));
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        FavoriteService favoriteService = new FavoriteService();

        ProductService productService = new ProductService();
        Product product = productService.findById(idProduct);

        favoriteService.addFavorite(user, product);
        resp.sendRedirect(req.getContextPath() + "/FavoriteController?route=listFavorites");

    }

    private void listFavorites(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        FavoriteService favoriteService = new FavoriteService();

        List<Favorite> favorites = favoriteService.getFavoritesByUser(user);
        req.setAttribute("favorites", favorites);
        req.getRequestDispatcher("/jsp/FAVORITES.jsp").forward(req, resp);
    }

}

