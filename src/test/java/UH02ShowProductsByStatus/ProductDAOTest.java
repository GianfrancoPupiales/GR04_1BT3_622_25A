package UH02ShowProductsByStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.dao.ProductDAO;
import model.entities.Product;
import model.enums.ProductCategory;
import model.enums.ProductState;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ProductDAOTest {

    @InjectMocks
    ProductDAO productDAO;

    @Mock
    EntityManager em;

    @Mock
    TypedQuery<Product> query;

    @Test
    void givenValidState_whenGetProductsByState_thenReturnProducts() {
        // Given
        ProductState state = ProductState.New;
        List<Product> expected = List.of(
                new Product(1, "Title1", "Desc", state, ProductCategory.Books, "", null)
        );

        when(em.createQuery(anyString(), eq(Product.class))).thenReturn(query);
        when(query.setParameter("state", state)).thenReturn(query);
        when(query.getResultList()).thenReturn(expected);

        // When
        List<Product> result = productDAO.getProductsByState(state);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(p -> p.getState() == state));
    }

    @Test
    void givenValidStateWithNoProducts_whenGetProductsByState_thenReturnEmptyList() {
        // Given
        ProductState state = ProductState.Repaired;

        when(em.createQuery(anyString(), eq(Product.class))).thenReturn(query);
        when(query.setParameter("state", state)).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.emptyList());

        // When
        List<Product> result = productDAO.getProductsByState(state);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void givenNullState_whenGetProductsByState_thenReturnEmptyList() {
        // Given
        ProductState state = null;

        when(em.createQuery(anyString(), eq(Product.class))).thenReturn(query);
        when(query.setParameter("state", state)).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.emptyList());

        // When
        List<Product> result = productDAO.getProductsByState(state);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
