import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Register } from './register';
import { of } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { ActivatedRoute, provideRouter, Router } from '@angular/router';

describe('Register', () => {
  let component: Register;
  let fixture: ComponentFixture<Register>;
  let authServiceMock: any;
  let router: Router;

  beforeEach(async () => {
    authServiceMock = {
      register: jasmine.createSpy('register').and.returnValue(of({}))
    };

    await TestBed.configureTestingModule({
      imports: [Register],
      providers: [
        {provide: AuthService, useValue: authServiceMock},
        provideRouter([]),
        {provide: ActivatedRoute, useValue: {}}
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Register);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    spyOn(router, 'navigate');
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call register auth service register method', () => {
    component.name = 'John Doe';
    component.email = 'john@email.com';
    component.password = 'john';
    component.register();
    expect(authServiceMock.register).toHaveBeenCalled();
    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });
});
