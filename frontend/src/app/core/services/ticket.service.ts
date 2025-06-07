import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Ticket, TicketType } from '../models/ticket.model';
import {AuthService} from '../auth/auth.service';

@Injectable({ providedIn: 'root' })
export class TicketService {
  private api = 'http://localhost:8080/api';

  constructor(private http: HttpClient, private authService: AuthService) {}

  getTicketTypes(): Observable<TicketType[]> {
    return this.http.get<TicketType[]>(`${this.api}/tickets/types`);
  }

  // getMyTickets(): Observable<Ticket[]> {
  //   return this.http.get<Ticket[]>(`${this.api}/my`);
  // }

  getUserTickets(): Observable<any[]> {
    const headers = this.authService.getAuthenticatedHttpOptions();  // Pobranie tokenu
    return this.http.get<any[]>(`${this.api}/tickets/my`, headers);
  }

  loadUserTickets() {
    this.getUserTickets().subscribe({
      next: (data) => {
        console.log('Bilety użytkownika:', data);
        // Przypisz dane do zmiennej w komponencie
      },
      error: (err) => {
        console.error('Błąd ładowania biletów:', err);
      },
      complete: () => {
        console.log('Ładowanie biletów zakończone.');
      }
    });
  }

  buyTicket(ticketTypeId: number): Observable<Ticket> {
    return this.http.post<Ticket>(`${this.api}/tickets/buy`, { ticketTypeId });
  }

  activateTicket(code: string, vehicleId: string): Observable<any> {
    console.log("[ticket.service] Received data for activation: ", code, vehicleId)
    return this.http.post(`${this.api}/validate/activate`, {
      code: code,
      vehicleId: vehicleId
    });
  }
}
