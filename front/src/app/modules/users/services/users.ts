import { inject, Injectable, signal } from '@angular/core';
import { User } from '../models/user.model';
import { UpdateUserRequest } from '../models/user.request';
import { tap } from 'rxjs';
import { Auth } from '../../auth/services/auth';
import { UsersApi } from './users.api';

/** Manages the authenticated user's profile and updates local state. */
@Injectable({ providedIn: 'root' })
export class Users {
  private readonly api = inject(UsersApi);
  private readonly auth = inject(Auth);

  private readonly _user = signal<User | null>(null);
  user = this._user.asReadonly();

  /** Loads the authenticated user's profile and updates the user signal. */
  loadProfile() {
    return this.api.getProfile().pipe(
      tap({
        next: (user: User) => this._user.set(user),
        error: () => console.error('Impossible de charger le profil'),
      }),
    );
  }

  /** Updates the user's profile and refreshes the local state + token if needed. */
  updateProfile(payload: UpdateUserRequest) {
    return this.api.updateProfile(payload).pipe(
      tap((updated) => {
        this._user.set(updated);
        if (updated.token) {
          this.auth.setToken(updated.token);
        }
      }),
    );
  }
}
