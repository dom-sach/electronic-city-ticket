package pl.biletmiejski.backend.service;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.biletmiejski.backend.dto.*;
import pl.biletmiejski.backend.model.*;
import pl.biletmiejski.backend.repository.*;
import pl.biletmiejski.backend.security.JwtAuthFilter;
import pl.biletmiejski.backend.security.JwtService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void shouldRegisterNewUserAndReturnToken() {
        RegisterRequest request = new RegisterRequest("test@example.com", "password", Role.PASSENGER);

        when(passwordEncoder.encode(anyString())).thenReturn("encoded-password");
        when(jwtService.generateToken(anyString())).thenReturn("jwt-token");

        AuthenticationResponse response = authenticationService.register(request);

        assertEquals("jwt-token", response.getToken());

        verify(userRepository).save(any(User.class));
        verify(tokenRepository).save(any(Token.class));
    }

    @Test
    void shouldAuthenticateAndReturnToken() {
        AuthenticationRequest request = new AuthenticationRequest("test@example.com", "password");

        User user = User.builder()
                .email("test@example.com")
                .password("encoded-password")
                .role(Role.PASSENGER)
                .build();

        when(authenticationManager.authenticate(any())).thenReturn(mock(Authentication.class));
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(anyString())).thenReturn("jwt-token");

        AuthenticationResponse response = authenticationService.authenticate(request);

        assertEquals("jwt-token", response.getToken());

        verify(tokenRepository).save(any(Token.class));
    }
}

