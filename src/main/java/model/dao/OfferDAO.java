package model.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import model.entities.Offer;
import model.entities.Product;

import java.util.List;

public class OfferDAO extends GenericDAO<Offer>{
    public OfferDAO() {
        super(Offer.class);
    }

    public List<Offer> findPendingOffersByUserId(int userId) {
        try (EntityManager em = getEntityManager()) {
            String jpql = "SELECT o FROM Offer o WHERE o.status = 'pending' AND o.productToOffer IN " +
                    "(SELECT p.idProduct FROM Product p WHERE p.user.idUser = :userId)";
            return em.createQuery(jpql, Offer.class)
                    .setParameter("userId", userId)
                    .getResultList();
        } catch (Exception e) {
            return List.of();
        }
    }
    public Offer findById(int id) {
        try (EntityManager em = getEntityManager()) {
            return em.find(Offer.class, id);
        }
    }
    public boolean update(Offer offer) {
        EntityManager em = getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(offer);
            tx.commit();
            return true; // Devuelve true si se actualizó correctamente
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace(); // Opcional: registrar el error
            return false; // Devuelve false si ocurrió un error
        } finally {
            em.close();
        }
    }

}

