package pl.biletmiejski.backend.dto.entities;

import org.springframework.stereotype.Component;
import pl.biletmiejski.backend.model.User;

@Component
public class UserMapper {

    // Metoda do mapowania User na UserDto
    public static UserDto toDto(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getRole()
        );
    }

}

