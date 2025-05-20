package pl.biletmiejski.backend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidateTicketRequest {
    private String code;
    private String vehicleId;
}