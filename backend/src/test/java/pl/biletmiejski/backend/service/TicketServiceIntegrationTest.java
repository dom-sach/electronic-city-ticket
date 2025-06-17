package pl.biletmiejski.backend.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pl.biletmiejski.backend.model.TicketType;
import pl.biletmiejski.backend.service.TicketService;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "spring.profiles.active=test")
@ActiveProfiles("test")
class TicketServiceIntegrationTest {

    @Autowired
    private TicketService ticketService;

    @Test
    void shouldLoadAvailableTicketTypesFromDatabase() {
        List<TicketType> types = ticketService.getAvailableTicketTypes();
        assertThat(types).hasSize(2);
    }
}
