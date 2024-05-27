import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '@services/auth.service';

export const traderGuard: CanActivateFn = (route, state) => {
  const auth = inject(AuthService)
  const router = inject(Router)
  return auth.userDetails?.role === "CAPTAIN" || auth.userDetails?.role === 'TRADER'  ? true: router.parseUrl("/home")
};
