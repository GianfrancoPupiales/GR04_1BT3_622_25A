package UH03ShowProductsByCategory;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import model.dao.ProductDAO;
import model.dto.SearchResult;
import model.entities.Product;
import model.enums.ProductCategory;
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
                new Product(1, "Product A", "Description", null, ProductCategory.Books, "", null),
                new Product(2, "Product B", "Description", null, ProductCategory.Electronics, "", null)
        );
    }

    // Escenario 04: Selección “All” o null muestra todos los productos sin mensaje
    @Test
    void givenNullOrAllCategory_whenSearchProductsByCategory_thenReturnAllProductsWithoutMessage() {
        when(productDAO.findAll()).thenReturn(sampleProducts);

        SearchResult resultNull = productService.searchProductsByCategory(null);
        SearchResult resultAll = productService.searchProductsByCategory("All");

        assertEquals(sampleProducts, resultNull.getProducts());
        assertNull(resultNull.getMessage());

        assertEquals(sampleProducts, resultAll.getProducts());
        assertNull(resultAll.getMessage());
    }


    // Escenario 05: Categoría inválida, muestra todos los productos con mensaje de alerta
    @Test
    void givenInvalidCategory_whenSearchProductsByCategory_thenReturnAllProductsWithErrorMessage() {
        when(productDAO.findAll()).thenReturn(sampleProducts);

        SearchResult result = productService.searchProductsByCategory("CategoriaInvalida");

        assertEquals(sampleProducts, result.getProducts());
        assertEquals("Invalid status, showing all products", result.getMessage());
    }

    // Escenario 02: Aplicar filtro por categoría válida con productos
    @Test
    void givenValidCategoryWithProducts_whenSearchProductsByCategory_thenReturnFilteredProductsWithoutMessage() {
        ProductCategory category = ProductCategory.Books;
        List<Product> filtered = List.of(
                new Product(3, "Filtered Book Product", "Desc", null, category, "", null)
        );
        when(productDAO.getProductsByCategory(category)).thenReturn(filtered);

        SearchResult result = productService.searchProductsByCategory("Books");

        assertEquals(filtered, result.getProducts());
        assertNull(result.getMessage());
    }

    // Escenario 03: Categoría válida pero sin productos, muestra mensaje informativo
    @Test
    void givenValidCategoryWithNoProducts_whenSearchProductsByCategory_thenReturnEmptyListWithInfoMessage() {
        ProductCategory category = ProductCategory.Electronics;
        when(productDAO.getProductsByCategory(category)).thenReturn(Collections.emptyList());

        SearchResult result = productService.searchProductsByCategory("Electronics");

        assertTrue(result.getProducts().isEmpty());
        assertEquals("There are no products in this category", result.getMessage());
    }
}