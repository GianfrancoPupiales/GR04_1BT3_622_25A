package model.service;

import jakarta.persistence.EntityManager;
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
            System.out.println("Usuario no encontrado");
            throw new IllegalArgumentException("El usuario no está autenticado.");
        }

        if (product == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo.");
        }

        // Validación: ¿El producto ya está en favoritos?
        List<Favorite> existingFavorites = favoriteDAO.findAll();
        boolean alreadyExists = existingFavorites.stream()
                .anyMatch(fav -> fav.getUser().getIdUser() == user.getIdUser()
                        && fav.getProduct().getIdProduct() == product.getIdProduct());

        if (alreadyExists) {
            System.out.println("El producto ya existe en la favorita.");
            throw new IllegalStateException("El producto ya está en favoritos.");
        }

        // Validación: Caracteres inválidos en título del producto
        if (product.getTitle() != null && !product.getTitle().matches("^[a-zA-Z0-9\\s]+$")) {
            throw new IllegalArgumentException("The title of the product contains invalid characters.");
        }

        Favorite favorite = new Favorite(user, product);
        favoriteDAO.save(favorite);
    }



    public List<Favorite> getFavoritesByUser(User user) {
        EntityManager em = favoriteDAO.getEntityManager();
        try {
            return em.createQuery("SELECT f FROM Favorite f WHERE f.user.idUser = :idUser", Favorite.class)
                    .setParameter("idUser", user.getIdUser())
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public boolean removeFavorite(User user, Product product) {
        if (user == null || product == null) {
            throw new IllegalArgumentException("Usuario o producto no pueden ser nulos.");
        }
        return favoriteDAO.deleteByUserAndProduct(user, product);
    }
}
