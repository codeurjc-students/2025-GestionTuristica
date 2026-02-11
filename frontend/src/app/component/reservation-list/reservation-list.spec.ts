import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReservationList } from './reservation-list';
import { ReservationService } from '../../services/reservation.service';
import { of } from 'rxjs';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { AuthService } from '../../services/auth.service';
import { RequestService } from '../../services/request.service';

describe('ReservationList', () => {
  let component: ReservationList;
  let fixture: ComponentFixture<ReservationList>;
  let reservationServiceMock: any;
  let authServiceMock: any;
  let requestServiceMock: any;

  const mockReservations = [
    {
      id: 1,
      reservationIdentifier: 'RSV-123',
      roomId: 1,
      roomName: 'Room Test',
      startDate: '2025-12-28',
      endDate: '2025-12-31',
      createdAt: new Date()
    },
    {
      id: 2,
      reservationIdentifier: 'RSV-1234',
      roomId: 1,
      roomName: 'Room Test',
      startDate: '2026-01-02',
      endDate: '2026-01-05',
      createdAt: new Date()
    }
  ];

  beforeEach(async () => {
    reservationServiceMock = {
      getReservations: jasmine.createSpy('getReservations').and.returnValue(of(mockReservations)),
      getReservationsByUserId: jasmine.createSpy('getReservationsByUserId').and.returnValue(of(mockReservations))
    }

    authServiceMock = {
      getRole: jasmine.createSpy('getRole').and.returnValue(of('ROLE_USER')),
      getUserId: jasmine.createSpy('getUserId').and.returnValue(of(1)),
      getUserEmail: jasmine.createSpy('getUserEmail').and.returnValue(of('john@test.com'))
    }

    requestServiceMock = {
      createRequest: jasmine.createSpy('requestCancellation').and.returnValue(of({}))
    }

    await TestBed.configureTestingModule({
      imports: [ReservationList],
      providers: [
        { provide: ReservationService, useValue: reservationServiceMock },
        { provide: AuthService, useValue: authServiceMock},
        { provide: RequestService, useValue: requestServiceMock },
        provideRouter([]),
        provideHttpClient()
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReservationList);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load reservations on init', () => {
    expect(component.reservations.length).toBe(2);
    expect(reservationServiceMock.getReservationsByUserId).toHaveBeenCalled();
  });

  it('should create request on cancellation by user', () => {
    component.userAdmin = false;
    component.requestCancellation('RSV-123');
    expect(requestServiceMock.createRequest).toHaveBeenCalled();
  });
});
