import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import {MatCard, MatCardContent, MatCardHeader, MatCardSubtitle, MatCardTitle} from '@angular/material/card';
import { ValidateService } from '../../core/services/validate.service';
import {TicketService} from '../../core/services/ticket.service';
import {MatSnackBar} from '@angular/material/snack-bar';

@Component({
  selector: 'app-inspector-dashboard',
  standalone: true,
  imports: [MatCard, FormsModule, CommonModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule, MatCardContent, MatCardTitle, MatCardHeader, MatCardSubtitle],
  templateUrl: `./dashboard.component.html`,
  styleUrls: ['./dashboard.component.scss']
})

export class DashboardComponent {
  ticketCode: string = '';
  vehicleId: string = '';
  ticketInfo: any = null;
  ticketValid: boolean | null = null;

  constructor(
    private ticketService: TicketService,
    private validateService: ValidateService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {}

  showTicketInfo() {
    if (this.ticketCode) {
      this.ticketService.getTicketInfo(this.ticketCode).subscribe({
        next: (data) => {
          this.ticketInfo = data;
        },
        error: () => {
          this.snackBar.open('Nie udało się pobrać danych biletu, spróbuj ponownie.', 'OK', { duration: 3000 });
        }
      });
    }
  }

  validateTicket() {
    if (this.ticketCode && this.vehicleId) {
      this.validateService.checkTicket(this.ticketCode, this.vehicleId).subscribe({
        next: (data) => {
          this.ticketValid = data;
        },
        error: () => {
          this.snackBar.open('Nie udało się skontrolować biletu, spróbuj ponownie.', 'OK', { duration: 3000 });
        }
      });
    }
  }

  // Form validation
  isFormValid(): "" | string {
    return this.ticketCode && this.vehicleId;
  }

  isTicketCodeValid(): boolean {
    return this.ticketCode !== '';
  }

}
