import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';
import { AuthService } from '../../../core/auth/auth.service';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterModule, MatToolbarModule, MatButtonModule, MatMenuModule],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent {
  constructor(private authService: AuthService, private router: Router) {}

  // Sprawdzanie, czy użytkownik jest zalogowany
  isLoggedIn(): boolean {
    return !!this.authService.getToken();
  }

  isPassenger(): boolean {
    return this.authService.getUserRole()==='PASSENGER';
  }

  isInspector(): boolean {
    return this.authService.getUserRole()==='TICKET_INSPECTOR';
  }

  isAdmin(): boolean {
    return this.authService.getUserRole()==='ADMINISTRATOR';
  }

  // Sprawdzanie roli użytkownika
  hasRole(role: string): boolean {
    return this.authService.getUserRole() === role;
  }

  // Wylogowywanie użytkownika
  logout(): void {
    this.authService.logout();
    this.router.navigateByUrl('/');
  }

  // Pobieranie roli użytkownika
  getUserRole(): string | null {
    return this.authService.getUserRole();
  }
}

