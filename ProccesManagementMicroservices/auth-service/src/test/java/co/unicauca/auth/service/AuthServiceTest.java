package co.unicauca.auth.service;
import co.unicauca.auth.dto.AuthResponse;
import co.unicauca.auth.dto.LoginRequest;
import co.unicauca.auth.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void authenticate_teacherWithAcceptedFormatA_shouldNotRequireFormatA() {
        LoginRequest request = new LoginRequest();
        request.setEmail("teacher@unicauca.edu.co");
        request.setPassword("secret");

        Map<String, Object> userBody = Map.of(
                "email", "teacher@unicauca.edu.co",
                "role", "TEACHER",
                "names", "Juan",
                "surnames", "Pérez");
        ResponseEntity<Map> userResponse = ResponseEntity.ok(userBody);

        when(restTemplate.postForEntity(
                eq("http://user-service/api/users/validate-credentials"),
                any(Map.class),
                eq(Map.class))).thenReturn(userResponse);

        

        AuthResponse response = authService.authenticate(request);

        assertEquals("jwt-token-123", response.getToken());
        assertEquals("teacher@unicauca.edu.co", response.getEmail());
        assertEquals("TEACHER", response.getRole());
        assertEquals("Juan Pérez", response.getNames());
        assertFalse(response.isRequiresFormatA());
        assertEquals(5L, response.getFormatoAId());
        assertEquals("FORMATO_A_ACEPTADO", response.getFormatoAEstado());
    }

    @Test
    void validateToken_shouldDelegateToJwtService() {
        when(jwtService.validateToken("abc.def.ghi")).thenReturn(true);
        boolean result = authService.validateToken("abc.def.ghi");
        assertTrue(result);
        verify(jwtService).validateToken("abc.def.ghi");
    }
}
