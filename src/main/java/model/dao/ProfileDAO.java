package model.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import model.entities.Product;
import model.entities.Profile;
import model.entities.User;

import java.util.Collections;
import java.util.List;

public class ProfileDAO extends GenericDAO<Profile> {

    public ProfileDAO() {
        super(Profile.class);
    }

    public Profile findProfileByUserId(int idUser) {
        try (EntityManager em = getEntityManager()) {
            String jpql = "SELECT p FROM Profile p WHERE p.user.idUser = :idUser";
            return em.createQuery(jpql, Profile.class)
                    .setParameter("idUser", idUser)
                    .getSingleResult();
        } catch (Exception e) {
            return handleException(e, "Error fetching profile by user ID", null);
        }
    }

    public List<Product> findProductsByUserId(int userId) {
        try (EntityManager em = getEntityManager()) {
            String jpql = "SELECT p FROM Product p WHERE p.user.idUser = :userId and p.isAvailable = true";
            return em.createQuery(jpql, Product.class)
                    .setParameter("userId", userId)
                    .getResultList();
        } catch (Exception e) {
            return handleException(e, "Error fetching products by user ID", Collections.emptyList());
        }
    }

    public User findUserById(int userId) {
        try (EntityManager em = getEntityManager()) {
            String jpql = "SELECT u FROM User u WHERE u.idUser = :userId";
            return em.createQuery(jpql, User.class)
                    .setParameter("userId", userId)
                    .getSingleResult();
        } catch (NoResultException e) {
            System.out.println(e.getMessage());
            return null;
        } catch (Exception e) {
            return handleException(e, "Error fetching user by ID", null);
        }
    }

    private <T> T handleException(Exception e, String contextMessage, T defaultValue) {
        if (e instanceof NoResultException) {
            System.out.println("NoResultException: " + e.getMessage());
            System.out.println(contextMessage + " (no result found)");
        } else {
            System.out.println("Exception: " + e.getMessage());
            System.out.println(contextMessage);
        }
        return defaultValue;
    }

}
