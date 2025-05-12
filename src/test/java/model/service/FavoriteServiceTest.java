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
import org.junit.jupiter.params.provider.ValueSource;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FavoriteServiceTest {
    private FavoriteService favoriteService;
    private FavoriteDAO mockDAO;
    private User testUser;
    private Product testProduct,testAltProduct;

    @BeforeEach
    void setUp() {
        mockDAO = mock(FavoriteDAO.class);
        favoriteService = new FavoriteService(mockDAO);
        //User data
        testUser = new User();
        testUser.setIdUser(1);
        //Main data product
        testProduct = new Product();
        testProduct.setIdProduct(1);
        testProduct.setTitle("Sample Product");
        //Alter data product
        testAltProduct = new Product();
        testAltProduct.setIdProduct(2);
        testAltProduct.setTitle("Sample Product 2");

        // Configurar el comportamiento del mockDAO:
        when(mockDAO.findByUserAndProduct(testUser, testProduct)).thenReturn(null); // No existe aún

        // Simular que después de agregar, el producto aparece en favoritos
        List<Favorite> favorites = new ArrayList<>();
        favorites.add(new Favorite(testUser, testProduct));
        when(mockDAO.findByUser(testUser)).thenReturn(favorites);
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
    @Timeout(value = 1, unit = TimeUnit.SECONDS)
    void given_valid_user_and_product_when_add_favorite_then_timeout() {
        assertDoesNotThrow(() -> favoriteService.addFavorite(testUser, testProduct));
    }

    /**
     * Test de Estado Esperado: El producto debe estar en la lista de favoritos tras agregarlo
     */
    @Test
    void given_valid_user_and_product_when_add_favorite_then_product_is_in_user_favorites() {
        favoriteService.addFavorite(testUser, testProduct);

        List<Favorite> favorites = favoriteService.getFavoritesByUser(testUser);
        boolean found = favorites.stream()
                .anyMatch(fav -> fav.getProduct().getIdProduct() == testProduct.getIdProduct());

        assertTrue(found);
    }

    /**
     * Test usando Mockito: Validar si el DAO verifica errores al crear
     */
    @Test
    void give_a_exception_when_dao_create_then_service_should_throw_it() {
        when(mockDAO.findByUserAndProduct(testUser, testAltProduct)).thenReturn(null);
        doThrow(new RuntimeException("Save failure")).when(mockDAO).create(any(Favorite.class));

        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> favoriteService.addFavorite(testUser, testAltProduct)
        );

        assertEquals("Save failure", thrown.getMessage());
    }

    /**
     * Test usando Mockito: Este test pretende validar si create() se llama solo cuando el producto no esté en Favoritos
     */

    @Test
    void give_a_product_when_it_is_already_in_favorites_then_create_should_not_called() {
        Favorite existingFavorite = new Favorite(testUser, testProduct);
        when(mockDAO.findByUserAndProduct(testUser, testProduct)).thenReturn(existingFavorite);

        favoriteService.addFavorite(testUser, testProduct);

        verify(mockDAO, never()).create(any());
    }


    /**
     * Test Parametrizado: No se deben permitir títulos con símbolos inválidos.
     */
    @ParameterizedTest
    @ValueSource(strings = {
            "Iphone@2025",
            "Offer#1",
            "$uper",
            "100%",
            "New*Serie",
            "¡!"
    })
    void given_invalid_title_when_add_favorite_then_exception_thrown(String invalidTitle) {
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

    /**
     *Test parametrizado: El límite de productos favoritos (en este caso 20) no se debe poder sobrepasar y el sistema debe mostrar un mensaje de excepción
     */
    @ParameterizedTest
    @ValueSource(ints = {19, 20, 21})
    void given_user_with_n_favorites_when_addFavorite_then_boundary_behavior(int existingFavoritesCount) {
        // Preparar lista simulada de favoritos actuales
        List<Favorite> existingFavorites = new ArrayList<>();
        for (int i = 0; i <= existingFavoritesCount; i++) {
            Product p = new Product();
            p.setIdProduct(i + 1);
            p.setTitle("Product " + (i + 1));
            existingFavorites.add(new Favorite(testUser, p));
        }

        // Configurar el mock para devolver la lista simulada
        when(mockDAO.findByUser(testUser)).thenReturn(existingFavorites);
        when(mockDAO.findByUserAndProduct(eq(testUser), any())).thenReturn(null);

        if (existingFavoritesCount >= 20) {
            assertDoesNotThrow(() -> {
                favoriteService.addFavorite(testUser, testAltProduct);
            }, "Se esperaba que se permitiera agregar hasta 20 favoritos.");
        } else {
            IllegalStateException thrown = assertThrows(
                    IllegalStateException.class,
                    () -> favoriteService.addFavorite(testUser, testAltProduct),
                    "Se esperaba que se lanzara una excepción al superar el límite de favoritos."
            );
            assertEquals("Cannot have more than 20 favorites", thrown.getMessage());
        }
    }


}