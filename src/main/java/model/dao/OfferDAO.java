package model.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import model.entities.Offer;

import java.util.List;

public class OfferDAO extends GenericDAO<Offer> {

    public OfferDAO() {
        super(Offer.class);
    }

    public List<Offer> findPendingOffersByUserId(int userId) {
        try (EntityManager em = getEntityManager()) {
            String jpql = "SELECT o FROM Offer o WHERE o.status = 'pending' AND o.productToOffer.user.idUser = :userId";
            return em.createQuery(jpql, Offer.class)
                    .setParameter("userId", userId)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public List<Offer> findAcceptedOffersByUserId(int userId) {
        try (EntityManager em = getEntityManager()) {
            String jpql = "SELECT o FROM Offer o WHERE o.status = 'accepted' AND o.isDelivered = false AND o.productToOffer.user.idUser = :userId";
            return em.createQuery(jpql, Offer.class)
                    .setParameter("userId", userId)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }


    public Offer findById(int id) {
        try (EntityManager em = getEntityManager()) {
            Offer offer = em.createQuery(
                            "SELECT o FROM Offer o " +
                                    "LEFT JOIN FETCH o.offeredProducts " +
                                    "LEFT JOIN FETCH o.productToOffer " +
                                    "WHERE o.idOffer = :id", Offer.class)
                    .setParameter("id", id)
                    .getSingleResult();

            offer.getOfferedProducts().size();

            return offer;
        } catch (NoResultException e) {
            return null;
        }
    }

    // Method to change the status of the offer to "pending".
    public boolean changeOfferStatusToPending(Offer offer) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            offer.setStatus("pending");
            em.merge(offer);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
            return false;
        }
    }
}

