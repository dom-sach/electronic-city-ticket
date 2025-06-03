import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TicketService} from '../../core/services/ticket.service';import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import {TicketType, Ticket} from '../../core/models/ticket.model';

@Component({
  selector: 'app-passenger-dashboard',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatButtonModule],
  templateUrl: './dashboard.component.html',
  styleUrls: [`./dashboard.component.scss`]
})
export class DashboardComponent implements OnInit {
  tickets: TicketType[] = [];
  myTickets: any[] = [];

  constructor(private ticketService: TicketService) {}

  ngOnInit(): void {
    this.ticketService.getTicketTypes().subscribe({
      next: types => this.tickets = types,
      error: () => alert('Błąd pobierania oferty')
    });

    this.ticketService.getMyTickets().subscribe({
      next: data => this.myTickets = data,
      error: () => alert('Błąd pobierania Twoich biletów')
    });
  }

  buy(ticketTypeId: number): void {
    this.ticketService.buyTicket(ticketTypeId).subscribe({
      next: () => {
        alert('Bilet zakupiony!');
        this.ngOnInit(); // przeładuj listę biletów
      },
      error: () => alert('Błąd przy zakupie biletu')
    });
  }

  getTicketStatus(b: any): string {
    const now = new Date();
    if (b.used) return 'Skasowany';
    if (b.validUntil && new Date(b.validUntil) < now) return 'Wygasły';
    return 'Aktywny';
  }
}
