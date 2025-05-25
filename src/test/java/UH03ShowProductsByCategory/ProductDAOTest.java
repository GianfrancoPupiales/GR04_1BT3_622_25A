package UH03ShowProductsByCategory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.dao.ProductDAO;
import model.entities.Product;
import model.enums.ProductCategory;
import model.enums.ProductState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductDAOTest {

    @InjectMocks
    ProductDAO productDAO;

    @Mock
    EntityManager entityManager;

    @Mock
    TypedQuery<Product> query;

    @BeforeEach
    void setup() {
        productDAO = new ProductDAO(entityManager);
    }

    @Test
    void givenValidCategory_whenGetProductsByCategory_thenReturnFilteredProducts() {
        // Given
        ProductCategory category = ProductCategory.Books;
        List<Product> mockProducts = List.of(
                new Product(1, "Title1", "Description", ProductState.New, category, "", null)
        );

        when(entityManager.createQuery(anyString(), eq(Product.class)))
                .thenReturn(query);
        when(query.setParameter("category", category)).thenReturn(query);
        when(query.getResultList()).thenReturn(mockProducts);

        // When
        List<Product> result = productDAO.getProductsByCategory(category);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(p -> p.getCategory() == category));
    }

    @Test
    void givenNullCategory_whenGetProductsByCategory_thenReturnAllProducts() {
        // Given
        List<Product> allProducts = List.of(
                new Product(1, "Title1", "Description", ProductState.New, ProductCategory.Books, "", null),
                new Product(2, "Title2", "Description", ProductState.New, ProductCategory.Electronics, "", null)
        );

        // Create a spy of the real productDAO instance.
        // A spy allows you to override specific method behavior while keeping the rest of the logic real.
        ProductDAO spyDao = Mockito.spy(productDAO);
        Mockito.doReturn(allProducts).when(spyDao).findAll();

        // When
        List<Product> result = spyDao.getProductsByCategory(null);

        // Then
        assertNotNull(result);
        assertEquals(allProducts.size(), result.size());
        assertTrue(result.containsAll(allProducts));
    }

    @Test
    void givenCategoryWithNoProducts_whenGetProductsByCategory_thenReturnEmptyList() {
        // Given
        ProductCategory category = ProductCategory.Electronics;

        when(entityManager.createQuery(anyString(), eq(Product.class)))
                .thenReturn(query);
        when(query.setParameter("category", category)).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.emptyList());

        // When
        List<Product> result = productDAO.getProductsByCategory(category);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
