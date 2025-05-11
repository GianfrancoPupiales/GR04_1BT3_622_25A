package model.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import model.entities.Favorite;
import model.entities.Offer;
import model.entities.Product;
import model.entities.User;

public class FavoriteDAO extends GenericDAO<Favorite>{
    public FavoriteDAO() {
        super(Favorite.class);
    }

    public Favorite findByUserAndProduct(User user, Product product) {
        try (EntityManager em = getEntityManager()) {
            return em.createQuery("SELECT f FROM Favorite f WHERE f.user = :user AND f.product = :product", Favorite.class)
                    .setParameter("user", user)
                    .setParameter("product", product)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
