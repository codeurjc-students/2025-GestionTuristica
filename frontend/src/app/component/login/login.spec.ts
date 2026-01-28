import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Login } from './login';
import { of } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

describe('Login', () => {
  let component: Login;
  let fixture: ComponentFixture<Login>;
  let authServiceMock: any;
  let routerMock: any;

  beforeEach(async () => {
    authServiceMock = {
      login: jasmine.createSpy('login').and.returnValue(of({}))
    };

    routerMock = {
      navigate: jasmine.createSpy('navigate')
    };

    await TestBed.configureTestingModule({
      imports: [Login],
      providers: [
        {provide: AuthService, useValue: authServiceMock},
        {provide: Router, useValue: routerMock}
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(Login);
    component = fixture.componentInstance;
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
    expect(routerMock.navigate).toHaveBeenCalledWith(['/']);
  });
});
