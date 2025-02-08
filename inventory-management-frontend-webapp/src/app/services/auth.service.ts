import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {tap} from "rxjs/operators";
import {JwtService} from "./jwt.service";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private authUrl = '/user-service/api/auth/authenticate';
  private registerUrl = '/user-service/api/auth/register';
  private readonly TOKEN_KEY = 'auth_token';
  private userData: any = null;
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(this.hasValidToken());

  constructor(private http: HttpClient,
              private jwtService: JwtService) {
    // Load user data from token if exists
    const token = this.getToken();
    if (token) {
      this.userData = this.jwtService.decodeToken(token);
    }
  }

  authenticate(credentials: { username: string; password: string }): Observable<any> {
    return this.http.post<any>(this.authUrl, credentials)
      .pipe(
        tap(response => {
          const token = response.token;
          localStorage.setItem(this.TOKEN_KEY, token);
          this.userData = this.jwtService.decodeToken(token);
          localStorage.setItem("role", <string>this.getUserRole());

          this.isAuthenticatedSubject.next(true);
        })
      );
  }

  register(credentials: { username: string; password: string, emailId: string }): Observable<any> {
    return this.http.post<any>(this.registerUrl, credentials)
      .pipe(
        tap(response => {
          localStorage.setItem(this.TOKEN_KEY, response.token);
          this.isAuthenticatedSubject.next(true);
        })
      );
  }

  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    this.isAuthenticatedSubject.next(false);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  isAuthenticated(): Observable<boolean> {
    return this.isAuthenticatedSubject.asObservable();
  }

  private hasValidToken(): boolean {
    const token = this.getToken();
    // Add token validation logic if needed
    return !!token;
  }


  getUserData() {
    return this.userData;
  }

  // Get specific claims from token
  getUserId(): string | null {
    return this.userData?.sub || null;
  }

  getUserRole(): string | null {
    return this.userData?.roles || null;
  }

  getUsername(): string | null {
    return this.userData?.username || null;
  }
}
