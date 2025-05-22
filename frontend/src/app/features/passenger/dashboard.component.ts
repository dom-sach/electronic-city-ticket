import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TicketService} from '../../core/services/ticket.service';import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import {TicketType, Ticket} from '../../core/models/ticket.model';

@Component({
  selector: 'app-passenger-dashboard',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatButtonModule],
  template: `
    <h2 style="text-align:center;">Dostępna oferta biletowa</h2>

    <div class="ticket-list">
      <mat-card *ngFor="let ticket of tickets" class="ticket-card">
        <mat-card-title>{{ ticket.name }}</mat-card-title>
        <mat-card-content>
          <p>Typ: {{ ticket.category }}</p>
          <p>Zniżka: {{ ticket.discountType }}</p>
          <p>Cena: {{ ticket.price | currency:'PLN':'symbol':'1.2-2' }}</p>
          <p *ngIf="ticket.durationMinutes">Czas trwania: {{ ticket.durationMinutes }} minut</p>
        </mat-card-content>
        <mat-card-actions>
          <button mat-raised-button color="primary" (click)="buy(ticket.id)">Kup</button>
        </mat-card-actions>
      </mat-card>
    </div>

    <h2 style="text-align:center; margin-top: 2rem;">Moje bilety</h2>

    <div class="ticket-list">
      <mat-card *ngFor="let b of myTickets" class="ticket-card">
        <mat-card-title>{{ b.ticketType.name }}</mat-card-title>
        <mat-card-content>
          <p>Data zakupu: {{ b.purchaseDate | date:'short' }}</p>
          <p *ngIf="b.activationDate">Skasowany: {{ b.activationDate | date:'short' }}</p>
          <p *ngIf="b.validUntil">Ważny do: {{ b.validUntil | date:'short' }}</p>
          <p *ngIf="b.activatedIn">Pojazd: {{ b.activatedIn.vehicleId }}</p>
          <p>Status: {{ getTicketStatus(b) }}</p>
        </mat-card-content>
      </mat-card>
    </div>
  `,
  styles: [`
    .ticket-list {
      display: grid;
      grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
      gap: 1.5rem;
      padding: 2rem;
    }

    .ticket-card {
      padding: 1rem;
    }
  `]
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
