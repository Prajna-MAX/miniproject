import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.zeta.dao.UserDao;
import org.zeta.model.Role;
import org.zeta.model.User;
import org.zeta.service.implementation.AuthenticationService;
import org.zeta.validation.ValidationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private AuthenticationService authenticationService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("john", "1234", Role.CLIENT);
    }

    @Test
    void register_shouldReturnTrue_whenValidData() {
        when(userDao.findByUsername("john")).thenReturn(Optional.empty());
        boolean result = authenticationService.register(
                "john", "1234567", "1234567", Role.CLIENT
        );
        assertTrue(result);
    }
    @Test
    void register_shouldReturnFalse_whenUserAlreadyExists() {
        when(userDao.findByUsername("john")).thenReturn(Optional.of(user));

        boolean result = authenticationService.register(
                "john", "1234", "1234", Role.CLIENT
        );

        assertFalse(result);
    }

    @Test
    void register_shouldReturnFalse_whenValidationFails() {
        // Empty username should fail validation
        boolean result = authenticationService.register(
                "", "1234", "1234", Role.CLIENT
        );

        assertFalse(result);
        verify(userDao, never()).save(any(User.class));
    }


    @Test
    void login_shouldReturnUser_whenCredentialsCorrect() {
        when(userDao.findByUsername("john")).thenReturn(Optional.of(user));

        User loggedIn = authenticationService.login("john", "1234");

        assertNotNull(loggedIn);
        assertEquals("john", loggedIn.getUsername());
    }

    @Test
    void login_shouldThrowException_whenUserNotFound() {
        when(userDao.findByUsername("john")).thenReturn(Optional.empty());

        assertThrows(ValidationException.class, () ->
                authenticationService.login("john", "1234")
        );
    }

    @Test
    void login_shouldThrowException_whenPasswordIncorrect() {
        when(userDao.findByUsername("john")).thenReturn(Optional.of(user));

        assertThrows(ValidationException.class, () ->
                authenticationService.login("john", "wrongpass")
        );
    }
}

