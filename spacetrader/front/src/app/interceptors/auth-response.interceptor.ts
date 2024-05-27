import { HttpErrorResponse, HttpInterceptorFn, HttpStatusCode } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '@services/auth.service';
import { catchError, finalize, throwError } from 'rxjs';

export const authResponseInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService)
  const router = inject(Router)
  return next(req).pipe(
    catchError((err: HttpErrorResponse)=>{
      if(err.status === HttpStatusCode.Unauthorized || err.status === HttpStatusCode.Forbidden){
        auth.logout().pipe(finalize(()=>setTimeout(() => router.navigate(["/login"]), 1500))).subscribe()
      }
      return throwError(()=>err)
    })
  );
};
