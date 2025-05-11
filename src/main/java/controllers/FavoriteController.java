package controllers;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.dao.FavoriteDAO;
import model.entities.Product;
import model.entities.User;
import model.service.FavoriteService;

import java.io.IOException;

@WebServlet("/favorite")
public class FavoriteController extends HttpServlet {

    private FavoriteService favoriteService;

    @Override
    public void init() throws ServletException {
        favoriteService = new FavoriteService(new FavoriteDAO());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String action = request.getParameter("action");
        String productIdStr = request.getParameter("productId");

        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            request.setAttribute("error", "El usuario no está autenticado.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        int productId = Integer.parseInt(productIdStr);
        Product product = new Product();
        product.setIdProduct(productId);

        try {
            if ("add".equals(action)) {
                favoriteService.addFavorite(user, product);
            } else if ("remove".equals(action)) {
                boolean removed = favoriteService.removeFavorite(user, product);
                if (!removed) {
                    request.setAttribute("error", "El producto no estaba en favoritos.");
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                    return;
                }
            }
            response.sendRedirect("favorites.jsp");
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    @Override
    public void destroy() {
        // Cierra recursos si es necesario, si el EntityManagerFactory está abierto
        if (favoriteService != null) {
            favoriteService.close();
        }
    }

}
