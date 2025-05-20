package pl.biletmiejski.backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.biletmiejski.backend.dto.BuyTicketRequest;
import pl.biletmiejski.backend.dto.CreateTicketTypeRequest;
import pl.biletmiejski.backend.model.*;
import pl.biletmiejski.backend.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final VehicleRepository vehicleRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final UserRepository userRepository;

    public List<TicketType> getAvailableTicketTypes() {
        return ticketTypeRepository.findAll();
    }

    @Transactional
    public Ticket buyTicket(BuyTicketRequest request) {
        TicketType type = ticketTypeRepository.findById(request.getTicketTypeId())
                .orElseThrow(() -> new RuntimeException("Ticket type not found"));

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Ticket.TicketBuilder builder = Ticket.builder()
                .code(UUID.randomUUID().toString())
                .user(user)
                .ticketType(type)
                .purchaseDate(LocalDateTime.now())
                .used(false);

        // Okresowy – ustaw daty od razu
        if (type.getCategory() == TicketCategory.PERIOD && type.getDurationMinutes() != null) {
            builder.validUntil(LocalDateTime.now().plusMinutes(type.getDurationMinutes()));
        }

        return ticketRepository.save(builder.build());
    }

    public List<Ticket> getMyTickets() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ticketRepository.findAll().stream()
                .filter(t -> t.getUser().equals(user))
                .toList();
    }

    // skasowanie biletu
    public Ticket activateTicket(String code, String vehicleId) {
        Ticket ticket = ticketRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (ticket.getActivationDate() != null) {
            throw new RuntimeException("Ticket already activated");
        }

        TicketType type = ticket.getTicketType();
        if (type.getCategory() == TicketCategory.PERIOD) {
            throw new RuntimeException("Periodic tickets do not need activation");
        }

        Vehicle vehicle = vehicleRepository.findByVehicleId(vehicleId)
                .orElseGet(() -> vehicleRepository.save(
                        Vehicle.builder().vehicleId(vehicleId).build()));

        ticket.setActivationDate(LocalDateTime.now());
        ticket.setActivatedIn(vehicle);

        if (type.getCategory() == TicketCategory.TIME) {
            ticket.setValidUntil(ticket.getActivationDate().plusMinutes(type.getDurationMinutes()));
        }

        return ticketRepository.save(ticket);
    }

    // sprawdzanie czy bilet jest ważny
    public boolean isTicketValid(String code, String vehicleId) {
        Ticket ticket = ticketRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        TicketType type = ticket.getTicketType();
        LocalDateTime now = LocalDateTime.now();

        return switch (type.getCategory()) {
            case PERIOD -> ticket.getValidUntil() != null && now.isBefore(ticket.getValidUntil());
            case ONE_TIME -> ticket.getActivationDate() != null &&
                    ticket.getActivatedIn() != null &&
                    ticket.getActivatedIn().getVehicleId().equals(vehicleId);
            case TIME -> ticket.getActivationDate() != null &&
                    ticket.getValidUntil() != null &&
                    now.isBefore(ticket.getValidUntil());
        };
    }

    // dodawanie nowego typu biletu do oferty
    public TicketType addTicketType(CreateTicketTypeRequest request) {
        TicketType type = TicketType.builder()
                .name(request.getName())
                .category(request.getCategory())
                .discountType(request.getDiscountType())
                .price(request.getPrice())
                .durationMinutes(request.getDurationMinutes())
                .build();
        return ticketTypeRepository.save(type);
    }

    // usuwanie typu biletu z oferty
    @Transactional
    public void deleteTicketType(Long id) {
        TicketType type = ticketTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket type not found"));

        LocalDateTime now = LocalDateTime.now();

        // Usuń aktywne bilety tego typu
        List<Ticket> tickets = ticketRepository.findByTicketTypeAndUsedFalse(type)
                .stream()
                .filter(ticket -> ticket.getValidUntil() == null || ticket.getValidUntil().isAfter(now))
                .toList();

        ticketRepository.deleteAll(tickets);

        // Usuń sam typ
        ticketTypeRepository.delete(type);
    }

}