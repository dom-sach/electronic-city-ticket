import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Vehicle} from '../models/ticket.model';

@Injectable({
  providedIn: 'root'
})
export class VehicleService {

  private api = 'http://localhost:8080/api/vehicles'; // Backendowy endpoint

  constructor(private http: HttpClient) {}

  getVehicles(): Observable<Vehicle[]> {
    return this.http.get<Vehicle[]>(this.api);
  }
}
