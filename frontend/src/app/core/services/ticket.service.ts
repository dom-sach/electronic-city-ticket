import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Ticket, TicketType } from '../models/ticket.model';

@Injectable({ providedIn: 'root' })
export class TicketService {
  private api = 'http://localhost:8080/api/tickets';

  constructor(private http: HttpClient) {}

  getTicketTypes(): Observable<TicketType[]> {
    return this.http.get<TicketType[]>(`${this.api}/types`);
  }

  getMyTickets(): Observable<Ticket[]> {
    return this.http.get<Ticket[]>(`${this.api}/my`);
  }

  buyTicket(ticketTypeId: number): Observable<Ticket> {
    return this.http.post<Ticket>(`${this.api}/buy`, { ticketTypeId });
  }

  activateTicket(code: string, vehicleId: string): Observable<any> {
    return this.http.post(`${this.api}/validate/activate`, {
      ticketCode: code,
      vehicleId: vehicleId
    });
  }

}
