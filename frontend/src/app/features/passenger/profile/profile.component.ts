import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../core/auth/auth.service';
import { TicketService } from '../../../core/services/ticket.service';  // Dodaj serwis do pobierania biletów
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';  // Zaimportuj cały MatCardModule
import { MatButtonModule } from '@angular/material/button';  // MatButtonModule
import { MatListModule } from '@angular/material/list';  // Jeżeli używasz listy
import {NgIf, NgForOf, NgClass} from '@angular/common';  // Dodanie NgIf i NgFor dla Angulara
import { DatePipe } from '@angular/common';  // Jeżeli używasz DatePipe
import {InfoPopupComponent} from '../../../shared/components/info-popup/info-popup.component';
import {MatDialog} from '@angular/material/dialog';
import {Ticket} from '../../../core/models/ticket.model';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  standalone: true,
  imports: [
    MatCardModule,
    MatButtonModule,
    MatListModule,
    NgIf,
    NgForOf,
    DatePipe,
    NgClass,
  ],
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  user: any = null; // currently logged-in user
  tickets: any[] = []; // user's tickets

  constructor(
    private authService: AuthService,
    private ticketService: TicketService,
    private router: Router,
    private dialog: MatDialog,
  ) {}

  ngOnInit(): void {
    // Getting user's role and email
    const role = this.authService.getUserRole();
    const email = this.authService.getUserEmail();

    this.user = {
      email: email ?? 'E-mail niedostępny',
      role: role ?? 'Rola użytkownika niedostępna'
    };

    // Debugging logs
    console.log('Rola:', role);
    console.log('Email:', email);

    // Get user's tickets
    this.loadUserTickets();
  }


  // user tickets
  loadUserTickets(): void {
    this.ticketService.getUserTickets().subscribe({
      next: (data) => {
        console.log("[profile.component] Fetched tickets: ", data);  // Logowanie biletów
        if (data && Array.isArray(data)) {
          this.tickets = data;
        } else {
          this.tickets = [];
        }
      },
      error: (error) => {
        console.error('[profile.component] Ticket getting error: ', error);
        this.openErrorDialog("Nie udało się załadować twoich biletów. Spróbuj odświeżyć stronę.");
      },
      complete: () => {
        console.log('[profile.component] Getting tickets finished successfully.');
      }
    });
  }

  // load ticket offer page
  goToTickets() {
    this.router.navigateByUrl('/passenger');
  }

  activateTicket(ticket: any): void {
    console.log("[profile.component] Activate ticket with code: " + ticket.code);
    if (!ticket.activationDate && !ticket.used && ticket.ticketTypeName !== 'PERIOD') {
      this.ticketService.activateTicket(ticket.code, '146_Gaj')
        .subscribe({
          next: () => {
            ticket.activattionDate = new Date().toISOString();
            ticket.used = true;
            ticket.error = false;
          },
          error: () => {
            ticket.error = true;
            this.openErrorDialog('Nie udało się skasować biletu, spróbuj ponownie za chwilę.\nPrzepraszamy za niedogodność.');
            console.log("[profile.component] Ticket activation error");
          }
        });
    } else {
      console.warn("[profile.component] Ticket cannot be activated");
      this.openErrorDialog('Ten bilet nie może być aktywowany')
    }
  }

  isTicketExpired(ticket: any): boolean {
    const currentDate = new Date();
    // @ts-ignore
    if (ticket == null || ticket.validUntil==null) {
      return false
    }
    const validUntilDate = new Date(ticket.validUntil);
    return validUntilDate < currentDate;
  }

  openErrorDialog(message: string): void {
    const dialogRef = this.dialog.open(InfoPopupComponent, {
      data: {
        message: message
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log("[profile.component] Popup closed");
    })
  }



}
