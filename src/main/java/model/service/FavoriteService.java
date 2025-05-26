package model.service;

import model.dao.FavoriteDAO;
import model.entities.Favorite;
import model.entities.Product;
import model.entities.User;
import model.utils.FavoriteValidator;

import java.util.List;

public class FavoriteService {
    private final FavoriteDAO favoriteDAO;
    private final FavoriteValidator favoriteValidator;

    public FavoriteService() {
        this.favoriteDAO = new FavoriteDAO();
        this.favoriteValidator = new FavoriteValidator(favoriteDAO);
    }

    // Constructor para pruebas
    public FavoriteService(FavoriteDAO favoriteDAO) {
        this.favoriteDAO = favoriteDAO;
        this.favoriteValidator = new FavoriteValidator(favoriteDAO);
    }

    public void addFavorite(User user, Product product) {
        favoriteValidator.validate(user, product);
        createFavorite(user, product);
    }

    private void createFavorite(User user, Product product) {
        favoriteDAO.create(new Favorite(user, product));
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
