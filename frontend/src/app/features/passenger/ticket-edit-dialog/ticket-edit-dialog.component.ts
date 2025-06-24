import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogContent, MatDialogActions } from '@angular/material/dialog';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormField, MatFormFieldModule, MatLabel } from '@angular/material/form-field';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import {MatSelect} from '@angular/material/select';
import {MatOption} from '@angular/material/core';

@Component({
  selector: 'app-ticket-edit-dialog',
  imports: [CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatDialogContent,
    ReactiveFormsModule,
    MatFormField,
    MatLabel,
    MatDialogActions,
    MatSelect,
    MatOption],
  templateUrl: './ticket-edit-dialog.component.html',
  standalone: true,
  styleUrl: './ticket-edit-dialog.scss'
})
export class TicketEditDialogComponent {
  form: FormGroup;

  constructor(
    private dialogRef: MatDialogRef<TicketEditDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private fb: FormBuilder
  ) {
    this.form = this.fb.group({
      name: [data?.name || '', Validators.required],
      category: [data?.category || '', Validators.required],
      discountType: [data?.discountType || '', Validators.required],
      price: [data?.price ?? 0, [Validators.required, Validators.min(0)]],
      durationMinutes: [data?.durationMinutes ?? null]
    });
  }

  save() {
    if (this.form.valid) {
      this.dialogRef.close(this.form.value);
    }
  }

  cancel() {
    this.dialogRef.close();
  }
}
