import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LoginRequest, LoginResponse, RegisterRequest } from './models/auth.model';

@Injectable({ providedIn: 'root' })
export class AuthApi {
  private readonly http = inject(HttpClient);

  private readonly authUrl = '/auth';

  login(data: LoginRequest) {
    return this.http.post<LoginResponse>(`${this.authUrl}/login`, data);
  }

  register(data: RegisterRequest) {
    return this.http.post<void>(`${this.authUrl}/register`, data);
  }
}