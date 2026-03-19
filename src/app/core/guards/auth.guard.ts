import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

/**
 * authGuard — protects routes that require authentication.
 * Redirects to /login if user is not authenticated.
 *
 * WHY functional guard? Angular 17+ recommends functional guards
 * over class-based CanActivate — less boilerplate, same power.
 */
export const authGuard: CanActivateFn = () => {
  const auth   = inject(AuthService);
  const router = inject(Router);

  if (auth.isAuthenticated()) {
    return true;
  }

  // Redirect to login, preserving intended destination
  return router.parseUrl('/login');
};

/**
 * guestGuard — prevents authenticated users from visiting auth pages.
 * Redirects to /dashboard if already logged in.
 */
export const guestGuard: CanActivateFn = () => {
  const auth   = inject(AuthService);
  const router = inject(Router);

  if (!auth.isAuthenticated()) {
    return true;
  }

  return router.parseUrl('/dashboard');
};
