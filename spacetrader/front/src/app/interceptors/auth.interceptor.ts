import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '@services/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService)
  const token = auth.userDetails?.token
  if(token === ""){
    return next(req)
  }
  req = req.clone({
    setHeaders: {
      Authorization: "Bearer " + token
    }
  })
  return next(req)
};
