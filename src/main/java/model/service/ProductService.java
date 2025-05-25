package model.service;

import model.dao.ProductDAO;
import model.dto.SearchResult;
import model.entities.Product;
import model.enums.ProductCategory;
import model.utils.ProductCategoryHelper;

import java.util.List;
import java.util.Optional;

public class ProductService {
    private final ProductDAO productDAO;

    public ProductService() {
        productDAO = new ProductDAO();
    }

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public List<Product> findProductsByUserId(int userId) {
        return productDAO.findProductsByUserId(userId);
    }

    public List<Product> findAvailableProductsByUserId(int userId) {
        return productDAO.findAvailableProductsByUserId(userId);
    }

    public boolean updateProductAvailability(List<Product> products, boolean available) {
        return productDAO.updateProductAvailability(products, available);
    }
    public boolean createProduct(Product product) {
        return productDAO.create(product);
    }
    public boolean updateProduct(Product product) {
        return productDAO.update(product);
    }

    public boolean removeProduct(int idProduct) {
        return productDAO.remove(idProduct);
    }

    public Product findProductById(int idProduct) {
        return productDAO.findById(idProduct);
    }
    public List<Product> findAllProducts() {
        return productDAO.findAll();
    }

    public List<Product> findAvailableProductsExceptUser(int userId) {
        return productDAO.findAvailableProductsExceptUser(userId);
    }

    public Product findById(int idProduct) {
        return productDAO.findById(idProduct);
    }


    public SearchResult searchProductsByCategory(Object inputCategory) {
        List<Product> products;
        String message = null;

        if (ProductCategoryHelper.isAllOrNull(inputCategory)) {
            products = productDAO.findAll();
        } else {
            Optional<ProductCategory> categoryOpt = ProductCategoryHelper.parseCategory(String.valueOf(inputCategory));
            if (categoryOpt.isPresent()) {
                products = productDAO.getProductsByCategory(categoryOpt.get());
                if (products.isEmpty()) {
                    message = "There are no products in this category";
                }
            } else {
                products = productDAO.findAll();
                if (inputCategory != null && !inputCategory.toString().equalsIgnoreCase("all")) {
                    message = "Invalid status, showing all products";
                }
            }
        }
        return new SearchResult(products, message);
    }

}
