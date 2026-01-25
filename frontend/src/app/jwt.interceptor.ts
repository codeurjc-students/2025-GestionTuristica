import { AuthService } from './services/auth.service';
import { HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Router } from '@angular/router';
import { catchError, Observable, throwError } from "rxjs";

@Injectable()
export class JwtInterceptor implements HttpInterceptor {

    constructor(private readonly authService: AuthService, private readonly router: Router){}

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>>{

        const token = localStorage.getItem('auth_token');

        if(token) {
            const authRequest = req.clone({
                setHeaders: {
                    Authorization: `Bearer ${token}`
                }
            });
            return next.handle(authRequest);
        }

        return next.handle(req).pipe(
            catchError((error: HttpErrorResponse) => {
                if(error.status === 401) {
                    this.authService.logout();
                    this.router.navigate(['/login']);
                }

                return throwError(() => error);
            })
        );
    }


}
