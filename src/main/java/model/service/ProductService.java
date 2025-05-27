package model.service;

import model.dao.ProductDAO;
import model.dto.SearchResult;
import model.entities.Product;
import model.enums.ProductCategory;
import model.enums.ProductState;
import model.utils.ProductCategoryHelper;
import model.utils.ProductSearchHelper;
import model.utils.ProductStateHelper;

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
            } else if (inputCategory != null && !inputCategory.toString().equalsIgnoreCase("all")
                    && categoryOpt.isEmpty()) {
                return "Invalid category, showing all products";
            }
        }
        return null;
    }

    public SearchResult searchProductsByTitle(String title, int userId) {
        title = title == null ? "" : title.trim();

        if (title.isEmpty() || title.length() > 50) {
            List<Product> all = productDAO.findAvailableProductsExceptUser(userId);
            String message = ProductSearchHelper.getSearchMessage(title, all);
            return new SearchResult(all, message);
        }

        List<Product> products = productDAO.getProductsByTitle(title, userId);
        String message = ProductSearchHelper.getSearchMessage(title, products);
        return new SearchResult(products, message);
    }

    public SearchResult searchProductsByState(Object inputState) {
        List<Product> products;
        String message = null;

        if (ProductCategoryHelper.isAllOrNull(inputState)) {
            products = productDAO.findAll();
        } else {
            Optional<ProductState> stateOpt = ProductStateHelper.parseState(String.valueOf(inputState));
            if (stateOpt.isPresent()) {
                products = productDAO.getProductsByState(stateOpt.get());
                if (products.isEmpty()) {
                    message = "There are no products with this state";
                }
            } else {
                products = productDAO.findAll();
                if (inputState != null && !inputState.toString().equalsIgnoreCase("all")) {
                    message = "Invalid status, showing all products";
                }
            }
        }
        return new SearchResult(products, message);
    }

}
