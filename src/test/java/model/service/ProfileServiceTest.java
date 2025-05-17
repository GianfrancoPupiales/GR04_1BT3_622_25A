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




}
