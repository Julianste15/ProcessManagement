package co.unicauca.user.service;

import co.unicauca.user.exceptions.UserException;
import co.unicauca.user.model.User;
import co.unicauca.user.model.enums.Career;
import co.unicauca.user.model.enums.Role;
import co.unicauca.user.repository.UserRepository;
import co.unicauca.user.validation.iValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private iValidator userValidator;

    @Mock
    private PasswordEncryptor passwordEncryptor;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUser_validUser_encryptsPasswordAndSaves() throws Exception {
        User user = new User();
        user.setNames("Juan");
        user.setSurnames("Pérez");
        user.setEmail("juan@unicauca.edu.co");
        user.setPassword("123456");
        user.setTelephone(3001234567L);
        user.setCareer(Career.SYSTEMS_ENGINEERING);
        user.setRole(Role.STUDENT);

        when(userRepository.findByEmail("juan@unicauca.edu.co")).thenReturn(null);
        when(passwordEncryptor.encrypt("123456")).thenReturn("encrypted");
        User saved = new User();
        saved.setId(1L);
        saved.setEmail("juan@unicauca.edu.co");
        when(userRepository.save(any(User.class))).thenReturn(saved);

        User result = userService.registerUser(user);

        verify(userValidator).validate(user);
        verify(passwordEncryptor).encrypt("123456");
        verify(userRepository).save(any(User.class));
        assertEquals(1L, result.getId());
    }

    @Test
    void registerUser_nullUser_throwsUserException() {
        UserException ex = assertThrows(UserException.class,
                () -> userService.registerUser(null));
        assertNotNull(ex.getMessage());
        assertTrue(ex.getMessage().contains("nombres") || ex.getMessage().contains("NAMES"));
    }

    @Test
    void validatePassword_nullValues_returnsFalse() {
        assertFalse(userService.validatePassword(null, "enc"));
        assertFalse(userService.validatePassword("raw", null));
    }

    @Test
    void getUserById_invalidId_throwsUserException() {
        assertThrows(UserException.class, () -> userService.getUserById(0L));
        assertThrows(UserException.class, () -> userService.getUserById(-1L));
    }

    @Test
    void getUserById_validId_returnsFromRepository() throws Exception {
        User user = new User();
        user.setId(10L);
        when(userRepository.findById(10L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(10L);

        assertTrue(result.isPresent());
        assertEquals(10L, result.get().getId());
    }
}
