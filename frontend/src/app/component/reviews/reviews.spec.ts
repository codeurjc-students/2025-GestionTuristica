import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Reviews } from './reviews';
import { provideHttpClient } from '@angular/common/http';
import { of } from 'rxjs';
import { ReservationService } from '../../services/reservation.service';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

describe('Reviews', () => {
  let component: Reviews;
  let fixture: ComponentFixture<Reviews>;
  let reservationServiceMock: any;
  let routerMock: any;
  let authServiceMock: any;

  const mockReservations = [
    {
      id: 1,
      reservationIdentifier: 'RSV-123',
      roomId: 1,
      roomName: 'Room Test',
      startDate: '2025-12-28',
      endDate: '2025-12-31',
      createdAt: new Date('2026-01-01'),
      userEmail: 'john@example.com',
      status: 'ACTIVE',
      reviewed: false
    },
    {
      id: 2,
      reservationIdentifier: 'RSV-1234',
      roomId: 1,
      roomName: 'Room Test',
      startDate: '2026-01-02',
      endDate: '2026-01-05',
      createdAt: new Date('2026-01-01'),
      userEmail: 'john@email.com',
      status: 'ACTIVE',
      reviewed: true
    }
  ];

  beforeEach(async () => {

    reservationServiceMock = {
      getReservationsByUserId: jasmine.createSpy('getReservationsByUserId').and.returnValue(of(mockReservations))
    };

    routerMock = {
      navigate: jasmine.createSpy('navigate')
    };

    authServiceMock = {
      getUserId: jasmine.createSpy('getUserId').and.returnValue(of(1))
    };

    await TestBed.configureTestingModule({
      imports: [Reviews],
      providers: [
        { provide: ReservationService, useValue: reservationServiceMock },
        { provide: Router, useValue: routerMock },
        { provide: AuthService, useValue: authServiceMock },
        provideHttpClient()
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Reviews);
    component = fixture.componentInstance; 
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load reviews on init', () => {
    expect(reservationServiceMock.getReservationsByUserId).toHaveBeenCalled();
    expect(component.reservations).toEqual(mockReservations)
  });

  it('goToReviewCreation should navigate to create review page', () => {
    component.goToReviewCreation('RSV-123');
    expect(routerMock.navigate).toHaveBeenCalledWith(['/reviews/create', 'RSV-123']);
  });

  it('viewReview should navigate to review detail page', () => {
    component.viewReview('RSV-1234');
    expect(routerMock.navigate).toHaveBeenCalledWith(['/reviews/detail', 'RSV-1234']);
  });
});
