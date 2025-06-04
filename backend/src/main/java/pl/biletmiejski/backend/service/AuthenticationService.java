package pl.biletmiejski.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.biletmiejski.backend.dto.*;
import pl.biletmiejski.backend.model.*;
import pl.biletmiejski.backend.repository.*;
import pl.biletmiejski.backend.security.JwtService;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    // register
    public AuthenticationResponse register(RegisterRequest request) {
        Role role = request.getRole();
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        userRepository.save(user);

        String jwt = jwtService.generateToken(user.getEmail());
        Token token = Token.builder()
                .token(jwt)
                .user(user)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.save(token);

        return AuthenticationResponse.builder()
                .token(jwt)
                .build();
    }

    // login
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()
                )
        );

        // user not registered yet
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // revoke old tokens - safety (e.g. multi-device login)
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);

        // create new jwt token
        String jwt = jwtService.generateToken(user.getEmail());
        tokenRepository.save(Token.builder()
                .token(jwt)
                .user(user)
                .expired(false)
                .revoked(false)
                .build());

        return AuthenticationResponse.builder()
                .token(jwt)
                .build();
    }

    // logout
    public void logout(String token) {
        tokenRepository.findByToken(token).ifPresent(storedToken -> {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
        });
    }
}
