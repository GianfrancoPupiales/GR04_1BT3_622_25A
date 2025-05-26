package UH01SearchProductsByTitle;

import model.entities.Product;
import model.enums.ProductCategory;
import model.enums.ProductState;
import model.service.ProductService;
import model.dao.ProductDAO;
import model.dto.SearchResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductDAO productDAO;

    @InjectMocks
    ProductService productService;

    // Define userId para usar en las pruebas
    private final int userId = 1;

    @Test
    void givenValidTitle_whenSearchProductsByTitle_thenReturnsProductsAndNoMessage() {
        String title = "Pen";
        List<Product> products = List.of(
                new Product(1, "Pen Black", "Desc", ProductState.New, ProductCategory.Stationery, "", null)
        );
        when(productDAO.getProductsByTitle(title, userId)).thenReturn(products);

        SearchResult result = productService.searchProductsByTitle(title, userId);

        assertEquals(products, result.getProducts());
        assertNull(result.getMessage());

        verify(productDAO).getProductsByTitle(title, userId);
        verify(productDAO, never()).findAll();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   ", "\t", "\n"})
    void givenEmptyOrWhitespaceTitle_whenSearchProductsByTitle_thenReturnsAllProducts(String title) {
        List<Product> products = List.of(
                new Product(1, "Pen Black", "Desc", ProductState.New, ProductCategory.Stationery, "", null),
                new Product(2, "Notebook", "Desc", ProductState.New, ProductCategory.Stationery, "", null)
        );

        int userId = 1;

        when(productDAO.findAvailableProductsExceptUser(anyInt())).thenReturn(products);

        SearchResult result = productService.searchProductsByTitle(title, userId);

        assertEquals(products, result.getProducts());

        assertTrue(result.getMessage() == null || !result.getMessage().isEmpty());

        verify(productDAO).findAvailableProductsExceptUser(userId);
        verify(productDAO, never()).getProductsByTitle(anyString(), anyInt());
    }




    @ParameterizedTest
    @ValueSource(strings = {
            "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",
            "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB",
            "CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC"
    })
    void givenTooLongTitle_whenSearchProductsByTitle_thenReturnsAllProductsAndMessage(String title) {
        List<Product> products = List.of(
                new Product(1, "Notebook", "Desc", ProductState.New, ProductCategory.Stationery, "", null)
        );

        when(productDAO.findAvailableProductsExceptUser(userId)).thenReturn(products);

        SearchResult result = productService.searchProductsByTitle(title, userId);

        assertEquals(products, result.getProducts());
        assertNotNull(result.getMessage());

        verify(productDAO).findAvailableProductsExceptUser(userId);
        verify(productDAO, never()).getProductsByTitle(anyString(), anyInt());
    }


    @Test
    void givenNoResults_whenSearchProductsByTitle_thenReturnsEmptyListAndMessage() {
        String title = "Nonexistent";
        when(productDAO.getProductsByTitle(title, userId)).thenReturn(Collections.emptyList());

        SearchResult result = productService.searchProductsByTitle(title, userId);

        assertTrue(result.getProducts().isEmpty());
        assertNotNull(result.getMessage());

        verify(productDAO).getProductsByTitle(title, userId);
    }
}
