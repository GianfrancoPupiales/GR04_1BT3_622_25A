package controllers;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
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
    private EntityManagerFactory emf;

    @Override
    public void init() {
        emf = Persistence.createEntityManagerFactory("TuUnidadDePersistencia");
        FavoriteDAO favoriteDAO = new FavoriteDAO(emf);  // DAO ahora usa emf
        favoriteService = new FavoriteService(favoriteDAO);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Debes iniciar sesión");
            return;
        }

        User user = (User) session.getAttribute("user");
        int productId = Integer.parseInt(request.getParameter("productId"));

        Product product = new Product();
        product.setIdProduct(productId);

        boolean removed = favoriteService.removeFavorite(user, product);

        System.out.println("Favorito eliminado: " + removed);

        response.sendRedirect("favorites.jsp?removed=" + removed);
    }

    @Override
    public void destroy() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

}
