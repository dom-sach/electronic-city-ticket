package pl.biletmiejski.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.biletmiejski.backend.dto.*;
import pl.biletmiejski.backend.model.*;
import pl.biletmiejski.backend.repository.*;
import pl.biletmiejski.backend.security.JwtService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// TODO
// Co powinno się wydarzyć?
// register():
//  - stworzenie nowego użytkownika
//  - zakodowanie hasła do hasha
//  - wygenerowanie nowego tokenu JWT
//  - zapisanie tokenu
//  - zwrócenie obiektu AuthenticationResponse już z tokenem
//
// authenticate():
//  - powinien wywołać authenticationManager.authenticate()
//  - pobrać użytkownika po e-mailu
//  - wygenerować token JWT
//  - zapisać token
//  - zwrócić token w AuthenticationResponse


public class AuthenticationServiceTest {
    // Mocks
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

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // Test register()
    @Test
    void shouldRegisterNewUserAndReturnToken() {
        // given
        RegisterRequest request = new RegisterRequest("user1@gmail.com", "123456", "PASSENGER");

        when(passwordEncoder.encode("123456")).thenReturn("hashed_123456");
        when(jwtService.generateToken("user1@gmail.com")).thenReturn("jwt_token");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<Token> tokenCaptor = ArgumentCaptor.forClass(Token.class);

        // when
        AuthenticationResponse response = authenticationService.register(request);

        // then
        verify(userRepository).save(userCaptor.capture());
        verify(tokenRepository).save(tokenCaptor.capture());

        User savedUser = userCaptor.getValue();
        Token savedToken = tokenCaptor.getValue();

        assertEquals("user1@gmail.com", savedUser.getEmail());
        assertEquals("hashed_123456", savedUser.getPassword());
        assertEquals("jwt_token", savedToken.getToken());
        assertEquals("jwt_token", response.getToken());
    }

    // Test authenticate()
    @Test
    void shouldAuthenticateAndReturnToken() {
        // given
        AuthenticationRequest request = new AuthenticationRequest("user1@gmail.com", "123456");

        User user = User.builder()
                .email("user1@gmail.com")
                .password("hashed_123456")
                .role(Role.PASSENGER)
                .build();

        when(userRepository.findByEmail("user1@gmail.com")).thenReturn(Optional.of(user));
        when(jwtService.generateToken("user1@gmail.com")).thenReturn("jwt_token");

        // when
        AuthenticationResponse response = authenticationService.authenticate(request);

        // then
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenRepository).save(any(Token.class));

        assertEquals("jwt_token", response.getToken());
    }

}
