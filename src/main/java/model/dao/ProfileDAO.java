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
        } catch (NoResultException e) {
            System.out.println(e.getMessage());
            return null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("An error occurred while trying to find the user by DNI");
            return null;
        }
    }

    public List<Product> findProductsByUserId(int userId) {
        try (EntityManager em = getEntityManager()) {
            String jpql = "SELECT p FROM Product p WHERE p.user.idUser = :userId";
            return em.createQuery(jpql, Product.class)
                    .setParameter("userId", userId)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
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
            System.out.println(e.getMessage());
            System.out.println("An error occurred while trying to find the user by ID");
            return null;
        }
    }
}
