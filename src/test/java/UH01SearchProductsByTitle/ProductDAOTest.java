package UH01SearchProductsByTitle;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.dao.ProductDAO;
import model.entities.Product;
import model.enums.ProductCategory;
import model.enums.ProductState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductDAOTest {

    @InjectMocks
    ProductDAO productDAO;

    @Mock
    EntityManager em;

    @Mock
    TypedQuery<Product> query;

    // Definimos un userId ejemplo para las pruebas
    private final int userId = 1;

    @Test
    void givenValidTitle_whenGetProductsByTitle_thenReturnMatchingProducts() {
        // Given
        String searchTitle = "Pen";
        List<Product> expected = List.of(new Product(1, "Pen Black", "Desc", ProductState.New, ProductCategory.Stationery, "", null));

        when(em.createQuery(anyString(), eq(Product.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(expected);

        // When
        List<Product> result = productDAO.getProductsByTitle(searchTitle, userId);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(p -> p.getTitle().toLowerCase().contains(searchTitle.toLowerCase())));
    }

    @Test
    void givenNoMatchingTitle_whenGetProductsByTitle_thenReturnEmptyList() {
        // Given
        String searchTitle = "NonexistentTitle";

        when(em.createQuery(anyString(), eq(Product.class))).thenReturn(query);
        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.emptyList());

        // When
        List<Product> result = productDAO.getProductsByTitle(searchTitle, userId);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
