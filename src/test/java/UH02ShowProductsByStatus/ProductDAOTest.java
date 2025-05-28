package UH02ShowProductsByStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.dao.ProductDAO;
import model.entities.Product;
import model.entities.User;
import model.enums.ProductCategory;
import model.enums.ProductState;

import org.junit.jupiter.api.BeforeEach;
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
    EntityManager entityManager;

    @Mock
    EntityManager em;

    @Mock
    TypedQuery<Product> query;

    private final int userId = 42;

    @BeforeEach
    void setup() {
        productDAO = new ProductDAO(entityManager);
    }
    @Test
    void givenValidState_whenGetProductsByState_thenReturnProducts() {
        // Given
        ProductState state = ProductState.New;
        User user = new User();
        user.setIdUser(10);
        List<Product> expected = List.of(
                new Product(1, "Title1", "Desc", state, ProductCategory.Books, "", user)
        );

        when(entityManager.createQuery(anyString(), eq(Product.class)))
                .thenReturn(query);
        when(query.setParameter("state", state)).thenReturn(query);
        when(query.setParameter("userId", userId)).thenReturn(query);
        when(query.getResultList()).thenReturn(expected);

        // When
        List<Product> result = productDAO.getProductsByState(state,userId);

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(result.stream().allMatch(p -> p.getState().equals(state)));
    }

    @Test
    void givenValidStateWithNoProducts_whenGetProductsByState_thenReturnEmptyList() {
        // Given
        ProductState state = ProductState.Repaired;

        when(entityManager.createQuery(anyString(), eq(Product.class)))
                .thenReturn(query);
        when(query.setParameter("state", state)).thenReturn(query);
        when(query.setParameter("userId", userId)).thenReturn(query);
        when(query.getResultList()).thenReturn(Collections.emptyList());

        // When
        List<Product> result = productDAO.getProductsByState(state, userId);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void givenNullState_whenGetProductsByState_thenReturnAllProducts() {
        // Given
        List<Product> allProducts = List.of(
                new Product(1, "Title1", "Description", ProductState.New, ProductCategory.Books, "", null),
                new Product(2, "Title2", "Description", ProductState.New, ProductCategory.Electronics, "", null)
        );

        // Create a spy para el método findAll
        ProductDAO spyDao = Mockito.spy(productDAO);
        Mockito.doReturn(allProducts).when(spyDao).findAll();

        // When
        List<Product> result = spyDao.getProductsByState(null, userId);

        // Then
        assertNotNull(result);
        assertEquals(allProducts.size(), result.size());
        assertTrue(result.containsAll(allProducts));
    }
}
