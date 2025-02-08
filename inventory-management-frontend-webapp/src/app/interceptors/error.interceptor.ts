import { catchError, throwError } from 'rxjs';
import {HttpInterceptorFn} from "@angular/common/http";

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  return next(req).pipe(
    catchError(error => {
      if (error.status === 401) {
        // Handle unauthorized
        localStorage.removeItem('token');
        window.location.href = '/login';
      }
      return throwError(() => error);
    })
  );
};
