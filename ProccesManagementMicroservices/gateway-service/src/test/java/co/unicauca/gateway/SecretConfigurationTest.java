package co.unicauca.gateway;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Guarda contra la reintroducción de un secreto JWT por defecto.
 * El valor debe ser siempre la variable de entorno obligatoria.
 */
class SecretConfigurationTest {

    @Test
    @DisplayName("El secreto JWT no tiene valor por defecto en el gateway")
    void jwtSecretHasNoDefault() throws Exception {
        try (InputStream input = Files.newInputStream(Path.of("src/main/resources/application.yml"))) {
            Map<String, Object> config = new Yaml().load(input);
            @SuppressWarnings("unchecked")
            Map<String, Object> jwt = (Map<String, Object>) config.get("jwt");

            assertEquals("${JWT_SECRET}", jwt.get("secret"));
        }
    }
}
