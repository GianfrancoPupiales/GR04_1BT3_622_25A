package model.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import model.entities.Reputation;
import model.entities.User;

public class ReputationDAO extends GenericDAO<Reputation> {

    public ReputationDAO() {
        super(Reputation.class);
    }

    public Reputation findByUserId(int userId) {
        try (EntityManager em = getEntityManager()) {
            return em.createQuery("SELECT r FROM Reputation r WHERE r.user.idUser = :userId", Reputation.class)
                    .setParameter("userId", userId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;  // Si no hay resultado, retorna null
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public void save(Reputation reputation) {
        try (EntityManager em = getEntityManager()) {
            em.getTransaction().begin();

            if (reputation.getId() == 0) {
                em.persist(reputation);
            } else {
                em.merge(reputation);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
