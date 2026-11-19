import { ApplicationConfig, provideBrowserGlobalErrorListeners, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { HTTP_INTERCEPTORS, provideHttpClient, withInterceptors, withInterceptorsFromDi } from '@angular/common/http';
import { routes } from './app.routes';
import { MAT_DATE_LOCALE } from '@angular/material/core';
import { JwtInterceptor } from './jwt.interceptor';
import { provideCharts, withDefaultRegisterables } from 'ng2-charts';
import { errorInterceptor } from './interceptors/error-interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(withInterceptorsFromDi(), withInterceptors([errorInterceptor])),
    provideCharts(withDefaultRegisterables()),
    { provide: MAT_DATE_LOCALE, useValue: 'es-ES' },
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true }
  ]
};
