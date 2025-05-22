import {Component, OnInit} from '@angular/core';
import { CommonModule } from '@angular/common';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import { Router } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { AuthService } from '../../core/auth/auth.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, MatFormFieldModule, MatInputModule, MatButtonModule],
  template: `
    <h2>Rejestracja</h2>
    <form [formGroup]="form" (ngSubmit)="onSubmit()" class="auth-form">
      <mat-form-field appearance="fill">
        <mat-label>Email</mat-label>
        <input matInput type="email" formControlName="email" required>
      </mat-form-field>

      <mat-form-field appearance="fill">
        <mat-label>Hasło</mat-label>
        <input matInput type="password" formControlName="password" required>
      </mat-form-field>

      <button mat-raised-button color="primary" type="submit" [disabled]="form.invalid">
        Zarejestruj
      </button>
    </form>
  `,
  styles: [`
    .auth-form {
      max-width: 400px;
      margin: 2rem auto;
      display: flex;
      flex-direction: column;
      gap: 20px;
    }
  `]
})
export class RegisterComponent implements OnInit {
  form!: FormGroup;

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {}


  ngOnInit(): void {
    this.form = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }
  onSubmit(): void {
    const credentials = this.form.getRawValue() as { email: string; password: string };

    this.authService.register(credentials).subscribe({
      next: () => {
        // automatyczne logowanie po rejestracji
        this.authService.login(credentials).subscribe({
          next: () => this.router.navigateByUrl('/'),
          error: () => alert('Logowanie po rejestracji nie powiodło się')
        });
      },
      error: () => alert('Rejestracja nie powiodła się')
    });
  }
}
