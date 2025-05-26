package UH01SearchProductsByTitle;

import model.entities.Product;
import model.enums.ProductCategory;
import model.enums.ProductState;
import model.utils.ProductSearchHelper;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductSearchHelperTest {

    @Test
    void givenEmptyResultsAndValidTitle_thenReturnsNotFoundMessage() {
        List<Product> results = Collections.emptyList();
        String title = "Nonexistent";
        String message = ProductSearchHelper.getSearchMessage(title, results);
        assertEquals("No products were found with this title", message);
    }

    @Test
    void givenTitleLongerThan50Characters_thenReturnsLengthErrorMessage() {
        List<Product> results = List.of();
        String title = "a".repeat(51);
        String message = ProductSearchHelper.getSearchMessage(title, results);
        assertEquals("The search text must not exceed 50 characters.", message);
    }

    @Test
    void givenEmptyOrWhitespaceTitle_thenReturnsNullMessage() {
        String title = "   ";
        List<Product> results = List.of();
        String message = ProductSearchHelper.getSearchMessage(title, results);
        assertNull(message);
    }

    @Test
    void givenValidTitleWithResults_thenReturnsNullMessage() {
        String title = "Pen";
        List<Product> results = List.of(
                new Product(1, "Pen Black", "Desc", ProductState.New, ProductCategory.Stationery, "", null)
        );
        String message = ProductSearchHelper.getSearchMessage(title, results);
        assertNull(message);
    }
}
