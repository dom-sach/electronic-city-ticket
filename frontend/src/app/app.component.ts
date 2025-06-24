import { Component, OnInit } from '@angular/core';
import {Router, RouterOutlet} from '@angular/router';
import { HeaderComponent } from './shared/components/header/header.component';
import { InfoPopupComponent} from './shared/components/info-popup/info-popup.component';
import { LocalStorageService } from './core/services/local-storage.service';
import {MatIconModule} from '@angular/material/icon';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    HeaderComponent,
    InfoPopupComponent,
    MatIconModule,
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent implements OnInit{
  title = 'frontend';
  constructor(private localStorageService: LocalStorageService, private router: Router) {
  };

  ngOnInit(): void {
    this.clearUserData();
  }

  clearUserData(): void {
    this.localStorageService.removeItem('token');
    this.localStorageService.removeItem('email');
    this.localStorageService.removeItem('role');

    this.router.navigateByUrl('/');
  }
}
