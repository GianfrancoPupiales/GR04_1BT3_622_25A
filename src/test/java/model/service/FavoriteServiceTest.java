package model.service;

import model.entities.Product;
import model.entities.User;
import model.entities.Favorite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
}