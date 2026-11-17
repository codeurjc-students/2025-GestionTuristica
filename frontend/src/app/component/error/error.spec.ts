import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Error } from './error';
import { ActivatedRoute, convertToParamMap } from '@angular/router';
import { BehaviorSubject } from 'rxjs';

describe('Error', () => {
  let component: Error;
  let fixture: ComponentFixture<Error>;
  let activatedRouteMockSubject: BehaviorSubject<any>;
  let activatedRouteMock: any;

  const testCases = [
    {
      reason: 'forbidden',
      expectedMessage: 'No tienes los permisos necesarios para acceder a esta página'
    },
    {
      reason: 'not-found',
      expectedMessage: 'No ha sido posible encontrar el recurso o la página que estás buscando'
    },
    {
      reason: 'server-error',
      expectedMessage: 'Ha ocurrido un error inesperado'
    },
    {
      reason: 'unknown',
      expectedMessage: 'Ha ocurrido un error inesperado'
    },
    {
      reason: 'unauthorized',
      expectedMessage: 'No tienes permisos para acceder a esta página, inicia sesión para continuar'
    }
  ]

  beforeEach(async () => {

    activatedRouteMockSubject = new BehaviorSubject(convertToParamMap({}));
    activatedRouteMock = {
      queryParamMap: activatedRouteMockSubject.asObservable()
    }

    await TestBed.configureTestingModule({
      imports: [Error],
      providers: [
        { provide: ActivatedRoute, useValue: activatedRouteMock }
      ]
    })
      .compileComponents();
  });

  it('should create', () => {
    fixture = TestBed.createComponent(Error);
    component = fixture.componentInstance;

    expect(component).toBeTruthy();
  });

  testCases.forEach(({ reason, expectedMessage }) => {
    it('should show the correct error message for reason ' + reason, () => {
      activatedRouteMockSubject.next(convertToParamMap({ reason }))

      fixture = TestBed.createComponent(Error);
      component = fixture.componentInstance;
      fixture.detectChanges();

      expect(component.errorMessage).toBe(expectedMessage);
    });
  })
});
