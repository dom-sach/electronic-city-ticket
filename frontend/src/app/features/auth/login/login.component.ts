import { Component } from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { AuthService } from '../../../core/auth/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent {
  form!: FormGroup;

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.form.invalid) return;

    const credentials = this.form.getRawValue() as { email: string; password: string };

    this.authService.login(credentials).subscribe({
      next: () => this.router.navigateByUrl('/'),
      // next: () => {
      //
      //   //const role = this.authService.getUserRole();
      //
      //   // switch (role) {
      //   //   case 'PASSENGER':
      //   //     this.router.navigateByUrl('/passenger');
      //   //     break;
      //   //   case 'TICKET_INSPECTOR':
      //   //     this.router.navigateByUrl('/inspector');
      //   //     break;
      //   //   case 'ADMINISTRATOR':
      //   //     this.router.navigateByUrl('/admin');
      //   //     break;
      //   //   default:
      //   //     alert('Nieznana rola użytkownika');
      //   // }
      // },
      error: () => {
        alert('Nieprawidłowy email lub hasło.');
      }
    });
  }
}
