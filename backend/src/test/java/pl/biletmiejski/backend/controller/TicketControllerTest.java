package pl.biletmiejski.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pl.biletmiejski.backend.dto.*;
import pl.biletmiejski.backend.dto.entities.TicketTypeDto;
import pl.biletmiejski.backend.model.*;
import pl.biletmiejski.backend.repository.TicketTypeRepository;
import pl.biletmiejski.backend.service.TicketService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketService ticketService;

    @MockBean
    private TicketTypeRepository ticketTypeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void logStart(TestInfo testInfo) {
        System.out.println("[TicketControllerTest] Starting test: " + testInfo.getDisplayName());
    }

    @AfterEach
    void logEnd(TestInfo testInfo) {
        System.out.println("[TicketControllerTest] Finished test: " + testInfo.getDisplayName() + " => SUCCESS :)");
    }

    @Test
    @DisplayName("should return available ticket types")
    void shouldReturnTicketTypes() throws Exception {
        // given
        TicketTypeDto type1 = new TicketTypeDto(1L, "Bilet 30-minutowy", TicketCategory.TIME, DiscountType.NORMAL, BigDecimal.valueOf(3.5), 30);
        TicketTypeDto type2 = new TicketTypeDto(2L, "Bilet 60-minutowy", TicketCategory.TIME, DiscountType.DISCOUNT, BigDecimal.valueOf(5.0), 60);
        when(ticketService.getAvailableTicketTypes()).thenReturn(List.of(
                TicketType.builder()
                        .name("Bilet 30-minutowy")
                        .category(TicketCategory.TIME)
                        .discountType(DiscountType.NORMAL)
                        .build(),
                TicketType.builder()
                        .name("Bilet 60-minutowy")
                        .category(TicketCategory.TIME)
                        .discountType(DiscountType.DISCOUNT)
                        .build()
        ));

        // when & then
        mockMvc.perform(get("/api/tickets/types")
                        .with(user("user").roles("PASSENGER")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("should delete ticket type when ADMIN")
    @WithMockUser(roles = "ADMINISTRATOR")
    void shouldDeleteTicketType() throws Exception {
        // wystarczy tylko to:
        doNothing().when(ticketService).deleteTicketType(1L);

        mockMvc.perform(delete("/api/tickets/types/1")
                        .with(user("admin").roles("ADMINISTRATOR")))
                .andExpect(status().isNoContent());

        verify(ticketService).deleteTicketType(1L); // opcjonalne potwierdzenie
    }
}
