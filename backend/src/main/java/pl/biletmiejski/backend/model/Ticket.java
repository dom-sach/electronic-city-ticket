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
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "ticket_type_id")
    private TicketType ticketType;

    private LocalDateTime purchaseDate;

    private LocalDateTime activationDate;

    private LocalDateTime validUntil;

    private boolean used;

    @ManyToOne
    private Vehicle activatedIn;
}
