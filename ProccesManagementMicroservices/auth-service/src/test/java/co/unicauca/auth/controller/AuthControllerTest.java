package co.unicauca.auth.controller;
import co.unicauca.auth.dto.AuthResponse;
import co.unicauca.auth.dto.LoginRequest;
import co.unicauca.auth.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void login_validCredentials_returnsAuthResponse() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("user@unicauca.edu.co");
        request.setPassword("pwd");

        AuthResponse authResponse = new AuthResponse(
                "jwt-token",
                "user@unicauca.edu.co",
                "STUDENT",
                "User Test",
                "Bearer",
                false,
                null,
                "NOT_SUBMITTED");

        when(authService.authenticate(any(LoginRequest.class))).thenReturn(authResponse);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.email").value("user@unicauca.edu.co"))
                .andExpect(jsonPath("$.role").value("STUDENT"));
    }

    @Test
    void validateToken_validToken_returnsTrue() throws Exception {
        when(authService.validateToken("abc")).thenReturn(true);

        mockMvc.perform(get("/api/auth/validate-token")
                .header("Authorization", "Bearer abc"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void logout_withBearerToken_callsServiceAndReturnsOk() throws Exception {
        mockMvc.perform(post("/api/auth/logout")
                .header("Authorization", "Bearer abc"))
                .andExpect(status().isOk())
                .andExpect(content().string("Logout exitoso"));

        verify(authService).logout("abc");
    }
}
