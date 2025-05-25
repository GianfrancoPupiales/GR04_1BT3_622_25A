package model.utils;

import model.dao.FavoriteDAO;
import model.entities.Favorite;
import model.entities.Product;
import model.entities.User;

import java.util.List;

public class FavoriteValidator {
    private final FavoriteDAO favoriteDAO;
    private static final String VALID_TITLE_REGEX = "^[a-zA-Z0-9 ]+$";
    private static final int MAX_FAVORITES = 20;

    public FavoriteValidator(FavoriteDAO favoriteDAO) {
        this.favoriteDAO = favoriteDAO;
    }

    public void validate(User user, Product product) {
        validateUserAndProduct(user, product);
        validateProductTitle(product);
        validateLimit(user);
        validateNotAlreadyFavorite(user, product);
    }

    private void validateUserAndProduct(User user, Product product) {
        if (user == null) throw new IllegalArgumentException("El usuario no está autenticado.");
        if (product == null) throw new IllegalArgumentException("El producto no existe.");
    }

    private void validateProductTitle(Product product) {
        String title = product.getTitle();
        if (title == null || !title.matches(VALID_TITLE_REGEX)) {
            throw new IllegalArgumentException("The title of the product contains invalid characters.");
        }
    }

    private void validateLimit(User user) {
        List<Favorite> currentFavorites = favoriteDAO.findByUser(user);
        if (currentFavorites.size() >= MAX_FAVORITES) {
            throw new IllegalStateException("Cannot have more than " + MAX_FAVORITES + " favorites");
        }
    }

    private void validateNotAlreadyFavorite(User user, Product product) {
        if (favoriteDAO.findByUserAndProduct(user, product) != null) {
            throw new IllegalStateException("Product is already in favorites");
        }
    }
}
