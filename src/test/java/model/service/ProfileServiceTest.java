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




}
