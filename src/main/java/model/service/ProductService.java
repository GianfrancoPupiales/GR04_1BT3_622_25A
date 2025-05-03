package model.service;

import model.dao.ProductDAO;
import model.entities.Product;

import java.util.List;

public class ProductService {
    private final ProductDAO productDAO;

    public ProductService() {
        productDAO = new ProductDAO();
    }

    public List<Product> findProductsByUserId(int userId) {
        return productDAO.findProductsByUserId(userId);
    }

    public List<Product> findAvailableProductsByUserId(int userId) {
        return productDAO.findAvailableProductsByUserId(userId);
    }

    public List<Product> getProductsToShow(int userId) {
        return productDAO.findProductsToShow(userId);
    }

    public boolean updateProductAvailability(List<Product> products, boolean available) {
        return productDAO.updateProductAvailability(products, available);
    }
    public List<Product> getProductById(int idProduct) {
        return productDAO.findProductById(idProduct);
    }
    public boolean createProduct(Product product) {
        return productDAO.create(product);
    }
    public boolean updateProduct(Product product) {
        return productDAO.update(product);
    }
    public boolean removeProduct(Product product) {
        return productDAO.remove(product);
    }
    public Product findProductById(int idProduct) {
        return productDAO.findById(idProduct);
    }
    public List<Product> findAllProducts() {
        return productDAO.findAll();
    }
}
