package pl.biletmiejski.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.biletmiejski.backend.model.Ticket;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByCode(String code);
}
