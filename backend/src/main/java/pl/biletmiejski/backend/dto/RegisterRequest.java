package pl.biletmiejski.backend.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String email;
    private String password;
    private String role; // "PASSENGER" lub "TICKET_INSPECTOR"
}