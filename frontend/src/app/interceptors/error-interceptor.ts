import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {

  const router = inject(Router);

  return next(req).pipe(
    catchError(error => {

      if (error.status === 401) {
        localStorage.removeItem('token');
        router.navigate(['/login']);
      }

      if (error.status === 403) {
        router.navigate(['/error'], { queryParams: { reason: 'forbidden' } });
      }

      if (error.status === 404) {
        router.navigate(['/error'], { queryParams: { reason: 'not-found' } });
      }

      if (error.status >= 500 && error.status < 600) {
        router.navigate(['/error'], { queryParams: { reason: 'server-error' } });
      }

      return throwError(() => error);
    })
  );
};
