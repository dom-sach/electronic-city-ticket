package pl.biletmiejski.backend.dto.entities;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import pl.biletmiejski.backend.model.Role;

@Setter
@Getter
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private Role role;

    public UserDto(Long id, String email, Role role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

}
