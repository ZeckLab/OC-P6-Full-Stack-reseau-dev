import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../models/user.model';
import { UpdateUserRequest } from '../models/user.request';
import { UpdatedUser } from '../models/user.response';

@Injectable({ providedIn: 'root' })
export class UsersApi {
  private readonly http = inject(HttpClient);
  private readonly userUrl = '/users';

  getProfile(): Observable<User> {
    return this.http.get<User>(`${this.userUrl}/me`);
  }
  
  updateProfile(payload: UpdateUserRequest): Observable<UpdatedUser> {
    return this.http.patch<UpdatedUser>(`${this.userUrl}/me`, payload);
  }
}
