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

    public AuthenticationResponse register(RegisterRequest request) {
        Role role = Role.valueOf(request.getRole().toUpperCase()); // np. PASSENGER
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

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String jwt = jwtService.generateToken(user.getEmail());

        // możesz dodać logikę odwoływania starych tokenów jeśli chcesz
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
}
