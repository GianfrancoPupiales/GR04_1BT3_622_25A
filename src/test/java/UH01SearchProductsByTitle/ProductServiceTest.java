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

    @Test
    void givenValidTitle_whenSearchProductsByTitle_thenReturnsProductsAndNoMessage() {
        String title = "Pen";
        List<Product> products = List.of(
                new Product(1, "Pen Black", "Desc", ProductState.New, ProductCategory.Stationery, "", null)
        );
        when(productDAO.getProductsByTitle(title)).thenReturn(products);

        SearchResult result = productService.searchProductsByTitle(title);

        assertEquals(products, result.getProducts());
        assertNull(result.getMessage());

        verify(productDAO).getProductsByTitle(title);
        verify(productDAO, never()).findAll();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   ", "\t", "\n"})
    void givenEmptyOrWhitespaceTitle_whenSearchProductsByTitle_thenReturnsAllProducts(String title) {
        List<Product> products = List.of(
                new Product(1, "Pen Black", "Desc", ProductState.New, ProductCategory.Stationery, "", null),
                new Product(2, "Notebook", "Desc", ProductState.New, ProductCategory.Stationery, "", null)
        );
        when(productDAO.findAll()).thenReturn(products);

        SearchResult result = productService.searchProductsByTitle(title);

        assertEquals(products, result.getProducts());
        assertNull(result.getMessage());

        verify(productDAO).findAll();
        verify(productDAO, never()).getProductsByTitle(anyString());
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
        when(productDAO.findAll()).thenReturn(products);

        SearchResult result = productService.searchProductsByTitle(title);

        assertEquals(products, result.getProducts());
        assertNotNull(result.getMessage());

        verify(productDAO).findAll();
        verify(productDAO, never()).getProductsByTitle(anyString());
    }

    @Test
    void givenNoResults_whenSearchProductsByTitle_thenReturnsEmptyListAndMessage() {
        String title = "Nonexistent";
        when(productDAO.getProductsByTitle(title)).thenReturn(Collections.emptyList());

        SearchResult result = productService.searchProductsByTitle(title);

        assertTrue(result.getProducts().isEmpty());
        assertNotNull(result.getMessage());

        verify(productDAO).getProductsByTitle(title);
    }
}