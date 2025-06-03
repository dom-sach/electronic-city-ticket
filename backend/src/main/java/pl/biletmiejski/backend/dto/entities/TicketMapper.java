package pl.biletmiejski.backend.dto.entities;

import pl.biletmiejski.backend.model.Ticket;

public class TicketMapper {
    public static TicketDto toDto(Ticket ticket) {
        return new TicketDto(
                ticket.getId(),
                ticket.getCode(),
                ticket.getPurchaseDate(),
                ticket.getActivationDate(),
                ticket.getValidUntil(),
                ticket.isUsed(),
                ticket.getTicketType() != null ? ticket.getTicketType().getName() : null,
                ticket.getActivatedIn() != null ? ticket.getActivatedIn().getVehicleId() : null
        );
    }
}
