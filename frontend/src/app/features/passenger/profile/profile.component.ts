import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../../core/auth/auth.service';
import { TicketService } from '../../../core/services/ticket.service';  // Dodaj serwis do pobierania biletów
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';  // Zaimportuj cały MatCardModule
import { MatButtonModule } from '@angular/material/button';  // MatButtonModule
import { MatListModule } from '@angular/material/list';  // Jeżeli używasz listy
import { NgIf, NgForOf } from '@angular/common';  // Dodanie NgIf i NgFor dla Angulara
import { DatePipe } from '@angular/common';  // Jeżeli używasz DatePipe

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
    DatePipe
  ],
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  user: any = null;
  tickets: any[] = [];  // Lista biletów użytkownika

  constructor(
    private authService: AuthService,
    private ticketService: TicketService,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Pobranie roli i email użytkownika
    const role = this.authService.getUserRole();
    const email = this.authService.getUserEmail();

    this.user = {
      email: email ?? 'Brak emaila',
      role: role ?? 'Brak roli'
    };

    // Debugging logs
    console.log('Rola:', role);
    console.log('Email:', email);

    // Pobranie biletów użytkownika
    this.loadUserTickets();
  }

  // Pobieranie biletów użytkownika
  loadUserTickets(): void {
    this.ticketService.getUserTickets().subscribe({
      next: (data) => {
        console.log('Bilety użytkownika:', data);  // Logowanie biletów
        this.tickets = data;
      },
      error: (error) => {
        console.error('Błąd ładowania biletów:', error);
      },
      complete: () => {
        console.log('Ładowanie biletów zakończone.');
      }
    });
  }

  goToTickets() {
    this.router.navigateByUrl('/passenger');
  }
}
