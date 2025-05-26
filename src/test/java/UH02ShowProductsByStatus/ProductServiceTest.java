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
        when(productDAO.findAll()).thenReturn(sampleProducts);

        SearchResult resultNull = productService.searchProductsByState(null);
        SearchResult resultTodos = productService.searchProductsByState("Todos");

        assertEquals(sampleProducts, resultNull.getProducts());
        assertNull(resultNull.getMessage());

        assertEquals(sampleProducts, resultTodos.getProducts());
        assertNull(resultTodos.getMessage());
    }

    @Test
    void givenInvalidState_whenSearchProductsByState_thenReturnAllProductsWithErrorMessage() {
        when(productDAO.findAll()).thenReturn(sampleProducts);

        SearchResult result = productService.searchProductsByState("InvalidState");

        assertEquals(sampleProducts, result.getProducts());
        assertEquals("Estado inválido, mostrando todos los productos", result.getMessage());
    }

    @Test
    void givenValidNewState_whenSearchProductsByState_thenReturnFilteredProductsWithoutMessage() {
        ProductState state = ProductState.New;
        List<Product> filtered = List.of(new Product(3, "Filtered New Product", "Desc", state, ProductCategory.Books, "", null));
        when(productDAO.getProductsByState(state)).thenReturn(filtered);

        SearchResult result = productService.searchProductsByState("New");

        assertEquals(filtered, result.getProducts());
        assertNull(result.getMessage());
    }

    @Test
    void givenValidRepairedStateWithNoProducts_whenSearchProductsByState_thenReturnEmptyListWithInfoMessage() {
        ProductState state = ProductState.Repaired;
        when(productDAO.getProductsByState(state)).thenReturn(Collections.emptyList());

        SearchResult result = productService.searchProductsByState("Repaired");

        assertTrue(result.getProducts().isEmpty());
        assertEquals("No hay productos con este estado", result.getMessage());
    }

    @Test
    void givenValidSemiNewStateWithDash_whenSearchProductsByState_thenReturnFilteredProductsWithoutMessage() {
        ProductState state = ProductState.Semi_new;
        List<Product> filtered = List.of(new Product(4, "Filtered SemiNew Product", "Desc", state, ProductCategory.Books, "", null));
        when(productDAO.getProductsByState(state)).thenReturn(filtered);

        // input string with dash should be normalized to enum constant Semi_new
        SearchResult result = productService.searchProductsByState("Semi-new");

        assertEquals(filtered, result.getProducts());
        assertNull(result.getMessage());
    }

    @Test
    void givenValidSemiNewStateWithSpace_whenSearchProductsByState_thenReturnFilteredProductsWithoutMessage() {
        ProductState state = ProductState.Semi_new;
        List<Product> filtered = List.of(new Product(5, "Filtered SemiNew Product", "Desc", state, ProductCategory.Books, "", null));
        when(productDAO.getProductsByState(state)).thenReturn(filtered);

        // input string with space should be normalized to enum constant Semi_new
        SearchResult result = productService.searchProductsByState("Semi new");

        assertEquals(filtered, result.getProducts());
        assertNull(result.getMessage());
    }
}
