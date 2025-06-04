package pl.biletmiejski.backend.dto.entities;

import java.time.LocalDateTime;

public record TicketDto(
        Long id,
        String code,
        LocalDateTime purchaseDate,
        LocalDateTime activationDate,
        LocalDateTime validUntil,
        boolean used,
        String ticketTypeName,
        String vehicleId
) {}

