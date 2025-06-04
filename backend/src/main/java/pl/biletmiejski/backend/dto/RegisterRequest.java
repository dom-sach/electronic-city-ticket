package pl.biletmiejski.backend.dto;

import lombok.*;
import pl.biletmiejski.backend.model.Role;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String email;
    private String password;
    private Role role; // "PASSENGER" lub "TICKET_INSPECTOR"
}