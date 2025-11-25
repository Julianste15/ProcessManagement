package co.unicauca.user.controller;
import co.unicauca.user.dto.RegisterRequest;
import co.unicauca.user.model.User;
import co.unicauca.user.model.enums.Career;
import co.unicauca.user.model.enums.Role;
import co.unicauca.user.service.UserService;
import co.unicauca.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.HashMap;
import java.util.Map;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void register_validRequest_returnsUserDTO() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setNames("Juan");
        request.setSurnames("Pérez");
        request.setEmail("juan@unicauca.edu.co");
        request.setPassword("123456");
        request.setTelephone(3001234567L);
        request.setCareer(Career.SYSTEMS_ENGINEERING);
        request.setRole(Role.STUDENT);

        User saved = new User();
        saved.setId(1L);
        saved.setNames("Juan");
        saved.setSurnames("Pérez");
        saved.setEmail("juan@unicauca.edu.co");
        saved.setTelephone(3001234567L);
        saved.setCareer(Career.SYSTEMS_ENGINEERING);
        saved.setRole(Role.STUDENT);

        when(userService.registerUser(any(User.class))).thenReturn(saved);

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("juan@unicauca.edu.co"))
                .andExpect(jsonPath("$.role").value("estudiante"));
    }

    @Test
    void validateCredentials_validUser_returnsOk() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("email", "user@unicauca.edu.co");
        body.put("password", "123456");

        User user = new User();
        user.setEmail("user@unicauca.edu.co");
        user.setPassword("enc");
        user.setNames("User");
        user.setSurnames("Test");
        user.setRole(Role.STUDENT);

        when(userService.getUserByEmail("user@unicauca.edu.co")).thenReturn(user);
        when(userService.validatePassword("123456", "enc")).thenReturn(true);

        mockMvc.perform(post("/api/users/validate-credentials")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@unicauca.edu.co"))
                .andExpect(jsonPath("$.role").value("STUDENT"))
                .andExpect(jsonPath("$.names").value("User"))
                .andExpect(jsonPath("$.surnames").value("Test"));
    }

    @Test
    void getCurrentUserProfile_withoutHeader_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/users/profile"))
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }
}
