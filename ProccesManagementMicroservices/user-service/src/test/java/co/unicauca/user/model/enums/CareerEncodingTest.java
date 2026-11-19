package co.unicauca.user.model.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Regresión de codificación: los literales con acentos deben conservarse en
 * UTF-8 y no deben contener caracteres de reemplazo.
 */
class CareerEncodingTest {

    @Test
    @DisplayName("Los nombres de programa conservan sus acentos")
    void careerDisplayNamesContainCorrectAccents() {
        assertEquals("Ingeniería de Sistemas", Career.SYSTEMS_ENGINEERING.getDisplayName());
        assertEquals("Ingeniería Electrónica y Telecomunicaciones",
                Career.ELECTRONICS_TELECOMMUNICATIONS.getDisplayName());
        assertEquals("Automatización Industrial", Career.INDUSTRIAL_AUTOMATION.getDisplayName());
        assertEquals("Tecnología en Telemática", Career.TELEMATICS_TECHNOLOGY.getDisplayName());
    }

    @Test
    @DisplayName("fromDisplayName acepta el valor acentuado")
    void fromDisplayNameAcceptsAccentedValues() {
        assertEquals(Career.SYSTEMS_ENGINEERING,
                Career.fromDisplayName("Ingeniería de Sistemas"));
        assertEquals(Career.ELECTRONICS_TELECOMMUNICATIONS,
                Career.fromDisplayName("Ingeniería Electrónica y Telecomunicaciones"));
    }

    @Test
    @DisplayName("Los nombres de rol no dependen de la plataforma")
    void roleDisplayNamesAreStable() {
        assertEquals("estudiante", Role.STUDENT.getDisplayName());
        assertEquals("profesor", Role.TEACHER.getDisplayName());
        assertEquals("coordinador", Role.COORDINATOR.getDisplayName());
        assertEquals("administrador", Role.ADMINISTRATOR.getDisplayName());
    }

    @Test
    @DisplayName("Un texto acentuado sobrevive el round-trip UTF-8")
    void accentedTextSurvivesUtf8RoundTrip() {
        String original = "Contraseña, Gestión, Sesión, Evaluación, tamaño máximo";

        String decoded = new String(original.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);

        assertEquals(original, decoded);
        assertFalse(decoded.contains("\uFFFD"), "El texto contiene caracteres de reemplazo");
    }
}
