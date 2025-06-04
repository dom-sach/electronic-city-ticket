import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {ReactiveFormsModule, FormBuilder, Validators, FormGroup} from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { TicketService } from '../../../core/services/ticket.service';

@Component({
  selector: 'app-ticket-activate',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule
  ],
  templateUrl: `./ticket-activate.component.html`,
  styleUrls: [`./ticket-activate.component.scss`]
})
export class TicketActivateComponent implements OnInit {
  form!: FormGroup;

  constructor(private fb: FormBuilder, private ticketService: TicketService) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      ticketCode: ['', Validators.required],
      vehicleId: ['', Validators.required],
    });
  }

  onSubmit() {
    const ticketCode = this.form.get('ticketCode')?.value;
    const vehicleId = this.form.get('vehicleId')?.value;

    if (!ticketCode || !vehicleId) {
      alert('Uzupełnij wszystkie pola');
      return;
    }

    this.ticketService.activateTicket(ticketCode, vehicleId).subscribe({
      next: () => alert('Bilet został skasowany!'),
      error: () => alert('Błąd podczas kasowania biletu')
    });
  }
}
