import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { Auth } from  '../../modules/auth/services/auth';

// Route guard that prevents access to authentication routes if the user is already authenticated
export const guestOnlyGuard: CanActivateFn = () => {
    const auth = inject(Auth);
    const router = inject(Router);
    
    if (auth.isAuthenticated()) {
        router.navigate(['/articles/feed']);
        return false;
    }
    
    return true;
};
