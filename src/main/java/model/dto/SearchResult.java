package model.dto;

import model.entities.Product;

import java.util.List;

public class SearchResult {
    private final List<Product> products;
    private final String message;

    public SearchResult(List<Product> products, String message) {
        this.products = products;
        this.message = message;
    }

    public List<Product> getProducts() {
        return products;
    }

    public String getMessage() {
        return message;
    }
}
