import {HttpErrorResponse, HttpInterceptorFn} from "@angular/common/http";
import {inject} from "@angular/core";
import {Router} from "@angular/router";
import {catchError, throwError} from "rxjs";

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);
  const token = localStorage.getItem('auth_token');

  // Skip interceptor for login/public endpoints
  if (req.url.includes('/auth/login') || req.url.includes('/public')) {
    return next(req);
  }

  // Add token if available
  if (token) {
    const authReq = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${token}`)
    });

    return next(authReq).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          // Token expired or invalid
          localStorage.removeItem('auth_token');
          router.navigate(['/login']);
        }
        return throwError(() => error);
      })
    );
  }

  // No token available
  return next(req);
};
