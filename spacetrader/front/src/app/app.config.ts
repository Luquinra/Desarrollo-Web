import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';
import { routes } from './app.routes';
import { provideClientHydration } from '@angular/platform-browser';
import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { provideAnimations } from '@angular/platform-browser/animations';
import { JWT_OPTIONS, JwtHelperService } from '@auth0/angular-jwt';
import { authInterceptor } from '@interceptors/auth.interceptor';
import { authResponseInterceptor } from '@interceptors/auth-response.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [provideRouter(routes), provideClientHydration(), provideHttpClient(withFetch(),
    withInterceptors([authInterceptor, authResponseInterceptor])), provideAnimations(),
    { provide: JWT_OPTIONS, useValue: JWT_OPTIONS }, JwtHelperService
  ]
};
