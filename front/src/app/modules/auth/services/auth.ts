import { inject, Injectable, signal, computed } from '@angular/core';
import { AuthApi } from './auth.api';
import { LoginRequest, RegisterRequest } from '../models/auth.model';
import { tap } from 'rxjs';
import { Router } from '@angular/router';

/**
 * Central authentication facade.
 * Manages JWT token, session persistence and navigation after auth actions.
 */
@Injectable({
  providedIn: 'root',
})
export class Auth {
  private readonly api = inject(AuthApi);
  private readonly router = inject(Router);

  // Token stored as a signal to enable reactive UI updates across the app.
  private readonly _token = signal<string | null>(null);

  token = this._token.asReadonly();
  isAuthenticated = computed(() => !!this._token());

  constructor() {
    this.restoreSession();
  }

  /** Performs login, stores JWT and redirects to feed. */
  login(data: LoginRequest) {
    return this.api.login(data).pipe(
      tap((res) => {
        this._token.set(res.token);
        localStorage.setItem('token', res.token);
        this.router.navigateByUrl('/articles/feed');
      }),
    );
  }

  /** Restores JWT from localStorage on app startup. */
  register(data: RegisterRequest) {
    return this.api.register(data).pipe(
      tap(() => {
        this.router.navigateByUrl('/auth/login');
      }),
    );
  }

  logout() {
    this.clearSession();
    this.router.navigateByUrl('/');
  }

  isLoggedIn(): boolean {
    return this.isAuthenticated();
  }

  getToken(): string | null {
    return this._token();
  }

  setToken(newToken: string) {
    this._token.set(newToken);
    localStorage.setItem('token', newToken);
  }

  // Restore token from localStorage on app startup.
  private restoreSession() {
    const storedToken = localStorage.getItem('token');
    if (storedToken) this._token.set(storedToken);
  }

  private clearSession() {
    localStorage.removeItem('token');
    this._token.set(null);
  }
}
