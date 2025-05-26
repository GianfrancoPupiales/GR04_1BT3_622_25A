package model.utils;

import model.entities.Product;

import java.util.List;

public class ProductSearchHelper {

    public static String getSearchMessage(String title, List<Product> results) {
        if (title == null || title.trim().isEmpty()) {
            return null;
        }

        if (title.length() > 50) {
            return "The search text must not exceed 50 characters.";
        }

        if (results.isEmpty()) {
            return "No products were found with this title";
        }

        return null;
    }
}
