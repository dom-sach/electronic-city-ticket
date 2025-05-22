import {Component} from '@angular/core';
import {CommonModule} from '@angular/common';
import {Router, RouterLink, RouterModule} from '@angular/router';
import {MatCardModule} from '@angular/material/card';
import {MatIcon, MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';

@Component({
  standalone: true,
  selector: 'app-profile',
  imports: [CommonModule, RouterModule, MatCardModule, MatIconModule, MatButtonModule, MatIcon],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss'],
})
export class ProfileComponent {
  user = {
    email: 'user@example.com',
    name: 'Jan Kowalski'
  };

  constructor(private router: Router) {}

  goToTickets() {
    this.router.navigateByUrl('/passenger/my');
  }
}
