package UH02ShowProductsByStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import model.dao.ProductDAO;
import model.dto.SearchResult;
import model.entities.Product;
import model.enums.ProductCategory;
import model.enums.ProductState;
import model.service.ProductService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductDAO productDAO;

    @InjectMocks
    ProductService productService;
    private final int userId = 42;

    private List<Product> sampleProducts;

    @BeforeEach
    void setup() {
        sampleProducts = List.of(
                new Product(1, "Product A", "Description", ProductState.New, ProductCategory.Books, "", null),
                new Product(2, "Product B", "Description", ProductState.Repaired, ProductCategory.Electronics, "", null)
        );
    }

    @Test
    void givenNullOrTodosState_whenSearchProductsByState_thenReturnAllProductsWithoutMessage() {
        when(productDAO.findAvailableProductsExceptUser(userId)).thenReturn(sampleProducts);

        SearchResult resultNull = productService.searchProductsByState(null, userId);
        SearchResult resultTodos = productService.searchProductsByState("All", userId);

        assertEquals(sampleProducts, resultNull.getProducts());
        assertNull(resultNull.getMessage());

        assertEquals(sampleProducts, resultTodos.getProducts());
        assertNull(resultTodos.getMessage());
    }

    @Test
    void givenInvalidState_whenSearchProductsByState_thenReturnAllProductsWithErrorMessage() {
        when(productDAO.findAvailableProductsExceptUser(userId)).thenReturn(sampleProducts);

        SearchResult result = productService.searchProductsByState("InvalidState", userId);

        assertEquals(sampleProducts, result.getProducts());
        assertEquals("Invalid status, showing all products", result.getMessage());
    }

    @Test
    void givenValidNewState_whenSearchProductsByState_thenReturnFilteredProductsWithoutMessage() {
        ProductState state = ProductState.New;
        List<Product> filtered = List.of(new Product(3, "Filtered New Product", "Desc", state, ProductCategory.Books, "", null));
        when(productDAO.getProductsByState(state, userId)).thenReturn(filtered);

        SearchResult result = productService.searchProductsByState("New",userId);

        assertEquals(filtered, result.getProducts());
        assertNull(result.getMessage());
    }

    @Test
    void givenValidRepairedStateWithNoProducts_whenSearchProductsByState_thenReturnEmptyListWithInfoMessage() {
        ProductState state = ProductState.Repaired;

        when(productDAO.getProductsByState(state, userId)).thenReturn(Collections.emptyList());

        SearchResult result = productService.searchProductsByState("Repaired", userId);

        assertTrue(result.getProducts().isEmpty());
        assertEquals("There are no products with this state", result.getMessage());
    }

    @Test
    void givenValidSemiNewStateWithDash_whenSearchProductsByState_thenReturnFilteredProductsWithoutMessage() {
        ProductState state = ProductState.Semi_new;
        List<Product> filtered = List.of(new Product(4, "Filtered SemiNew Product", "Desc", state, ProductCategory.Books, "", null));
        when(productDAO.getProductsByState(state, userId)).thenReturn(filtered);

        // input string with dash should be normalized to enum constant Semi_new
        SearchResult result = productService.searchProductsByState("Semi_new", userId);

        assertEquals(filtered, result.getProducts());
        assertNull(result.getMessage());
    }

    @Test
    void givenSemiNewStateWithSpace_whenSearchProductsByState_thenReturnAEmptyListWithAErrorMessage() {
        ProductState state = ProductState.Semi_new;
        List<Product> filtered = List.of(new Product(5, "Filtered SemiNew Product", "Desc", state, ProductCategory.Books, "", null));
        // input string with space should be normalized to enum constant Semi_new
        SearchResult result = productService.searchProductsByState("Semi new", userId);

        assertTrue(result.getProducts().isEmpty());
        assertEquals("Invalid status, showing all products", result.getMessage());
    }
}
