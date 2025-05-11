package model.service;

import jakarta.persistence.EntityManager;
import model.dao.FavoriteDAO;
import model.entities.Favorite;
import model.entities.Product;
import model.entities.User;

import java.util.List;

public class FavoriteService {
    private final FavoriteDAO favoriteDAO;

    public FavoriteService() {
        this.favoriteDAO = new FavoriteDAO();
    }

    // Este constructor se usa para pruebas
    public FavoriteService(FavoriteDAO favoriteDAO) {
        this.favoriteDAO = favoriteDAO;
    }

    public void addFavorite(User user, Product product) {
        /*
        if (user == null) {
            throw new IllegalArgumentException("El usuario no está autenticado.");
        }

        Favorite existing = favoriteDAO.findByUserAndProduct(user, product);
        if (existing != null) {
            System.out.println("Advertencia: El producto ya está en favoritos.");
            return;
        }
*/
        Favorite favorite = new Favorite(user, product);
        favoriteDAO.create(favorite);
    }

    public List<Favorite> getFavoritesByUser(User user) {
        List<Favorite> list = favoriteDAO.findByUser(user);
        System.out.println("Favoritos encontrados: " + list.size());
        return list;
    }

    public boolean removeFavorite(User user, Product product) {
        if (user == null || product == null) {
            throw new IllegalArgumentException("Usuario o producto inválido");
        }
        return favoriteDAO.deleteByUserAndProduct(user, product);
    }

    public boolean isProductAlreadyFavorite(User user, Product product) {
        return favoriteDAO.findByUserAndProduct(user, product) != null;
    }
}
