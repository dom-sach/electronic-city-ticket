package pl.biletmiejski.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.biletmiejski.backend.model.TicketType;

public interface TicketTypeRepository extends JpaRepository<TicketType, Long> {
}
