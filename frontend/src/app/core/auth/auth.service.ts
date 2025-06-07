import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { firstValueFrom, tap } from 'rxjs';
import { jwtDecode } from 'jwt-decode';
import { Observable } from 'rxjs';
import { routes } from '../../app.routes';
import { LocalStorageService} from '../services/local-storage.service';

export interface JwtPayload {
  sub: string; // email
  iat: number;
  role?: string;
  exp: number;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private api = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient, private localStorageService: LocalStorageService) {}

  register(credentials: { email: string; password: string }): Observable<any> {
    return this.http.post(`${this.api}/register`, credentials);
  }

  // Logowanie i zapisanie tokenu w localStorage
  login(credentials: { email: string; password: string }) {
    return this.http.post<{ token: string, email: string, role: string }>(`${this.api}/login`, credentials).pipe(
      tap(res => {
        // Zapisz token i dane użytkownika w localStorage
        this.localStorageService.setItem('token', res.token);
        // Rozkodowanie tokenu JWT, aby uzyskać dane użytkownika
        const decodedToken = jwtDecode<JwtPayload>(res.token);
        console.log(decodedToken)
        this.localStorageService.setItem('email', decodedToken.sub);
        this.localStorageService.setItem('role', decodedToken.role || '');
        console.log("[INFO] Zalogowano uzytkownika: ", res.token, res.email, res.role)
      })
    );
  }

  // Metoda do wylogowania
  async logout(): Promise<void> {
    const token = this.localStorageService.getItem('token');
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
      this.localStorageService.removeItem('token');
      this.localStorageService.removeItem('email');
      this.localStorageService.removeItem('role');
      window.location.href = '/';
    }
  }

  // Pobranie tokenu z localStorage
  getToken(): string | null {
    return this.localStorageService.getItem('token');
  }

  // Pobranie roli użytkownika z localStorage
  getUserRole(): string | null {
    const role = this.localStorageService.getItem('role');
    return role ?? null;
  }

  // Pobranie emaila użytkownika z localStorage
  getUserEmail(): string | null {
    const email = this.localStorageService.getItem('email');
    return email ?? null;
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

