package model.dao;

import jakarta.persistence.*;
import model.entities.Favorite;
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
        try (EntityManager em = getEntityManager()) {
            return em.createQuery(
                            "SELECT f FROM Favorite f WHERE f.user.idUser = :idUser", Favorite.class)
                    .setParameter("idUser", user.getUserId())
                    .getResultList();
        }
    }

    public boolean deleteByUserAndProduct(User user, Product product) {
        try (EntityManager em = getEntityManager()) {
            em.getTransaction().begin();
            Favorite favorite = em.createQuery(
                            "SELECT f FROM Favorite f WHERE f.user = :user AND f.product = :product", Favorite.class)
                    .setParameter("user", user)
                    .setParameter("product", product)
                    .getSingleResult();
            em.remove(em.contains(favorite) ? favorite : em.merge(favorite));
            em.getTransaction().commit();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }
}
