package pl.biletmiejski.backend.dto.entities;

import pl.biletmiejski.backend.model.DiscountType;
import pl.biletmiejski.backend.model.TicketCategory;

import java.math.BigDecimal;

public record TicketTypeDto(
        Long id,
        String name,
        TicketCategory category,
        DiscountType discountType,
        BigDecimal price,
        Integer durationMinutes
) {}
