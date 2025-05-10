package model.service;

import model.entities.Product;
import model.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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
}