package model.service;

import model.dao.FavoriteDAO;
import model.dao.InMemoryFavoriteDAO;
import model.entities.Product;
import model.entities.User;
import model.entities.Favorite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FavoriteServiceTest {
    private FavoriteService favoriteService;
    private FavoriteDAO mockDAO;
    private User testUser;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        mockDAO = mock(FavoriteDAO.class);
        favoriteService = new FavoriteService(mockDAO);
        testUser = new User();
        testProduct = new Product();
        testUser.setIdUser(1);
        testProduct.setIdProduct(1);
        testProduct.setTitle("Sample Product");
    }

    /**
     * Test unitario: Agregar un favorito para un usuario y comprobar que se ha creado correctamente.
     */
    @Test
    public void given_user_when_add_to_favorites_then_is_created() {
        FavoriteService service = new FavoriteService(new InMemoryFavoriteDAO());
        service.addFavorite(testUser, testProduct);
        List<Favorite> favorites = service.getFavoritesByUser(testUser);
        assertEquals(1, favorites.size());
    }

    /*
    Test Unitario: Elminar producto inexistente
    */
    @Test
    public void when_removeFavorite_is_called_with_non_existent_favorite_then_return_false() {
        FavoriteService service = new FavoriteService(new InMemoryFavoriteDAO());
        boolean result = service.removeFavorite(testUser, testProduct);
        assertFalse(result, "Se esperaba que el resultado fuera 'false' al intentar eliminar un favorito inexistente");
    }

    /*
    Test Unitario: Agregar producto duplicado
    */

    @Test
    public void when_same_product_is_added_twice_then_it_should_not_be_duplicated() {
        FavoriteService service = new FavoriteService(new InMemoryFavoriteDAO());

        service.addFavorite(testUser, testProduct);
        service.addFavorite(testUser, testProduct);

        List<Favorite> favorites = service.getFavoritesByUser(testUser);
        assertEquals(1, favorites.size(), "Se esperaba que el producto no se duplicara en favoritos");
    }



    /*
     Test Unitario: Quitar los productos de favoritos
     */
    @Test
    public void testRemoveFavorite_Success() {
        FavoriteService service = new FavoriteService(new InMemoryFavoriteDAO());

        service.addFavorite(testUser, testProduct);
        service.removeFavorite(testUser, testProduct);

        assertEquals(0, service.getFavoritesByUser(testUser).size(),
                "Se esperaba que el favorito fuera eliminado del repositorio");
    }


    /*
    Prueba con condición - Si el usuario no está autenticado, lanzar excepción
     */
    @Test
    void testAddFavoriteAlreadyExistsThrowsException() {
        // Arrange
        FavoriteDAO mockDAO = mock(FavoriteDAO.class);
        //   FavoriteService service = new FavoriteService(mockDAO);

        User user = new User();
        user.setIdUser(1);

        Product product = new Product();
        product.setIdProduct(101);

        // Simular que el producto ya está en la lista de favoritos
        Favorite existingFavorite = new Favorite();
        existingFavorite.setUser(user);
        existingFavorite.setProduct(product);

        when(mockDAO.findAll()).thenReturn(List.of(existingFavorite));

    }

    /*
    Prueba con condición - Lanzar excepción cuando el producto sea nulo al agregar a favoritos
     */


    @Test
    void when_addFavorite_is_called_with_null_product_then_throw_exception() {
        FavoriteService service = new FavoriteService(new InMemoryFavoriteDAO());

        assertThrows(IllegalArgumentException.class, () -> {
            service.addFavorite(testUser, null);
        }, "Se esperaba que se lanzara una excepción si el producto es nulo.");
    }



    /**
     * Test con Timeout: Agregar un favorito no debe tardar más de 500ms
     */
    @Test
    @Timeout(500)
    void testAddFavoritePerformance() {
        assertDoesNotThrow(() -> favoriteService.addFavorite(testUser, testProduct));
    }

    /**
     * Test de Estado Esperado: El producto debe estar en la lista de favoritos tras agregarlo
     */
    @Test
    void testAddFavoriteState() {
        favoriteService.addFavorite(testUser, testProduct);

        List<Favorite> favorites = favoriteService.getFavoritesByUser(testUser);
        boolean found = favorites.stream()
                .anyMatch(fav -> fav.getProduct().getIdProduct() == testProduct.getIdProduct());

        assertTrue(found, "El producto debería estar en la lista de favoritos.");
    }

    /**
     * Test Parametrizado: No se deben permitir títulos con símbolos inválidos.
     */
    @ParameterizedTest
    @ValueSource(strings = {
            "Smartphone@2025",
            "Oferta#1",
            "Precio$Bajo",
            "Descuento%Extra",
            "Combo&Pack",
            "Nueva*Serie",
            "¡Imperdible!"
    })
    void testAddProductWithInvalidSymbols(String invalidTitle) {
        Product invalidProduct = new Product();
        invalidProduct.setIdProduct(102); // ID arbitrario
        invalidProduct.setTitle(invalidTitle);

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> favoriteService.addFavorite(testUser, invalidProduct),
                "IllegalArgumentException expected due to invalid symbols in title."
        );

        assertEquals("The title of the product contains invalid characters.", thrown.getMessage());
    }

}