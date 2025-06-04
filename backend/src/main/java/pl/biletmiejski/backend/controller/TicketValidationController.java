package pl.biletmiejski.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.biletmiejski.backend.dto.ValidateTicketRequest;
import pl.biletmiejski.backend.dto.entities.TicketDto;
import pl.biletmiejski.backend.dto.entities.TicketMapper;
import pl.biletmiejski.backend.model.Ticket;
import pl.biletmiejski.backend.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/validate")
public class TicketValidationController {

    private final TicketService ticketService;

    // przyszła rola dla kasowników
    // @PreAuthorize("hasRole('SYSTEM')")
    @Operation(summary = "Kasowanie biletu", description = "Aktywuje bilet użytkownika")
    @PostMapping("/activate")
    public ResponseEntity<TicketDto> activateTicket(@RequestBody ValidateTicketRequest request) {
        var activatedTicket = ticketService.activateTicket(request.getCode(), request.getVehicleId());
        return ResponseEntity.ok(TicketMapper.toDto(activatedTicket));
    }

    @Operation(summary = "Walidacja biletu", description = "Sprawdza ważność biletu")
    @PreAuthorize("hasRole('TICKET_INSPECTOR')")
    @PostMapping("/check")
    public ResponseEntity<Boolean> validateTicket(@RequestBody ValidateTicketRequest request) {
        return ResponseEntity.ok(ticketService.isTicketValid(request.getCode(), request.getVehicleId()));
    }
}