package model.dao;

import jakarta.persistence.EntityManager;
import model.entities.Product;

import java.util.List;

public class ProductDAO extends GenericDAO<Product> {

    public ProductDAO() {
        super(Product.class);
    }

    public List<Product> findProductsByIdUser(int idUser) {
        try (EntityManager em = getEntityManager()) {
            String jpql = "SELECT p FROM Product p WHERE p.user.idUser = :idUser";
            return em.createQuery(jpql, Product.class)
                    .setParameter("idUser", idUser)
                    .getResultList();
        } catch (Exception e) {
            return List.of();
        }
    }
}
