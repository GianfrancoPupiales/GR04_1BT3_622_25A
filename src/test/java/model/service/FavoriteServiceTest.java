package model.service;

import model.dao.FavoriteDAO;
import model.entities.Product;
import model.entities.User;
import model.entities.Favorite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.mockito.Mockito.when;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class FavoriteServiceTest {
    private FavoriteService favoriteService;
    private FavoriteDAO mockDAO;
    private User testUser;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        mockDAO = mock(FavoriteDAO.class);
      //  favoriteService = new FavoriteService(mockDAO);

        testUser = new User();
        testUser.setIdUser(1);
        testProduct = new Product();
        testProduct.setIdProduct(100);
        testProduct.setTitle("Sample Product");

    }

    /**
     * Test unitario: Agregar un favorito para un usuario y comprobar que se ha creado correctamente.
     */
    @Test
    public void given_user_when_add_to_favorites_then_is_created(){
        favoriteService.addFavorite(testUser, testProduct);
        List<Favorite> favorites = favoriteService.getFavoritesByUser(testUser);
        assertEquals(1, favorites.size());
    }



    /*
    Test Parametrizada: Quitar los productos de favoritos
     */
    @ParameterizedTest
    @CsvSource({
            "1, 101, true",
            "2, 202, false",
            "3, 303, true"
    })
    void testRemoveFavoriteParameterized(int userId, int productId, boolean expected) {
        // Preparar entidades
        User user = new User();
        user.setIdUser(userId);

        Product product = new Product();
        product.setIdProduct(productId);

        // Definir comportamiento del mock
        when(mockDAO.deleteByUserAndProduct(user, product)).thenReturn(true);

        // Ejecutar
        boolean result = favoriteService.removeFavorite(user, product);

        System.out.println("Probando eliminar favorito - Usuario ID: " + userId +
                ", Producto ID: " + productId + ", Resultado esperado: " + expected +
                ", Resultado real: " + result);

        assertEquals(expected, result);
    }

    /*
    Prueba unitaria de excepciones - Agregar producto que ya está en favoritos
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

        // Act & Assert
        /*
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> service.addFavorite(user, product),
                "Se esperaba IllegalStateException porque el producto ya está en favoritos"
        );

        assertEquals("El producto ya está en favoritos.", exception.getMessage());

         */
    }


    /*
    Prueba con condición - Si el usuario no está autenticado, lanzar excepción
     */

    @Test
    void testAddFavoriteUnauthenticatedUser() {
        Product product = new Product();
        product.setIdProduct(999);
        product.setTitle("Producto válido");

        User unauthenticatedUser = null;

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> favoriteService.addFavorite(unauthenticatedUser, product),
                "Debería lanzar excepción si el usuario no está autenticado"
        );

        assertEquals("El usuario no está autenticado.", exception.getMessage());
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