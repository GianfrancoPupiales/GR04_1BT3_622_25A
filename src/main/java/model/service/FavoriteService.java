package model.service;

import model.dao.FavoriteDAO;
import model.entities.Favorite;
import model.entities.Product;
import model.entities.User;

import java.util.List;

public class FavoriteService {
    private FavoriteDAO favoriteDAO;
    public FavoriteService(FavoriteDAO favoriteDAO) {
        this.favoriteDAO = favoriteDAO;
    }

    public void addFavorite(User user, Product product) {
        if (user == null) {
            throw new IllegalArgumentException("El usuario no está autenticado.");
        }

        List<Favorite> existingFavorites = favoriteDAO.findAll();
        boolean alreadyExists = existingFavorites.stream()
                .anyMatch(fav -> fav.getUser().getIdUser() == user.getIdUser()
                        && fav.getProduct().getIdProduct() == product.getIdProduct());

        if (alreadyExists) {
            throw new IllegalStateException("El producto ya está en favoritos.");
        }

        Favorite newFavorite = new Favorite();
        newFavorite.setUser(user);
        newFavorite.setProduct(product);
        favoriteDAO.save(newFavorite);
    }



    public List<Favorite> getFavoritesByUser(User user) {
        return favoriteDAO.findAllByUser(user);
    }

    public boolean removeFavorite(User user, Product product) {
        if (user == null || product == null) {
            throw new IllegalArgumentException("Usuario o producto no pueden ser nulos.");
        }
        return favoriteDAO.deleteByUserAndProduct(user, product);
    }

    public void close() {
        // Liberar recursos si es necesario
        favoriteDAO.close();
    }
}
