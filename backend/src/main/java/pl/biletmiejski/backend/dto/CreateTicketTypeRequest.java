package pl.biletmiejski.backend.dto;

import lombok.*;
import pl.biletmiejski.backend.model.DiscountType;
import pl.biletmiejski.backend.model.TicketCategory;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTicketTypeRequest {
    private String name;
    private TicketCategory category;
    private DiscountType discountType;
    private BigDecimal price;
    private Integer durationMinutes; // null jeśli nie dotyczy
}

