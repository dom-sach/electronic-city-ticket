package pl.biletmiejski.backend.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuyTicketRequest {
    private Long ticketTypeId;
}
