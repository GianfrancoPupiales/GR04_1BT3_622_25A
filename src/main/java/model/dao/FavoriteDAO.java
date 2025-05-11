package model.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import model.entities.Favorite;
import model.entities.Offer;
import model.entities.Product;
import model.entities.User;

import java.util.List;

public class FavoriteDAO extends GenericDAO<Favorite>{
    private EntityManagerFactory emf;

    public FavoriteDAO() {
        super(Favorite.class);
    }
    public FavoriteDAO(EntityManagerFactory emf) {
        super(Favorite.class);
        this.emf = emf;
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

    public boolean deleteByUserAndProduct(User user, Product product) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            TypedQuery<Favorite> query = em.createQuery(
                    "SELECT f FROM Favorite f WHERE f.user.idUser = :userId AND f.product.idProduct = :productId",
                    Favorite.class
            );
            query.setParameter("userId", user.getIdUser());
            query.setParameter("productId", product.getIdProduct());

            List<Favorite> favorites = query.getResultList();
            if (favorites.isEmpty()) return false;

            tx.begin();
            for (Favorite favorite : favorites) {
                em.remove(em.contains(favorite) ? favorite : em.merge(favorite));
            }
            tx.commit();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            if (tx.isActive()) tx.rollback();
            return false;
        } finally {
            em.close();
        }

    }

    public List<Favorite> findAll() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("SELECT f FROM Favorite f", Favorite.class).getResultList();
        } finally {
            em.close();
        }
    }

    public Favorite save(Favorite favorite) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(favorite);
            tx.commit();
            return favorite;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }


}
