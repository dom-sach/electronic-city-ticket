package pl.biletmiejski.backend.service;

import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.biletmiejski.backend.model.*;
import pl.biletmiejski.backend.repository.*;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private TicketTypeRepository ticketTypeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private TicketService ticketService;

    public TicketServiceTest() {
        //MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void setup(TestInfo testInfo) {
        MockitoAnnotations.openMocks(this);
        System.out.println("\n[TicketServiceTest] Starting test: " + testInfo.getDisplayName() + "\n");
    }

    @AfterEach
    void teardown(TestInfo testInfo) {
        System.out.println("\n[TicketServiceTest] Finished test: " + testInfo.getDisplayName() + "\n");
    }

    @Test
    void shouldReturnAvailableTicketTypes() {
        TicketType ticket1 = TicketType.builder()
                .id(1L)
                .name("Bilet 30-minutowy")
                .price(BigDecimal.valueOf(3.50))
                .category(TicketCategory.TIME)
                .discountType(DiscountType.NORMAL)
                .durationMinutes(30)
                .build();

        TicketType ticket2 = TicketType.builder()
                .id(2L)
                .name("Bilet miesięczny")
                .price(BigDecimal.valueOf(120.00))
                .category(TicketCategory.PERIOD)
                .discountType(DiscountType.DISCOUNT)
                .durationMinutes(43200)
                .build();

        when(ticketTypeRepository.findAll()).thenReturn(List.of(ticket1, ticket2));

        List<TicketType> result = ticketService.getAvailableTicketTypes();

        assertEquals(2, result.size());
        assertEquals("Bilet 30-minutowy", result.get(0).getName());
        assertEquals("Bilet miesięczny", result.get(1).getName());
    }
}
