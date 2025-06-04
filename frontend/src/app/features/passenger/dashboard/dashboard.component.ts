import { Component, Inject, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TicketService} from '../../../core/services/ticket.service';import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import {TicketType, Ticket} from '../../../core/models/ticket.model';
import { TicketEditDialogComponent } from '../ticket-edit-dialog/ticket-edit-dialog.component';
import { MAT_DIALOG_DATA, MatDialog, MatDialogContent, MatDialogModule, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-passenger-dashboard',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatButtonModule,  MatDialogModule, MatDialogContent],
  templateUrl: './dashboard.component.html',
  styleUrls: [`./dashboard.component.scss`]
})
export class DashboardComponent implements OnInit {
  tickets: TicketType[] = [];
  myTickets: any[] = [];

constructor(
  private ticketService: TicketService,
  private dialog: MatDialog,
) {}

  ngOnInit(): void {
    this.ticketService.getTicketTypes().subscribe({
      next: types => this.tickets = types,
      error: () => alert('Błąd pobierania oferty')
    });

    // this.ticketService.getMyTickets().subscribe({
    //   next: data => this.myTickets = data,
    //   error: () => alert('Błąd pobierania Twoich biletów')
    // });
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

  edit(ticketTypeId: number): void{
    const ticket = this.tickets.find(t => t.id === ticketTypeId);
    const dialogRef = this.dialog.open(TicketEditDialogComponent, {
      width: '500px',
      height: '800px',
      data: { ...ticket }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {

      }
    });

  }

  getTicketStatus(b: any): string {
    const now = new Date();
    if (b.used) return 'Skasowany';
    if (b.validUntil && new Date(b.validUntil) < now) return 'Wygasły';
    return 'Aktywny';
  }
}
