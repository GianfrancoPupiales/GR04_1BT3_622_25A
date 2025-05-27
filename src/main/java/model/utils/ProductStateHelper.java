package model.utils;

import model.enums.ProductState;

import java.util.Optional;

public class ProductStateHelper {
    public static boolean isAllOrNull(Object input) {
        if (input == null) return true;
        if (input instanceof String s) {
            return s.isEmpty() || s.equalsIgnoreCase("all");
        }
        return false;
    }

    public static Optional<ProductState> parseState(String input) {
        if (input == null || input.trim().isEmpty()) {
            return Optional.empty();
        }

        for (ProductState state : ProductState.values()) {
            if (state.name().equalsIgnoreCase(input.trim())) {
                return Optional.of(state);
            }
        }

        return Optional.empty();
    }
}
