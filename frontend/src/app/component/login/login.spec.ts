import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Login } from './login';
import { of } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { ActivatedRoute, provideRouter, Router } from '@angular/router';

describe('Login', () => {
  let component: Login;
  let fixture: ComponentFixture<Login>;
  let authServiceMock: any;
  let router: Router;

  beforeEach(async () => {
    authServiceMock = {
      login: jasmine.createSpy('login').and.returnValue(of({}))
    };

    await TestBed.configureTestingModule({
      imports: [Login],
      providers: [
        {provide: AuthService, useValue: authServiceMock},
        provideRouter([]),
        {provide: ActivatedRoute, useValue: {}}
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(Login);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    spyOn(router, 'navigate');
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('login should call authService login and navigate to main page', () => {
    component.email = 'john@email.com';
    component.password = 'john';
    component.login();
    expect(authServiceMock.login).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['/']);
  });
});
