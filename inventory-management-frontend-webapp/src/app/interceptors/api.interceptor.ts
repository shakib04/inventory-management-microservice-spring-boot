import { HttpInterceptorFn } from '@angular/common/http';
import { environment } from '../../environments/environment.development';

export const apiInterceptor: HttpInterceptorFn = (req, next) => {
  // Only modify requests that don't already have an absolute URL
  if (!req.url.startsWith('http')) {
    const apiReq = req.clone({
      url: `${environment.apiBaseUrl}/${req.url.replace(/^\//, '')}`
    });
    return next(apiReq);
  }
  return next(req);
};
