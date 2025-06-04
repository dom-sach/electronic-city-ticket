import { Component, OnInit } from '@angular/core';
import { AuthService} from '../../../core/auth/auth.service';
import { TicketService } from '../../../core/services/ticket.service';  // Dodaj serwis do pobierania biletów
import { Router } from '@angular/router';
import {MatCard, MatCardContent, MatCardHeader, MatCardTitle} from '@angular/material/card';
import {MatButton} from '@angular/material/button';
import {MatList, MatListItem} from '@angular/material/list';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  standalone: true,
  imports: [
    MatCardHeader,
    MatCard,
    MatCardContent,
    MatList,
    MatListItem,
    MatButton
  ],
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  user: any = null;
  tickets: any[] = [];  // Lista biletów użytkownika

  constructor(private authService: AuthService, private ticketService: TicketService, private router: Router) {}

  ngOnInit(): void {
    // Pobieranie roli i email użytkownika
    const role = this.authService.getUserRole();
    const email = localStorage.getItem('email');

    this.user = {
      email: email ?? '',
      role: role ?? 'Brak roli'
    };

    // Pobranie biletów użytkownika
    this.loadUserTickets();
  }

  // Pobieranie biletów użytkownika
  loadUserTickets(): void {
    this.ticketService.getUserTickets().subscribe({
      next: (data) => {
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
