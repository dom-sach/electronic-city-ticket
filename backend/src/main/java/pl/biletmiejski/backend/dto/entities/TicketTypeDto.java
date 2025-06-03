package pl.biletmiejski.backend.dto.entities;

import java.math.BigDecimal;

public record TicketTypeDto(
        Long id,
        String name,
        String category,
        String discountType,
        BigDecimal price,
        Integer durationMinutes
) {}
