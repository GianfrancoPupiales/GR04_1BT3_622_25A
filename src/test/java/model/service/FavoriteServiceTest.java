package model.service;

import model.entities.Product;
import model.entities.User;
import model.entities.Favorite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FavoriteServiceTest {
    private FavoriteService favoriteService;
    private User testUser;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService();
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