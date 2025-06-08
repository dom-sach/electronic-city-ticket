package pl.biletmiejski.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.biletmiejski.backend.dto.entities.UserDto;
import pl.biletmiejski.backend.dto.entities.UserMapper;
import pl.biletmiejski.backend.model.User;
import pl.biletmiejski.backend.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Operation(summary = "Profil", description = "Umożliwia uzyskanie informacji o zalogowanym użytkowniku")
    @GetMapping("/current")
    public ResponseEntity<UserDto> getCurrentUser(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            System.out.println("Authenticated user: " + authentication.getName());
            User user = (User) authentication.getPrincipal();
            if (user != null) {
                UserDto userDto = new UserDto(user.getId(), user.getEmail(), user.getRole());
                return ResponseEntity.ok(userDto);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // get all users
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @Operation(summary = "Pobierz wszystkich użytkowników", description = "Pobiera listę użytkowników pogrupowaną po roli")
    @GetMapping
    public ResponseEntity<Map<String, List<UserDto>>> getAllUsers() {
        List<User> users = userRepository.findAll();
        Map<String, List<UserDto>> groupedUsers = users.stream()
                .collect(Collectors.groupingBy(user -> user.getRole().name(),
                        Collectors.mapping(UserMapper::toDto, Collectors.toList())));

        return ResponseEntity.ok(groupedUsers);
    }
}