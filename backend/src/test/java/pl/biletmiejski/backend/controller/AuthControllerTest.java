package pl.biletmiejski.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.biletmiejski.backend.dto.AuthenticationRequest;
import pl.biletmiejski.backend.dto.RegisterRequest;
import pl.biletmiejski.backend.model.Role;
import pl.biletmiejski.backend.model.Token;
import pl.biletmiejski.backend.repository.TokenRepository;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenRepository tokenRepository;

    @BeforeEach
    void setup() {
        tokenRepository.deleteAll();
    }

    // register test
    @Test
    void register_shouldReturnToken_whenValidData() throws Exception {
        RegisterRequest request = new RegisterRequest(
                "newuser@test.com",
                "securePassword123",
                Role.PASSENGER
        );

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    // login test
    @Test
    void login_shouldReturnToken_whenCredentialsAreCorrect() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest(
                "user1@test.com",
                "123456"
        );

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    // logout test
    @Test
    void logout_shouldRevokeToken_whenTokenIsValid() throws Exception {
        // get token
        AuthenticationRequest loginRequest = new AuthenticationRequest(
                "user1@test.com",
                "123456"
        );

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        String token = objectMapper.readTree(responseBody).get("token").asText();

        // logout
        mockMvc.perform(post("/api/auth/logout")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("Logout successful"));

        // token deactivated
        Token storedToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token not found"));

        assertTrue(storedToken.isExpired());
        assertTrue(storedToken.isRevoked());
    }
}
