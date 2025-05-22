import { CanActivateFn } from '@angular/router';
import { inject } from '@angular/core';
import { AuthService } from '../auth/auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  const token = inject(AuthService).getToken();
  return !!token; // true jeśli zalogowany
};
