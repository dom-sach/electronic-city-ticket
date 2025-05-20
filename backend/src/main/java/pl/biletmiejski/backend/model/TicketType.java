package pl.biletmiejski.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // np. "Bilet 30-minutowy ulgowy"

    @Enumerated(EnumType.STRING)
    private TicketCategory category; // ONE_TIME / TIME / PERIOD

    @Enumerated(EnumType.STRING)
    private DiscountType discountType; // NORMAL / DISCOUNT

    private BigDecimal price;

    private Integer durationMinutes; // null jeśli nie dotyczy (np. bilet okresowy)
}
