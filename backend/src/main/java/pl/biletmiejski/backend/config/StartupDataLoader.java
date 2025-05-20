package pl.biletmiejski.backend.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.biletmiejski.backend.model.*;
import pl.biletmiejski.backend.repository.*;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StartupDataLoader {

    private final TicketTypeRepository ticketTypeRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        // Dodaj ofertę biletową, jeśli nie istnieje
        if (ticketTypeRepository.count() == 0) {
            List<TicketType> types = List.of(
                    TicketType.builder()
                            .name("Bilet jednorazowy normalny")
                            .category(TicketCategory.ONE_TIME)
                            .discountType(DiscountType.NORMAL)
                            .price(BigDecimal.valueOf(4.00))
                            .build(),
                    TicketType.builder()
                            .name("Bilet 30-minutowy ulgowy")
                            .category(TicketCategory.TIME)
                            .discountType(DiscountType.DISCOUNT)
                            .price(BigDecimal.valueOf(2.50))
                            .durationMinutes(30)
                            .build(),
                    TicketType.builder()
                            .name("Bilet miesięczny normalny")
                            .category(TicketCategory.PERIOD)
                            .discountType(DiscountType.NORMAL)
                            .price(BigDecimal.valueOf(120.00))
                            .durationMinutes(43200) // 30 dni
                            .build()
            );
            ticketTypeRepository.saveAll(types);
        }

        // Dodaj użytkowników testowych, jeśli nie istnieją
        if (userRepository.findByEmail("pasażer@test.pl").isEmpty()) {
            userRepository.save(
                    User.builder()
                            .email("pasażer@test.pl")
                            .password(passwordEncoder.encode("123456"))
                            .role(Role.PASSENGER)
                            .build()
            );
        }

        if (userRepository.findByEmail("bileter@test.pl").isEmpty()) {
            userRepository.save(
                    User.builder()
                            .email("bileter@test.pl")
                            .password(passwordEncoder.encode("123456"))
                            .role(Role.TICKET_INSPECTOR)
                            .build()
            );
        }
    }
}
