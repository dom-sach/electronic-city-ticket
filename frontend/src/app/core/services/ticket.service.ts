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

  getUserTickets(): Observable<any[]> {
    const headers = this.authService.getAuthenticatedHttpOptions();  // Pobranie tokenu
    return this.http.get<any[]>(`${this.api}/tickets/my`, headers);
  }

  getTicketInfo(ticketCode: string): Observable<any> {
    return this.http.get<any>(`${this.api}/tickets/check?code=${ticketCode}`);
  }

  createTicketType(data: any): Observable<any> {
    return this.http.post(`${this.api}/tickets/types`, data);
  }

  buyTicket(ticketTypeId: number): Observable<Ticket> {
    return this.http.post<Ticket>(`${this.api}/tickets/buy`, { ticketTypeId });
  }

  activateTicket(code: string, vehicleId: string | null): Observable<any> {
    console.log("[ticket.service] Received data for activation: ", code, vehicleId)
    return this.http.post(`${this.api}/validate/activate`, {
      code: code,
      vehicleId: vehicleId
    });
  }
}
