import {Component} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterLink} from '@angular/router';

@Component({
  standalone: true,
  selector: 'app-profile',
  imports: [CommonModule, RouterLink],
  template: `
    <h2>Mój profil</h2>
    <p>Email: {{ email }}</p>
    <a routerLink="/passenger/my">→ Moje bilety</a>
  `
})
export class ProfileComponent {
  email = 'mock@email.com'; // docelowo: pobrane z JWT lub endpointu `/me`
}
