import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '@services/auth.service';

export const pilotGuard: CanActivateFn = (route, state) => {
  const auth = inject(AuthService)
  const router = inject(Router)
  return auth.userDetails?.role === "CAPTAIN" || auth.userDetails?.role === 'PILOT'  ? true: router.parseUrl("/home")
};
