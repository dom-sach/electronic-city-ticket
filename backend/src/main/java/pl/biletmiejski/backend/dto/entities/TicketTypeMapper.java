package pl.biletmiejski.backend.dto.entities;

import pl.biletmiejski.backend.model.TicketType;

public class TicketTypeMapper {
    public static TicketTypeDto toDto(TicketType ticketType) {
        return new TicketTypeDto(
                ticketType.getId(),
                ticketType.getName(),
                ticketType.getCategory(),
                ticketType.getDiscountType(),
                ticketType.getPrice(),
                ticketType.getDurationMinutes()
        );
    }
}
