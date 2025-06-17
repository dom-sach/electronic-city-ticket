package pl.biletmiejski.backend.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import pl.biletmiejski.backend.dto.AuthenticationRequest;
import pl.biletmiejski.backend.dto.AuthenticationResponse;
import pl.biletmiejski.backend.dto.RegisterRequest;
import pl.biletmiejski.backend.model.Role;
import pl.biletmiejski.backend.model.Token;
import pl.biletmiejski.backend.model.User;
import pl.biletmiejski.backend.repository.TokenRepository;
import pl.biletmiejski.backend.repository.UserRepository;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class AuthenticationServiceIntegrationTest {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenRepository tokenRepository;

    // test register()
    @Test
    void register_createsNewUserAndToken() {
        String email = "newuser_" + UUID.randomUUID() + "@test.com";
        // given
        RegisterRequest request = RegisterRequest.builder()
                .email(email)
                .password("test123")
                .role(Role.PASSENGER)
                .build();

        // when
        AuthenticationResponse response = authenticationService.register(request);

        // then
        User createdUser = userRepository.findByEmail(email).orElseThrow();
        assertNotNull(createdUser.getId());
        assertTrue(createdUser.getEmail().equals(email));
        assertTrue(createdUser.getRole() == Role.PASSENGER);

        List<Token> tokens = tokenRepository.findAllValidTokensByUser(createdUser.getId());
        assertEquals(1, tokens.size());
        assertEquals(response.getToken(), tokens.get(0).getToken());
    }

    @AfterEach
    void tearDown() {
        tokenRepository.deleteAll();
        userRepository.deleteAll();
    }


    // test authenticate()
    @BeforeEach
    void setup() {
        // ensure test user exists
        if (userRepository.findByEmail("user1@test.com").isEmpty()) {
            User user = User.builder()
                    .email("user1@test.com")
                    .password(passwordEncoder.encode("123456"))
                    .role(Role.PASSENGER)
                    .build();
            userRepository.save(user);
        }
    }

    @Test
    void authenticate_validCredentials_returnsTokenAndRevokesOldOnes() {
        // given
        User user = userRepository.findByEmail("user1@test.com").orElseThrow();

        // create existing token to be revoked
        Token oldToken = Token.builder()
                .token("oldToken123")
                .user(user)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(oldToken);

        AuthenticationRequest request = AuthenticationRequest.builder()
                .email("user1@test.com")
                .password("123456")
                .build();

        // when
        AuthenticationResponse response = authenticationService.authenticate(request);

        // then
        List<Token> tokens = tokenRepository.findAllValidTokensByUser(user.getId());
        assertEquals(1, tokens.size());
        assertEquals(response.getToken(), tokens.get(0).getToken());

        Token revokedToken = tokenRepository.findByToken("oldToken123").orElseThrow();
        assertTrue(revokedToken.isExpired());
        assertTrue(revokedToken.isRevoked());
    }


    // test logout()
    @BeforeEach
    void setupUser() {
        if (userRepository.findByEmail("user1@test.com").isEmpty()) {
            User user = User.builder()
                    .email("user1@test.com")
                    .password(passwordEncoder.encode("123456"))
                    .role(Role.PASSENGER)
                    .build();
            userRepository.save(user);
        }
    }

    @Test
    void logout_validToken_marksTokenAsExpiredAndRevoked() {
        // given
        User user = userRepository.findByEmail("user1@test.com").orElseThrow();

        Token token = Token.builder()
                .token("logoutTestToken123")
                .user(user)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.save(token);

        // when
        authenticationService.logout("logoutTestToken123");

        // then
        Token updatedToken = tokenRepository.findByToken("logoutTestToken123").orElseThrow();
        assertTrue(updatedToken.isExpired());
        assertTrue(updatedToken.isRevoked());
    }
}
