package co.unicauca.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Guarda contra la reintroducción de un secreto JWT por defecto.
 */
class SecretConfigurationTest {

    @Test
    @DisplayName("El secreto JWT no tiene valor por defecto en auth-service")
    void jwtSecretHasNoDefault() throws Exception {
        Properties properties = new Properties();
        try (InputStream input = Files.newInputStream(Path.of("src/main/resources/application.properties"))) {
            properties.load(input);
        }

        assertEquals("${JWT_SECRET}", properties.getProperty("jwt.secret"));
    }
}
