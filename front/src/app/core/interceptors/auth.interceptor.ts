import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Auth } from '../../modules/auth/services/auth';
import { environment } from '../../../environment';
import { catchError, throwError } from 'rxjs';
import { Router } from '@angular/router';
import { ToastService } from '../../shared/ui/toast/toast.service';
import { MESSAGES } from '../messages/constants';

/**
 * Global HTTP interceptor.
 * - Prefixes API URLs
 * - Attaches JWT token
 * - Handles invalid/expired tokens
 */
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(Auth);
  const toast = inject(ToastService);
  const router = inject(Router);
  const token = auth.token();

  // Prefix API URL if the request URL is relative
  let apiReq = req;
  if (!req.url.startsWith('http')) {
    apiReq = req.clone({
      url: `${environment.apiUrl}${req.url}`,
    });
  }

  // Add Authorization header only if token is available
  if (!token) {
    return next(apiReq);
  }

  const authReq = apiReq.clone({
    setHeaders: {
      Authorization: `Bearer ${token}`,
    },
  });

  return next(authReq).pipe(
    catchError((error) => {
      const message = error.error?.message;

      // Handle invalid or expired JWT
      if (message === 'Invalid token') {
        toast.show(MESSAGES.INVALID_TOKEN, 'error');
        auth.logout();
        router.navigate(['/auth/login']);
      }

      return throwError(() => error);
    }),
  );
};
