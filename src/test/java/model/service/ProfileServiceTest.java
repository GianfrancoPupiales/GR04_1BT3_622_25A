package model.service;

import model.dao.InMemoryProfileDAO;
import model.dao.ProfileDAO;
import model.entities.Product;
import model.entities.Profile;
import model.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProfileServiceTest {

    private ProfileDAO mockDAO;
    private ProfileService service;

    @BeforeEach
    void setUp() {
        mockDAO = mock(ProfileDAO.class);
        service = new ProfileService(mockDAO);
    }

    // ======= HISTORIA DE USUARIO 08: VISUALIZAR PERFIL PERSONAL ============

    /*
     Prueba unitaria: DADO que se registra un usuario, CUANDO se crea su perfil, ENTONCES debe almacenarse correctamente.
     */
    @Test
    void given_registered_user_when_profile_created_then_profile_should_be_stored() {
        Profile profile = new Profile(1, "Luis", "Gomez", "photo.jpg", "Engineering student");
        assertEquals("Luis", profile.getFirstName());
        assertEquals("Gomez", profile.getLastName());
        assertEquals("photo.jpg", profile.getPhoto());
        assertEquals("Engineering student", profile.getDescription());
    }


    /*
     Prueba unitaria: DADO que se creó un perfil, CUANDO se accede a “Mi Perfil”, ENTONCES debe retornar la información del usuario.
     */
    @Test
    void given_created_profile_when_accessing_my_profile_then_return_profile_info() {
        Profile profile = new Profile(1, "Luis", "Gomez", "photo.jpg", "Engineering student");
        InMemoryProfileDAO dao = new InMemoryProfileDAO();
        dao.save(profile);
        ProfileService service = new ProfileService(dao);

        Profile result = service.getProfileByUserId(1);

        assertNotNull(result);
        assertEquals("Luis", result.getFirstName());
        assertEquals("Gomez", result.getLastName());
    }

    // ========= HISTORIA DE USUARIO 09: EDITAR PERFIL ==================

    /*
    Test con mockito: Verifica que si el perfil es válido, se llama al método de actualización del DAO.
     */
    @Test
    void given_valid_profile_update_when_saveProfile_then_persists_data() {
        Profile profileToUpdate = new Profile(1, "Ana", "Torres", "nueva.jpg", "Nueva descripción");

        when(mockDAO.update(profileToUpdate)).thenReturn(true);

        boolean result = service.updateProfile(profileToUpdate);

        assertTrue(result);
    }

    /*
Test con mockito: Asegura que si el perfil es inválido , no se llama al DAO ('never')
 */
    @Test
    void given_invalid_profile_update_when_saveProfile_then_fails() {
        Profile invalidProfile = new Profile(1, "", "Torres", "foto.jpg", "desc");

        boolean result = service.updateProfile(invalidProfile);

        assertFalse(result);
        verify(mockDAO, never()).update(invalidProfile);
    }

    /*
    Prueba parametrizada: valida distintos casos de perfil incompleto, esperando que la validación falle.
     */
    @ParameterizedTest
    @CsvSource({
            "'', 'Torres', 'foto.jpg'",
            "'Ana', '', 'foto.jpg'",
            "'Ana', 'Torres', ''",
            "' ', 'Apellido', 'foto.jpg'",
            "'Nombre', ' ', 'foto.jpg'"
    })
    void given_incomplete_profile_when_validateProfile_then_return_false(String firstName, String lastName, String photo) {
        Profile profile = new Profile(1, firstName, lastName, photo, "desc");
        assertFalse(service.validateProfile(profile));
    }

    /*
    Prueba parametrizada: valida perfiles con datos correctos, esperandoque la validación pase.
     */
    @ParameterizedTest
    @CsvSource({
            "'Ana', 'Torres', 'foto.jpg'",
            "'Juan', 'Perez', 'img.png'"
    })
    void given_valid_profile_when_validateProfile_then_return_true(String firstName, String lastName, String photo) {
        Profile profile = new Profile(1, firstName, lastName, photo, "desc");
        assertTrue(service.validateProfile(profile));
    }

    /*
Pruebas unitarias con InMemoryProfileDAO: Verificaque al guardar un perfil, se pueda recuperar correctamente.
 */
    @Test
    void given_profile_saved_in_memory_when_getProfile_then_return_profile() {
        InMemoryProfileDAO dao = new InMemoryProfileDAO();
        ProfileService service = new ProfileService(dao);

        Profile profile = new Profile(1, "Carlos", "Lopez", "foto2.jpg", "Estudiante");
        dao.save(profile);

        Profile result = service.getProfileByUserId(1);
        assertNotNull(result);
        assertEquals("Carlos", result.getFirstName());
    }

    /*
    Prueba unitaria: comprueba que al actualizar un perfil, los cambios se reflejan correctamente.
     */
    @Test
    void given_profile_saved_in_memory_when_updateProfile_then_profile_is_updated() {
        InMemoryProfileDAO dao = new InMemoryProfileDAO();
        ProfileService service = new ProfileService(dao);

        Profile profile = new Profile(1, "Carlos", "Lopez", "foto2.jpg", "Estudiante");
        dao.save(profile);

        profile.setFirstName("Carla");
        boolean updated = service.updateProfile(profile);

        assertTrue(updated);
        Profile updatedProfile = service.getProfileByUserId(1);
        assertEquals("Carla", updatedProfile.getFirstName());
    }


    // ============== HISTORIA DE USUARIO 10: CONSULTAR OTRO PERFIL ==============
     /*
     Prueba unitaria: DADO que tengo un producto publicado por otro estudiante,
     CUANDO selecciono su nombre, ENTONCES el sistema debe mostrar el perfil del dueño del producto.
      */

    @Test
    void given_product_when_select_owner_then_display_owner_profile() {
        Product product = new Product(1,"Book", "Electronics", "Used", new User());

        assertEquals(2, product.getOwner().getId());
    }


}
