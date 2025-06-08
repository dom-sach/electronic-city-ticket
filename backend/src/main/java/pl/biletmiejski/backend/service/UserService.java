package pl.biletmiejski.backend.service;

import org.springframework.security.core.context.SecurityContextHolder;
import pl.biletmiejski.backend.dto.entities.UserDto;
import pl.biletmiejski.backend.model.User;

public class UserService {

    public UserDto getCurrentUser() {
        // Pobierz użytkownika z kontekstu bezpieczeństwa
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new UserDto(user.getId(), user.getEmail(), user.getRole());
    }
}
