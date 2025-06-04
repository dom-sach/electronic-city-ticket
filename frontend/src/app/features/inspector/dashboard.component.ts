import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatCard } from '@angular/material/card';
import { ValidateService } from '../../core/services/validate.service';

@Component({
  selector: 'app-inspector-dashboard',
  standalone: true,
  imports: [MatCard, FormsModule , CommonModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule],
  templateUrl: `./dashboard.component.html`,
  styleUrls: ['./dashboard.component.scss']
})

export class DashboardComponent {
  constructor(private validateService: ValidateService) {}

  ticketCode: string = '';
  vehicleId: string = '';
  result: { valid: boolean; message: string } | null = null;

  onValidate() {
    const validCodes = ['ABC123', 'XYZ789'];
    const isValid = validCodes.includes(this.ticketCode.trim().toUpperCase());

    this.validateService.checkTicket(this.ticketCode, this.vehicleId).subscribe({
      next: () => alert('Bilet został skasowany!'),
      error: () => alert('Błąd podczas kasowania biletu')
    });

  }

}
