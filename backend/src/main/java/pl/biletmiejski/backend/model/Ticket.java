package pl.biletmiejski.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code; // Unikalny kod biletu (UUID jako string)

    @ManyToOne
    private User user;

    @ManyToOne
    private TicketType ticketType;

    private LocalDateTime purchaseDate;

    private LocalDateTime activationDate;

    private LocalDateTime validUntil;

    private boolean used;

    @ManyToOne
    private Vehicle activatedIn; // pojazd, w którym skasowano (dla jednorazowych/czasowych)
}
