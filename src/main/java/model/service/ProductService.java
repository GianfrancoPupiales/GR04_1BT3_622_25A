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


    public SearchResult searchProductsByCategory(Object inputCategory, int userId) {
        List<Product> products = findProductsByCategory(inputCategory, userId);
        String message = buildMessage(inputCategory, products);
        return new SearchResult(products, message);
    }

    private List<Product> findProductsByCategory(Object inputCategory, int userId) {
        if (ProductCategoryHelper.isAllOrNull(inputCategory)) {
            return productDAO.findAvailableProductsExceptUser(userId);
        }

        Optional<ProductCategory> categoryOpt = ProductCategoryHelper.parseCategory(String.valueOf(inputCategory));
        if (categoryOpt.isPresent()) {
            return productDAO.getProductsByCategory(categoryOpt.get(), userId);
        }

        return productDAO.findAvailableProductsExceptUser(userId);
    }

    private String buildMessage(Object inputCategory, List<Product> products) {
        if (!ProductCategoryHelper.isAllOrNull(inputCategory)) {
            Optional<ProductCategory> categoryOpt = ProductCategoryHelper.parseCategory(String.valueOf(inputCategory));
            if (categoryOpt.isPresent() && products.isEmpty()) {
                return "There are no products in this category";
            } else if (inputCategory != null && !inputCategory.toString().equalsIgnoreCase("all") && categoryOpt.isEmpty()) {
                return "Invalid category, showing all products";
            }
        }
        return null;
    }

}
