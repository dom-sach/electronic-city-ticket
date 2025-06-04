import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom, tap } from 'rxjs';
import { jwtDecode } from 'jwt-decode';
import { Observable } from 'rxjs';
import { routes } from '../../app.routes';

export interface JwtPayload {
  sub: string; // email
  exp: number;
  iat: number;
  role?: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private api = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) {}

  register(credentials: { email: string; password: string }): Observable<any> {
    return this.http.post(`${this.api}/register`, credentials);
  }

  // Logowanie i zapisanie tokenu w localStorage
  login(credentials: { email: string; password: string }) {
    return this.http.post<{ token: string }>(`${this.api}/login`, credentials).pipe(
      tap(res => {
        // Zapisz token w localStorage
        localStorage.setItem('token', res.token);
      })
    );
  }

  // Metoda do wylogowania
  async logout(): Promise<void> {
    const token = localStorage.getItem('token');
    if (!token) {
      window.location.href = '/';
      return;
    }

    try {
      await firstValueFrom(
        this.http.post(`${this.api}/logout`, {}, {
          headers: {
            Authorization: `Bearer ${token}`
          }
        })
      );
    } catch (err) {
      console.warn('Wylogowywanie nie powiodło się lub token był już nieważny:', err);
    } finally {
      localStorage.removeItem('token');
      window.location.href = '/';
    }
  }

  // Pobranie tokenu z localStorage
  getToken(): string | null {
    if (typeof window === 'undefined') return null;
    return localStorage.getItem('token');
  }

  // Pobranie roli użytkownika z tokenu
  getUserRole(): string | null {
    const token = this.getToken();
    if (!token) return null;
    try {
      const decoded = jwtDecode<JwtPayload>(token);
      return decoded.role ?? null;
    } catch {
      return null;
    }
  }

  // Dodanie tokenu do nagłówków w żądaniu
  getAuthenticatedHttpOptions() {
    const token = this.getToken();
    if (!token) {
      return {};
    }
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return { headers };
  }
}

