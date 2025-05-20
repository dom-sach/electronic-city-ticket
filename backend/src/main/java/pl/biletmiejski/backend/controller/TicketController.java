package pl.biletmiejski.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.biletmiejski.backend.dto.BuyTicketRequest;
import pl.biletmiejski.backend.dto.CreateTicketTypeRequest;
import pl.biletmiejski.backend.model.Ticket;
import pl.biletmiejski.backend.model.TicketType;
import pl.biletmiejski.backend.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @Operation(summary = "Oferta biletowa", description = "Pokazuje dostępne do kupienia bilety")
    @GetMapping("/types")
    public ResponseEntity<List<TicketType>> getTicketTypes() {
        return ResponseEntity.ok(ticketService.getAvailableTicketTypes());
    }

    @Operation(summary = "Kup bilet", description = "Umożliwia zakup biletu dla zalogowanego pasażera")
    @PreAuthorize("hasRole('PASSENGER')")
    @PostMapping("/buy")
    public ResponseEntity<Ticket> buyTicket(@RequestBody BuyTicketRequest request) {
        return ResponseEntity.ok(ticketService.buyTicket(request));
    }

    @Operation(summary = "Moje bilety", description = "Umożliwia podgląd zakupionych biletów danego użytkownika")
    @PreAuthorize("hasRole('PASSENGER')")
    @GetMapping("/my")
    public ResponseEntity<List<Ticket>> getMyTickets() {
        return ResponseEntity.ok(ticketService.getMyTickets());
    }

    @Operation(summary = "Dodaj bilet", description = "Dodaje nowy typ biletu do oferty")
    @PreAuthorize("isAuthenticated()") // Można zmienić na hasRole('ADMIN') w przyszłości
    @PostMapping("/types")
    public ResponseEntity<TicketType> addTicketType(@RequestBody CreateTicketTypeRequest request) {
        return ResponseEntity.ok(ticketService.addTicketType(request));
    }

    @Operation(summary = "Usuń bilet", description = "Usuwa typ biletu z oferty")
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/types/{id}")
    public ResponseEntity<Void> deleteTicketType(@PathVariable Long id) {
        ticketService.deleteTicketType(id);
        return ResponseEntity.noContent().build();
    }

}
