package pl.biletmiejski.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pl.biletmiejski.backend.dto.AuthenticationRequest;
import pl.biletmiejski.backend.dto.AuthenticationResponse;
import pl.biletmiejski.backend.dto.BuyTicketRequest;
import pl.biletmiejski.backend.dto.CreateTicketTypeRequest;
import pl.biletmiejski.backend.model.*;
import pl.biletmiejski.backend.repository.TicketTypeRepository;
import pl.biletmiejski.backend.repository.UserRepository;
import pl.biletmiejski.backend.security.JwtService;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketTypeRepository ticketTypeRepository;

    private String passengerToken;
    private String adminToken;
    private String userToken;

    @BeforeEach
    void setup() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest("user1@test.com", "123456");

        String requestBody = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        AuthenticationResponse response = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                AuthenticationResponse.class
        );

        userToken = response.getToken();
        passengerToken = jwtService.generateToken(
                userRepository.findByEmail("user1@test.com").orElseThrow()
        );
        adminToken = jwtService.generateToken(
                userRepository.save(User.builder()
                        .email("admin@test.com")
                        .password("dummy")
                        .role(Role.ADMINISTRATOR)
                        .build())
        );
    }

    @Test
    void getTicketTypes_shouldReturnAvailableTypes() throws Exception {
        mockMvc.perform(get("/api/tickets/types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void getTicketDetails_shouldReturnTicket_whenExists() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/tickets/check")
                        .param("code", "ticket_01")
                        .header("Authorization", "Bearer " + userToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("ticket_01"))
                .andExpect(jsonPath("$.ticketTypeName.name").value("TicketType01"))
                .andReturn();

        String json = result.getResponse().getContentAsString();
        System.out.println("==== JSON RESPONSE ====");
        System.out.println(json);
    }

    @Test
    void getTicketDetails_shouldReturn404_whenNotExists() throws Exception {
        mockMvc.perform(get("/api/tickets/check")
                        .param("code", "not_existing")
                        .header("Authorization", "Bearer " + userToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void getMyTickets_shouldReturnUserTickets() throws Exception {
        mockMvc.perform(get("/api/tickets/my")
                        .header("Authorization", "Bearer " + passengerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void buyTicket_shouldCreateNewTicket() throws Exception {
        Long typeId = ticketTypeRepository.findAll().get(0).getId();
        BuyTicketRequest request = new BuyTicketRequest();
        request.setTicketTypeId(typeId);

        mockMvc.perform(post("/api/tickets/buy")
                        .header("Authorization", "Bearer " + passengerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticketTypeName").isNotEmpty());
    }

    @Test
    void addTicketType_shouldAddType_whenAdmin() throws Exception {
        CreateTicketTypeRequest request = new CreateTicketTypeRequest();
        request.setName("Test Ticket");
        request.setCategory(TicketCategory.TIME);
        request.setDiscountType(DiscountType.NORMAL);
        request.setPrice(BigDecimal.valueOf(8));
        request.setDurationMinutes(30);

        mockMvc.perform(post("/api/tickets/types")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Ticket"));
    }

    @Test
    void deleteTicketType_shouldRemoveType_whenAdmin() throws Exception {
        Long typeId = ticketTypeRepository.save(
                TicketType.builder()
                        .name("Deletable")
                        .category(TicketCategory.TIME)
                        .discountType(DiscountType.NORMAL)
                        .price(BigDecimal.TEN)
                        .durationMinutes(30)
                        .build()).getId();

        mockMvc.perform(delete("/api/tickets/types/" + typeId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());
    }
}
