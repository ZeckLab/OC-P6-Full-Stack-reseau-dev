import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Auth } from '../../modules/auth/auth';
import { environment } from '../../../environment';

// Intercepts outgoing HTTP requests to attach API URL and auth token
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(Auth);
  const token = auth.token();

  // Prefix API URL if the request URL is relative
  let apiReq = req;
  if (!req.url.startsWith('http')) {
    apiReq = req.clone({
      url: `${environment.apiUrl}${req.url}`,
    });
  }

  // Add Authorization header if token exists
  if (!token) {
    return next(apiReq);
  }

  const authReq = apiReq.clone({
    setHeaders: {
      Authorization: `Bearer ${token}`,
    },
  });

  return next(authReq);
};

