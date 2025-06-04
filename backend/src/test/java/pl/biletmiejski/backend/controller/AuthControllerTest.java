package pl.biletmiejski.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.biletmiejski.backend.dto.*;
import pl.biletmiejski.backend.model.Role;
import pl.biletmiejski.backend.service.AuthenticationService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthControllerTest {

    @BeforeEach
    void beforeEach(TestInfo testInfo) {
        System.out.println("\n[AuthControllerTest] Starting test: " + testInfo.getDisplayName() + "\n");
    }

    @AfterEach
    void afterEach(TestInfo testInfo) {
        System.out.println("\n[AuthControllerTest] Finished test: " + testInfo.getDisplayName() + "\n");
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldRegisterUser() throws Exception {
        RegisterRequest request = new RegisterRequest("test@example.com", "password", Role.PASSENGER);
        AuthenticationResponse response = AuthenticationResponse.builder().token("jwt-token").build();

        when(authService.register(any(RegisterRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"));
    }

    @Test
    void shouldAuthenticateUser() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest("test@example.com", "password");
        AuthenticationResponse response = AuthenticationResponse.builder().token("jwt-token").build();

        when(authService.authenticate(any(AuthenticationRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"));
    }

    @Test
    void shouldLogoutUserWithValidToken() throws Exception {
        String jwt = "valid-jwt-token";

        doNothing().when(authService).logout(jwt);

        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(content().string("Logout successful"));
    }

    @Test
    void shouldReturnBadRequestOnLogoutWithoutToken() throws Exception {
        mockMvc.perform(post("/api/auth/logout"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("No JWT token"));
    }
}
