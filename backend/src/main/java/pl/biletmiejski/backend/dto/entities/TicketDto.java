package pl.biletmiejski.backend.dto.entities;

import pl.biletmiejski.backend.model.TicketType;

import java.time.LocalDateTime;

public record TicketDto(
        Long id,
        String code,
        LocalDateTime purchaseDate,
        LocalDateTime activationDate,
        LocalDateTime validUntil,
        boolean used,
        TicketType ticketTypeName,
        String vehicleId
) {}

