package model.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import model.entities.Offer;
import model.service.ProductService;

import java.util.Collections;
import java.util.List;

public class OfferDAO extends GenericDAO<Offer> {
    private EntityManagerFactory entityManagerFactory;

    private EntityManager entityManager;
    public OfferDAO() {
        super(Offer.class);
    }
    public OfferDAO(EntityManager entityManager) {
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
            return true;
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }


    public boolean updateOffer(Offer offer) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(offer);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean confirmDeliveryAndUpdateOffer(Offer offer) {
        EntityManager em = getEntityManager(); // Usamos el mismo EntityManager
        ProductService productService = new ProductService();

        try {
            em.getTransaction().begin(); // Comenzamos la transacción

            // Actualizamos los productos en la misma transacción
            productService.disableProductsInOffer(offer);

            // Marcamos la oferta como entregada
            offer.markAsDelivered(offer.getOfferedByUser());

            // Ahora actualizamos la oferta en la base de datos
            em.merge(offer);

            em.getTransaction().commit(); // Confirmamos la transacción
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            em.close();
        }
    }




    // Método para cambiar el estado de la oferta a "pending"
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

