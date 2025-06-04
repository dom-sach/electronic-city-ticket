import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Ticket, TicketType } from '../models/ticket.model';

@Injectable({ providedIn: 'root' })
export class ValidateService {
  private api = 'http://localhost:8080/api/validate';

  constructor(private http: HttpClient) {}

  checkTicket(code: string, vehicleId: string): Observable<any>{
    return this.http.post(`${this.api}/check`, {
      ticketCode: code,
      vehicleId: vehicleId
    });

  }
}
