import { DOCUMENT } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';
import { environment } from '@environments/environment';
import { UserDetails } from '@model/auth/user-details';
import { LoginRequest } from '@model/request/login-request';
import { LoginResponse } from '@model/response/login-response';
import { BehaviorSubject, Observable, finalize, map, of, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private static readonly INTERNAL_STORAGE_NAME = "currentUSER"

  private userData: BehaviorSubject<UserDetails | null>;

  constructor(private http: HttpClient, private jwt: JwtHelperService, @Inject(DOCUMENT) private document: Document) {
    const localStorage = document.defaultView?.localStorage
    if (localStorage) {
      let user = localStorage.getItem(AuthService.INTERNAL_STORAGE_NAME);
      if (user !== null) {
        this.userData = new BehaviorSubject<UserDetails | null>(JSON.parse(user));
        return
      }
    }
    this.userData = new BehaviorSubject<UserDetails | null>(null);
  }

  public get userDetails() {
    return this.userData.value
  }

  public get isLogged() {
    return this.userData.value !== null
  }

  login(request: LoginRequest): Observable<UserDetails> {
    return this.http.post<LoginResponse>(`${environment.apiUrl}/auth/login`, request)
      .pipe(map((res: LoginResponse) => this.storeSessionInfo(res)))
  }

  logout(): Observable<any> {
    return of({})
      .pipe(finalize(() => {
        localStorage.removeItem(AuthService.INTERNAL_STORAGE_NAME)
        this.userData.next(null);
      }
      ))
  }

  private storeSessionInfo(res:LoginResponse): UserDetails {
    const details = this.extractClaims(res)
    localStorage.setItem(AuthService.INTERNAL_STORAGE_NAME, JSON.stringify(details));
    this.userData.next(details);
    return details
  }

  private extractClaims(response: LoginResponse): UserDetails {
    const decoded = this.jwt.decodeToken(response.token)
    return {
      username: decoded['sub'],
      role: decoded['ROLE'],
      token: response.token
    }
  }
}
