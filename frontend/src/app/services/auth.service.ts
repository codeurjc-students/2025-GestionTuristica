import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable, tap } from "rxjs";

export interface AuthResponse {
    token: string;
}

export interface LoginRequest {
    email: string;
    password: string;
}

export interface RegisterRequest {
    name: string;
    email: string;
    password: string;
}

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    private readonly apiUrl = 'http://localhost:8080/api/v1/auth';

    private readonly loggedInSubject = new BehaviorSubject<boolean>(this.isAuthenticated());
    loggedIn$ = this.loggedInSubject.asObservable();

    constructor(private readonly http: HttpClient){}

    login(credentials: LoginRequest): Observable<AuthResponse> {
        return this.http.post<AuthResponse>(this.apiUrl + "/login", credentials)
        .pipe(
            tap(response => {
                this.storeToken(response.token);
                this.loggedInSubject.next(true);
            })
        );
    }

    register(credentials: RegisterRequest): Observable<void> {
        return this.http.post<void>(this.apiUrl + "/register", credentials);
    }

    logout(): void {
        localStorage.removeItem('auth_token');
        this.loggedInSubject.next(false);
    }

    getToken(): string | null {
        return localStorage.getItem('auth_token');
    }

    getRole(): string | null {
        const token = this.getToken();
        if(!token) return null;

        const payload = JSON.parse(atob(token.split('.')[1]));
        return payload.role;
    }

    isAuthenticated(): boolean {
        return !!this.getToken();
    }

    private storeToken(token: string): void {
        localStorage.setItem('auth_token', token);
    }
}
