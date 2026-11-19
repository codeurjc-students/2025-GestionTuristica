import { TestBed } from '@angular/core/testing';
import { HttpClient, provideHttpClient, withInterceptors } from '@angular/common/http';

import { errorInterceptor } from './error-interceptor';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { Router } from '@angular/router';

describe('errorInterceptor', () => {

  let httpTesting: HttpTestingController;
  let httpClient: HttpClient;
  let routerMock: jasmine.SpyObj<Router>;

  beforeEach(() => {

    routerMock = jasmine.createSpyObj('Router', ['navigate']);

    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(
          withInterceptors([errorInterceptor])
        ),
        provideHttpClientTesting(),
        { provide: Router, useValue: routerMock }
      ]
    });

    httpTesting = TestBed.inject(HttpTestingController);
    httpClient = TestBed.inject(HttpClient);
  });

  afterEach(() => {
    httpTesting.verify();
    localStorage.clear();
  });


  it('should remove token and redirect to login on 401', () => {

    localStorage.setItem('token', 'token-test');

    httpClient.get('/hotels/test').subscribe({
      error: () => { }
    });

    const req = httpTesting.expectOne('/hotels/test');

    req.flush(
      'Error',
      {
        status: 401,
        statusText: 'unauthorized'
      }
    );

    expect(localStorage.getItem('token')).toBeNull();
    expect(routerMock.navigate).toHaveBeenCalledWith(['/login'])
  });

  it('should redirect to error on 403 with parameter forbidden', () => {

    httpClient.get('/hotels/test').subscribe({
      error: () => { }
    });

    const req = httpTesting.expectOne('/hotels/test');

    req.flush(
      'Error',
      {
        status: 403,
        statusText: 'forbidden'
      }
    );

    expect(localStorage.getItem('token')).toBeNull();
    expect(routerMock.navigate).toHaveBeenCalledWith(['/error'], { queryParams: { reason: 'forbidden' } });
  });

  it('should redirect to error on 403 with parameter not found', () => {

    httpClient.get('/hotels/test').subscribe({
      error: () => { }
    });

    const req = httpTesting.expectOne('/hotels/test');

    req.flush(
      'Error',
      {
        status: 404,
        statusText: 'not-found'
      }
    );

    expect(localStorage.getItem('token')).toBeNull();
    expect(routerMock.navigate).toHaveBeenCalledWith(['/error'], { queryParams: { reason: 'not-found' } });
  });

  it('should redirect to error on 500-600 with parameter server-error', () => {

    httpClient.get('/hotels/test').subscribe({
      error: () => { }
    });

    const req = httpTesting.expectOne('/hotels/test');

    req.flush(
      'Error',
      {
        status: 500,
        statusText: 'internal-server-error'
      }
    );

    expect(localStorage.getItem('token')).toBeNull();
    expect(routerMock.navigate).toHaveBeenCalledWith(['/error'], { queryParams: { reason: 'server-error' } });
  });
});
