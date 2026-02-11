import { inject } from '@angular/core';
import { AuthService } from './services/auth.service';
import { CanActivateFn, Router } from "@angular/router";

export const roleGuard: CanActivateFn = (route) => {
    const authService = inject(AuthService);
    const router = inject(Router);

    const expectedRoles = route.data['roles'] as string[];
    const userRole = authService.getRole();

    if(userRole && expectedRoles.includes(userRole)) {
        return true;
    }

    return router.createUrlTree(['/login']);
}