import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReservationList } from './reservation-list';
import { ReservationService } from '../../services/reservation.service';
import { of } from 'rxjs';
import { provideRouter } from '@angular/router';

describe('ReservationList', () => {
  let component: ReservationList;
  let fixture: ComponentFixture<ReservationList>;
  let reservationServiceMock: any;

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
      getReservations: jasmine.createSpy('getReservations').and.returnValue(of(mockReservations))
    }

    await TestBed.configureTestingModule({
      imports: [ReservationList],
      providers: [
        { provide: ReservationService, useValue: reservationServiceMock },
        provideRouter([])
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
    expect(reservationServiceMock.getReservations).toHaveBeenCalled();
  });
});
