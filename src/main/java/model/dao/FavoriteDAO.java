package model.dao;

import jakarta.persistence.*;
import model.entities.Favorite;
import model.entities.Product;
import model.entities.User;

import java.util.List;

public class FavoriteDAO extends GenericDAO<Favorite>{
    private EntityManagerFactory emf;

    public FavoriteDAO() {
        super(Favorite.class);
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
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
            return em.createQuery("SELECT f FROM Favorite f WHERE f.user.idUser = :userId", Favorite.class)
                    .setParameter("userId", user.getIdUser())
                    .getResultList();
        } finally {
            em.close();
        }
    }



    public Object deleteByUserAndProduct(User user, Product product) {
    return null;
    }
}
