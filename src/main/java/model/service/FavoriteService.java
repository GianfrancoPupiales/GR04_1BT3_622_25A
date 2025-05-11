package model.service;

import model.dao.FavoriteDAO;
import model.entities.Favorite;
import model.entities.Product;
import model.entities.User;

import java.util.List;

public class FavoriteService {
    private final FavoriteDAO favoriteDAO;
    private static final String VALID_TITLE_REGEX = "^[a-zA-Z0-9 ]+$";


    public FavoriteService() {
        this.favoriteDAO = new FavoriteDAO();
    }

    // Este constructor se usa para pruebas
    public FavoriteService(FavoriteDAO favoriteDAO) {
        this.favoriteDAO = favoriteDAO;
    }

    public void addFavorite(User user, Product product) {
        validateUserAndProduct(user, product);
        validateProductTitle(product);

        if (isFavorite(user, product)) {
            System.out.println("Advertencia: El producto ya está en favoritos.");
            return;
        }

        createFavorite(user, product);
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

    private boolean isFavorite(User user, Product product) {
        return favoriteDAO.findByUserAndProduct(user, product) != null;
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
