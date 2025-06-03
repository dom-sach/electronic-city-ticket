package pl.biletmiejski.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.token.TokenService;
import org.springframework.web.bind.annotation.*;
import pl.biletmiejski.backend.dto.*;
import pl.biletmiejski.backend.model.Token;
import pl.biletmiejski.backend.repository.TokenRepository;
import pl.biletmiejski.backend.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authService;
    private final TokenRepository tokenRepository;

    // REGISTER
    @Operation(summary = "Rejestracja", description = "Umożliwia rejestrację nowego użytkownika")
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    // LOGIN
    @Operation(summary = "Logowanie", description = "Umożliwia zalogowanie zarejestrowanego użytkownika")
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    // LOGOUT
    @Operation(summary = "Wylogowanie", description = "Wylogowuje użytkownika i deaktywuje jego token dostępu JWT")
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("No JWT token");
        }
        String jwt = authHeader.substring(7);
        authService.logout(jwt);
        return ResponseEntity.ok("Logout successful");
    }
}