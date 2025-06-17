package pl.biletmiejski.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import pl.biletmiejski.backend.dto.BuyTicketRequest;
import pl.biletmiejski.backend.model.Ticket;
import pl.biletmiejski.backend.model.TicketCategory;
import pl.biletmiejski.backend.model.TicketType;
import pl.biletmiejski.backend.model.User;
import pl.biletmiejski.backend.repository.TicketRepository;
import pl.biletmiejski.backend.repository.TicketTypeRepository;
import pl.biletmiejski.backend.repository.UserRepository;
import pl.biletmiejski.backend.repository.VehicleRepository;
import pl.biletmiejski.backend.service.TicketService;

import java.time.LocalDateTime;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "spring.profiles.active=test")
@ActiveProfiles("test")
class TicketServiceIntegrationTest {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketTypeRepository ticketTypeRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    // test getAvailableTicketTypes()
    @Test
    void shouldLoadAvailableTicketTypesFromDatabase() {
        // when
        List<TicketType> ticketTypes = ticketService.getAvailableTicketTypes();

        // then
        assertThat(ticketTypes).hasSize(2);

        assertThat(ticketTypes).anySatisfy(type ->
                assertThat(type.getName()).isEqualTo("TicketType01"));
        assertThat(ticketTypes).anySatisfy(type ->
                assertThat(type.getName()).isEqualTo("TicketType02"));
    }

    // test buyTicket()
    @BeforeEach
    void setupAuthentication() {
        // logged in user user1@test.com
        var auth = new UsernamePasswordAuthenticationToken("user1@test.com", null, List.of());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void shouldBuyTicketSuccessfully() {
        // given
        BuyTicketRequest request = new BuyTicketRequest();
        request.setTicketTypeId(1L);

        // when
        Ticket ticket = ticketService.buyTicket(request);

        // then
        assertThat(ticket).isNotNull();
        assertThat(ticket.getId()).isNotNull();
        assertThat(ticket.getTicketType().getId()).isEqualTo(1L);
        assertThat(ticket.getUser().getEmail()).isEqualTo("user1@test.com");
        assertThat(ticket.getCode()).isNotBlank();
        assertThat(ticket.isUsed()).isFalse();
        assertThat(ticket.getPurchaseDate()).isNotNull();
    }

    // test getMyTickets()
    @Test
    void shouldReturnOnlyTicketsOfAuthenticatedUser() {
        // given – user user1@test.com has 1 test ticket assigned
        var auth = new UsernamePasswordAuthenticationToken("user1@test.com", null, List.of());
        SecurityContextHolder.getContext().setAuthentication(auth);

        // when
        List<Ticket> tickets = ticketService.getMyTickets();

        // then
        assertThat(tickets).isNotNull();
        assertThat(tickets).hasSize(1);
        assertThat(tickets.get(0).getCode()).isEqualTo("ticket_01");
        assertThat(tickets.get(0).getUser().getEmail()).isEqualTo("user1@test.com");
    }

    // test getTicketByCode(String code)
    @Test
    void shouldReturnTicketByCode() {
        // when
        Ticket ticket = ticketService.getTicketByCode("ticket_01");

        // then
        assertThat(ticket).isNotNull();
        assertThat(ticket.getCode()).isEqualTo("ticket_01");
        assertThat(ticket.getUser().getEmail()).isEqualTo("user1@test.com");
        assertThat(ticket.getTicketType().getName()).isEqualTo("TicketType01");
        assertThat(ticket.isUsed()).isTrue();
    }

    // test activateTicket(String code, String vehicleId)
    @Test
    void shouldActivateTicket() {
        // given
        User user = userRepository.findByEmail("user1@test.com")
                .orElseThrow();
        TicketType type = ticketTypeRepository.findAll().stream()
                .filter(t -> t.getCategory() == TicketCategory.TIME)
                .findFirst()
                .orElseThrow();

        Ticket ticket = Ticket.builder()
                .code("test_activation_code")
                .user(user)
                .ticketType(type)
                .purchaseDate(LocalDateTime.now())
                .used(false)
                .build();

        ticket = ticketRepository.save(ticket);

        // log in as user1@test.com
        TestingAuthenticationToken auth = new TestingAuthenticationToken(user.getEmail(), null);
        SecurityContextHolder.getContext().setAuthentication(auth);

        // when
        Ticket activated = ticketService.activateTicket("test_activation_code", "VehicleTest01");

        // then
        assertThat(activated.isUsed()).isTrue();
        assertThat(activated.getActivationDate()).isNotNull();
        assertThat(activated.getValidUntil()).isNotNull();
        assertThat(activated.getActivatedIn().getVehicleId()).isEqualTo("VehicleTest01");
    }

    @Test
    void shouldReturnTrueForValidTimeTicket() {
        // given
        String code = "ticket_01";
        String vehicleId = "Vehicle01";

        // is ticket expiration date < current date
        Ticket ticket = ticketRepository.findByCode(code).orElseThrow();
        assertThat(ticket.getTicketType().getCategory()).isEqualTo(TicketCategory.TIME);
        assertThat(ticket.getValidUntil()).isAfter(LocalDateTime.now());

        // when
        boolean valid = ticketService.isTicketValid(code, vehicleId);

        // then
        assertThat(valid).isTrue();
    }

    @Test
    void shouldReturnFalseForExpiredTicket() {
        // given
        User user = userRepository.findByEmail("user1@test.com").orElseThrow();
        TicketType type = ticketTypeRepository.findAll().stream()
                .filter(t -> t.getCategory() == TicketCategory.TIME)
                .findFirst().orElseThrow();

        Ticket ticket = Ticket.builder()
                .code("expired_ticket")
                .user(user)
                .ticketType(type)
                .purchaseDate(LocalDateTime.now().minusDays(10))
                .activationDate(LocalDateTime.now().minusDays(5))
                .validUntil(LocalDateTime.now().minusDays(1))
                .used(true)
                .activatedIn(vehicleRepository.findByVehicleId("Vehicle01").orElse(null))
                .build();

        ticketRepository.save(ticket);

        // when
        boolean valid = ticketService.isTicketValid("expired_ticket", "Vehicle01");

        // then
        assertThat(valid).isFalse();
    }

}
