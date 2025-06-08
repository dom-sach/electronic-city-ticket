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
import {Ticket, Vehicle} from '../../../core/models/ticket.model';
import {MatInput} from '@angular/material/input';
import {VehicleService} from '../../../core/services/vehicle.service';
import {MatFormField, MatLabel} from '@angular/material/form-field';
import {MatOption} from '@angular/material/core';
import {MatSelect} from '@angular/material/select';
import {FormsModule} from '@angular/forms';
import {VehicleSelectionDialogComponent} from '../../../shared/components/vehicle-selection-dialog.component.ts/vehicle-selection-dialog.component';

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
    MatFormField,
    MatLabel,
    MatInput,
    MatOption,
    MatSelect,
    FormsModule,
    VehicleSelectionDialogComponent
  ],
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  user: any = null; // currently logged-in user
  tickets: any[] = []; // user's tickets
  vehicles: Vehicle[] = [];
  filteredVehicles: Vehicle[] = [];
  selectedVehicleId: string | null = null;
  searchVehicle: string = '';

  constructor(
    private authService: AuthService,
    private ticketService: TicketService,
    private vehicleService: VehicleService,
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

    // Load all vehicles
    this.loadVehicles();
  }


  loadVehicles(): void {
    this.vehicleService.getVehicles().subscribe({
      next: (data) => {
        this.vehicles = data;
        this.filteredVehicles = data;
      },
      error: (error) => {
        console.error('[profile.component] Couldn\'t load vehicles ', error);
      },
      complete: () => {
        console.log('[profile.component] Loading vehicles successful.');
      }
    });
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

  goToUsersList() {
    this.router.navigateByUrl('/admin/users')
  }

  // Filter vehicles
  filterVehicles(): void {
    if (!this.searchVehicle) {
      this.filteredVehicles = this.vehicles;
    } else {
      this.filteredVehicles = this.vehicles.filter(vehicle =>
        vehicle.vehicleId.toLowerCase().includes(this.searchVehicle.toLowerCase())
      );
    }
  }

  openVehicleSelectionDialog(ticket: any): void {
    const dialogRef = this.dialog.open(VehicleSelectionDialogComponent, {
      width: '400px',
      data: { ticket: ticket } // Przekazujemy bilet do dialogu
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.activateTicket(ticket, result.vehicleId);  // Aktywujemy bilet
      }
    });
  }

  activateTicket(ticket: any, vehicleId: string): void {
    if (!ticket.activationDate && !ticket.used && ticket.ticketTypeName !== 'PERIOD') {
      this.ticketService.activateTicket(ticket.code, vehicleId).subscribe({
        next: () => {
          ticket.activationDate = new Date().toISOString();
          ticket.used = true;
          ticket.error = false;
          ticket.vehicleId = vehicleId;
        },
        error: () => {
          ticket.error = true;
          this.openErrorDialog('Nie udało się skasować biletu, spróbuj ponownie za chwilę.\nPrzepraszamy za niedogodność.');
        }
      });
    } else {
      this.openErrorDialog('Ten bilet nie może być aktywowany');
    }
  }

  isTicketExpired(ticket: any): boolean {
    const currentDate = new Date();
    // @ts-ignore
    if (ticket == null || ticket.validUntil == null) {
      return false;
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
