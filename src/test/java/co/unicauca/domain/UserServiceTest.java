/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.unicauca.domain;

import co.unicauca.domain.services.*;
import co.unicauca.domain.entities.User;
import co.unicauca.domain.repositories.IUserRepositoriy;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    // Repositorio en memoria para pruebas unitarias
    static class InMemoryUserRepository implements IUserRepositoriy {
        private final Map<String, User> data = new HashMap<>();

        @Override
        public void save(User user) {
            data.put(user.getEmail(), user);
        }

        @Override
        public Optional<User> findByEmail(String email) {
            return Optional.ofNullable(data.get(email));
        }

        @Override
        public boolean existsByEmail(String email) {
            return data.containsKey(email);
        }

        @Override
        public List<User> findAll() {
            return new ArrayList<>(data.values());
        }
    }

    // ---------- Tests de registerUser ----------

    @Test
    void registerUser_guardarUsuarioValido() {
        IUserRepositoriy repo = new InMemoryUserRepository();
        PasswordValidator passwordValidator = new PasswordValidator();
        PasswordEncryptor passwordEncryptor = new PasswordEncryptor();
        EmailValidator emailValidator = new EmailValidator();
        UserService service = new UserService(repo, passwordValidator, passwordEncryptor, emailValidator);

        String rawPwd = "Password1!";
        User user = new User(
                "Juan", "Pérez", "3001234567",
                "Ingeniería de Sistemas", "estudiante",
                "juan@unicauca.edu.co", rawPwd
        );

        service.registerUser(user);

        Optional<User> saved = repo.findByEmail("juan@unicauca.edu.co");
        assertTrue(saved.isPresent(), "El usuario debería haberse guardado");
        // La contraseña debe estar encriptada (distinta a la original)
        assertNotEquals(rawPwd, saved.get().getPassword());
        // …y debe validar con matches
        assertTrue(passwordEncryptor.matches(rawPwd, saved.get().getPassword()));
    }

    @Test
    void registerUser_lanzaSiUsuarioNull() {
        IUserRepositoriy repo = new InMemoryUserRepository();
        UserService service = new UserService(repo, new PasswordValidator(), new PasswordEncryptor(), new EmailValidator());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.registerUser(null));
        assertEquals("User cannot be null", ex.getMessage());
    }

    @Test
    void registerUser_lanzaSiEmailNoInstitucional() {
        IUserRepositoriy repo = new InMemoryUserRepository();
        UserService service = new UserService(repo, new PasswordValidator(), new PasswordEncryptor(), new EmailValidator());

        User user = new User(
                "Ana", "Gómez", "3000000000",
                "Ingeniería de Sistemas", "estudiante",
                "ana@gmail.com", "Password1!"
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.registerUser(user));
        assertEquals("Email must be @unicauca.edu.co", ex.getMessage());
    }

    @Test
    void registerUser_lanzaSiPasswordDebil() {
        IUserRepositoriy repo = new InMemoryUserRepository();
        UserService service = new UserService(repo, new PasswordValidator(), new PasswordEncryptor(), new EmailValidator());

        // Falta dígito y símbolo
        User user = new User(
                "Luis", "Rodríguez", "3111111111",
                "Ingeniería de Sistemas", "estudiante",
                "luis@unicauca.edu.co", "Password"
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.registerUser(user));
        assertEquals("Weak password", ex.getMessage());
    }

    @Test
    void registerUser_lanzaSiEmailDuplicado() {
        IUserRepositoriy repo = new InMemoryUserRepository();
        UserService service = new UserService(repo, new PasswordValidator(), new PasswordEncryptor(), new EmailValidator());

        User u1 = new User(
                "Marta", "Rojas", "3001111111",
                "Ingeniería de Sistemas", "estudiante",
                "marta@unicauca.edu.co", "Password1!"
        );
        service.registerUser(u1);

        User u2 = new User(
                "Pedro", "López", "3002222222",
                "Ingeniería de Sistemas", "estudiante",
                "marta@unicauca.edu.co", "Password1!"
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.registerUser(u2));
        assertEquals("Email already registered", ex.getMessage());
    }

    // ---------- Tests de login ----------

    @Test
    void login_okConCredencialesValidas() {
        IUserRepositoriy repo = new InMemoryUserRepository();
        PasswordValidator passwordValidator = new PasswordValidator();
        PasswordEncryptor passwordEncryptor = new PasswordEncryptor();
        EmailValidator emailValidator = new EmailValidator();
        UserService service = new UserService(repo, passwordValidator, passwordEncryptor, emailValidator);

        User user = new User(
                "Carlos", "Méndez", "3003333333",
                "Ingeniería de Sistemas", "profesor",
                "carlos@unicauca.edu.co", "Password1!"
        );
        service.registerUser(user);

        var logged = service.login("carlos@unicauca.edu.co", "Password1!");
        assertTrue(logged.isPresent());
        assertEquals("Carlos", logged.get().getFirstName());
        assertEquals("profesor", logged.get().getRole());
    }

    @Test
    void login_vacioSiPasswordIncorrecta() {
        IUserRepositoriy repo = new InMemoryUserRepository();
        UserService service = new UserService(repo, new PasswordValidator(), new PasswordEncryptor(), new EmailValidator());

        User user = new User(
                "Laura", "Vargas", "3004444444",
                "Ingeniería de Sistemas", "estudiante",
                "laura@unicauca.edu.co", "Password1!"
        );
        service.registerUser(user);

        var logged = service.login("laura@unicauca.edu.co", "wrongPass1!");
        assertTrue(logged.isEmpty());
    }

    @Test
    void login_vacioSiEmailNoExiste() {
        IUserRepositoriy repo = new InMemoryUserRepository();
        UserService service = new UserService(repo, new PasswordValidator(), new PasswordEncryptor(), new EmailValidator());

        var logged = service.login("noexiste@unicauca.edu.co", "Password1!");
        assertTrue(logged.isEmpty());
    }
}
