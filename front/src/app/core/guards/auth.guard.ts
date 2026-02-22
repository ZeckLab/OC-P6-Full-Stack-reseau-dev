import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { Auth } from '../../modules/auth/auth';

// Route guard that prevents access to protected routes if the user is not authenticated
export const authGuard: CanActivateFn = () => {
  const auth = inject(Auth);
  const router = inject(Router);

  // Allow navigation only if the user is authenticated
  if (auth.isAuthenticated()) {
    return true;
  }

  // Redirect unauthenticated users to the login page
  return router.parseUrl('/auth/login');
};
