package model.utils;

import model.enums.ProductCategory;

import java.util.Optional;

public class ProductCategoryHelper {
    public static Optional<ProductCategory> parseCategory(String input) {
        if (input == null || input.trim().isEmpty()) {
            return Optional.empty();
        }

        for (ProductCategory category : ProductCategory.values()) {
            if (category.name().equalsIgnoreCase(input.trim())) {
                return Optional.of(category);
            }
        }

        return Optional.empty();
    }


    public static boolean isAllOrNull(Object input) {
        if (input == null) return true;
        if (input instanceof String s) {
            return s.isEmpty() || s.equalsIgnoreCase("all");
        }
        return false;
    }

}
