// src/app/app.routes.ts
import { Routes } from '@angular/router';
import {authGuard} from './core/guards/auth.guard';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./features/home/home.component').then(m => m.HomeComponent)
  },
  {
    path: 'passenger',
    canActivate: [authGuard],
    loadComponent: () => import('./features/passenger/dashboard.component').then(m => m.DashboardComponent),
  },
  {
    path: 'inspector',
    canActivate: [authGuard],
    loadComponent: () => import('./features/inspector/dashboard.component').then(m => m.DashboardComponent),
  },
  {
    path: 'admin',
    canActivate: [authGuard],
    loadComponent: () => import('./features/admin/dashboard.component').then(m => m.DashboardComponent),
  },
  {
    path: 'passenger/activate',
    canActivate: [authGuard],
    loadComponent: () => import('./features/passenger/ticket-activate.component').then(m => m.TicketActivateComponent)
  },
  {
    path: 'profile',
    canActivate: [authGuard],
    loadComponent: () => import('./features/passenger/profile.component').then(m => m.ProfileComponent)
  },
  {
    path: 'register',
    loadComponent: () => import('./features/auth/register.component').then(m => m.RegisterComponent)
  },
  {
    path: 'login',
    loadComponent: () => import('./features/auth/login.component').then(m => m.LoginComponent)
  }
];
