package model.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import model.entities.User;

public class UserDAO extends GenericDAO<User> {
    public UserDAO() {
        super(User.class);
    }

    public User findUserByDni(String dni) {
        try (EntityManager em = getEntityManager()) {
            return em.createQuery("SELECT u FROM User u WHERE u.dni = :dni", User.class)
                    .setParameter("dni", dni)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            System.out.println("An error occurred while trying to find the user by DNI");
            return null;
        }
    }
}
