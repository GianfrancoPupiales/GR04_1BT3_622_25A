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
        if (isBlank(input)) {
            return Optional.empty();
        }

        return findMatchingState(input.trim());
    }

    private static boolean isBlank(String input) {
        return input == null || input.trim().isEmpty();
    }

    private static Optional<ProductState> findMatchingState(String cleanedInput) {
        for (ProductState state : ProductState.values()) {
            if (state.name().equalsIgnoreCase(cleanedInput)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }

}
