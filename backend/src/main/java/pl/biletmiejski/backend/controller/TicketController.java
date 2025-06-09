package pl.biletmiejski.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.biletmiejski.backend.dto.BuyTicketRequest;
import pl.biletmiejski.backend.dto.CreateTicketTypeRequest;
import pl.biletmiejski.backend.dto.entities.TicketDto;
import pl.biletmiejski.backend.dto.entities.TicketMapper;
import pl.biletmiejski.backend.dto.entities.TicketTypeDto;
import pl.biletmiejski.backend.dto.entities.TicketTypeMapper;
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

    // get all tickets
    @Operation(summary = "Oferta biletowa", description = "Pokazuje dostępne do kupienia bilety")
    @GetMapping("/types")
    public ResponseEntity<List<TicketTypeDto>> getTicketTypes() {
        List<TicketTypeDto> dtoList = ticketService.getAvailableTicketTypes()
                .stream()
                .map(TicketTypeMapper::toDto)
                .toList();
        return ResponseEntity.ok(dtoList);
    }

    // get ticket by code
    @GetMapping("/check")
    @Operation(summary = "Sprawdzenie szczegółów biletu", description = "Zwraca szczegóły biletu na podstawie kodu biletu")
    public ResponseEntity<TicketDto> getTicketDetails(@RequestParam String code) {
        Ticket ticket = ticketService.getTicketByCode(code);  // Zwróć ticket po kodzie
        if (ticket != null) {
            return ResponseEntity.ok(TicketMapper.toDto(ticket));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    // buy a ticket
    @Operation(summary = "Kup bilet", description = "Umożliwia zakup biletu dla zalogowanego pasażera")
    @PreAuthorize("hasRole('PASSENGER')")
    @PostMapping("/buy")
    public ResponseEntity<TicketDto> buyTicket(@RequestBody BuyTicketRequest request) {
        Ticket ticket = ticketService.buyTicket(request);
        return ResponseEntity.ok(TicketMapper.toDto(ticket));
    }

    // get my tickets
    @Operation(summary = "Moje bilety", description = "Umożliwia podgląd zakupionych biletów danego użytkownika")
    @GetMapping("/my")
    public List<TicketDto> getTickets() {
        return ticketService.getMyTickets()
                .stream()
                .map(TicketMapper::toDto)
                .toList();
    }

    @Operation(summary = "Dodaj bilet", description = "Dodaje nowy typ biletu do oferty")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PostMapping("/types")
    public ResponseEntity<TicketTypeDto> addTicketType(@RequestBody CreateTicketTypeRequest request) {
        TicketType ticketType = ticketService.addTicketType(request);
        return ResponseEntity.ok(TicketTypeMapper.toDto(ticketType));
    }

    @Operation(summary = "Usuń bilet", description = "Usuwa typ biletu z oferty")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @DeleteMapping("/types/{id}")
    public ResponseEntity<Void> deleteTicketType(@PathVariable Long id) {
        ticketService.deleteTicketType(id);
        return ResponseEntity.noContent().build();
    }

}
