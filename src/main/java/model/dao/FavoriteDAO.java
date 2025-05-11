package model.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import model.entities.Favorite;
import model.entities.Offer;
import model.entities.Product;
import model.entities.User;

import java.util.List;

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

    public List<Favorite> findByUser(User user) {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery(
                            "SELECT f FROM Favorite f WHERE f.user.idUser = :idUser", Favorite.class)
                    .setParameter("idUser", user.getIdUser())
                    .getResultList();
        } finally {
            em.close();
        }
    }

}
