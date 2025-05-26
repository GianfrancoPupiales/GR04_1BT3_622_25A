package model.dao;

import jakarta.persistence.EntityManager;
import model.entities.Product;
import model.enums.ProductCategory;
import model.enums.ProductState;

import java.util.List;

public class ProductDAO extends GenericDAO<Product> {

    private final EntityManager em;

    public ProductDAO() {
        super(Product.class);
        this.em = getEntityManager(); // crea uno real para producción
    }

    public ProductDAO(EntityManager em) {
        super(Product.class);
        this.em = em;
    }

    public List<Product> findProductsByUserId(int idUser) {
        try {
            String jpql = "SELECT p FROM Product p WHERE p.user.idUser = :idUser";
            return em.createQuery(jpql, Product.class)
                    .setParameter("idUser", idUser)
                    .getResultList();
        } catch (Exception e) {
            return List.of();
        }
    }

    public List<Product> findAvailableProductsByUserId(int idUser) {
        try {
            String jpql = "SELECT p FROM Product p WHERE p.user.idUser = :idUser AND p.isAvailable = true";
            return em.createQuery(jpql, Product.class)
                    .setParameter("idUser", idUser)
                    .getResultList();
        } catch (Exception e) {
            return List.of();
        }
    }

    public List<Product> findProductById(int idProduct) {
        try {
            String jpql = "SELECT p FROM Product p WHERE p.idProduct = :idProduct";
            return em.createQuery(jpql, Product.class)
                    .setParameter("idProduct", idProduct)
                    .getResultList();
        } catch (Exception e) {
            return List.of();
        }
    }


    public boolean updateProductAvailability(List<Product> products, boolean available) {
        try {
            em.getTransaction().begin();
            for (Product product : products) {
                product.setAvailable(available);
                em.merge(product);
            }
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            e.printStackTrace();
            return false;
        }
    }

    public List<Product> findAvailableProductsExceptUser(int userId) {
        try {
            String jpql = "SELECT p FROM Product p WHERE p.user.idUser != :userId AND p.isAvailable = true";
            return em.createQuery(jpql, Product.class)
                    .setParameter("userId", userId)
                    .getResultList();
        } catch (Exception e) {
            return List.of();
        }
    }

    public List<Product> getProductsByCategory(ProductCategory category) {
        try {
            if (category == null) {
                return findAll();
            }
            String jpql = "SELECT p FROM Product p WHERE p.category = :category";
            return em.createQuery(jpql, Product.class)
                    .setParameter("category", category)
                    .getResultList();
        } catch (Exception e) {
            return List.of();
        }
    }

    public List<Product> getProductsByTitle(String title) {
        try {
            String jpql = "SELECT p FROM Product p WHERE LOWER(p.title) LIKE LOWER(:title)";
            return em.createQuery(jpql, Product.class)
                    .setParameter("title", "%" + title + "%")
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace(); // para depuración, puedes quitarlo en producción
            return List.of();
        }
    }

    public List<Product> getProductsByState(ProductState state) {
        return null;
    }
}