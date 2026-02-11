import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { ReservationEdit } from './reservation-edit';
import { ActivatedRoute, Router } from '@angular/router';
import { RoomService } from '../../services/room.service';
import { ReservationService } from '../../services/reservation.service';
import { provideHttpClient } from '@angular/common/http';
import { RequestService } from '../../services/request.service';

describe('ReservationEdit', () => {
  let component: ReservationEdit;
  let fixture: ComponentFixture<ReservationEdit>;
  let reservationServiceMock: any;
  let roomServiceMock: any;
  let routerMock: any;
  let activatedRouteMock: any;
  let requestServiceMock: any;

  const mockReservedRanges = [
    {startDate: new Date(2025,11,24), endDate: new Date(2025,11,26)},
    {startDate: new Date(2025,11,28), endDate: new Date(2025,11,31)}
  ]

  const mockReservation = {
    id: 1,
    reservationIdentifier: 'RSV-123',
    roomId: 1,
    roomName: 'Room Test',
    startDate: '2025-12-28',
    endDate: '2025-12-31',
    createdAt: new Date(),
    status: 'ACTIVE',
    userEmail: 'john@test.com'
  }

  beforeEach(async () => {
    roomServiceMock = {

    };

    reservationServiceMock = {
      getReservationByIdentifier: jasmine.createSpy('getReservationByIdentifier').and.returnValue(of(mockReservation)),
      getReservedDates: jasmine.createSpy('getReservedDates').and.returnValue(of(mockReservedRanges)),
      updateReservation: jasmine.createSpy('updateReservation').and.returnValue(of({}))
    };

    routerMock = {
      navigate: jasmine.createSpy('navigate')
    };

    activatedRouteMock = {
      snapshot: {
        paramMap: {
          get: jasmine.createSpy().and.returnValue('RSV-123')
        }
      }
    };

    requestServiceMock = {
      createRequest: jasmine.createSpy('requestServiceMock').and.returnValue(of({}))
    }

    await TestBed.configureTestingModule({
      imports: [ReservationEdit],
      providers: [
        { provide: ActivatedRoute, useValue: activatedRouteMock },
        { provide: RoomService, useValue: roomServiceMock },
        { provide: ReservationService, useValue: reservationServiceMock },
        { provide: Router, useValue: routerMock },
        { provide: RequestService, useValue: requestServiceMock },
        provideHttpClient()
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ReservationEdit);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('reservation data and reserved ranges should be loaded on init', () => {
    expect(reservationServiceMock.getReservationByIdentifier).toHaveBeenCalledWith('RSV-123');
    expect(component.reservation).toEqual(mockReservation);
    expect(component.actualReservedRange.startDate).toEqual(new Date(2025,11,28));
    expect(component.actualReservedRange.endDate).toEqual(new Date(2025,11,31));
    expect(reservationServiceMock.getReservedDates).toHaveBeenCalledWith(1);
    expect(component.reservedDates).toEqual(mockReservedRanges.filter(range => !component.sameRange(range, component.actualReservedRange)));
  });

  it('updateReservation should call updateReservation service method', () => {

    component.dateRange.setValue({start: new Date(2026,0,5), end: new Date(2026,0,10)});

    component.updateReservation();

    expect(reservationServiceMock.updateReservation).toHaveBeenCalledWith('RSV-123', {startDate: '2026-01-05', endDate: '2026-01-10'});
    expect(routerMock.navigate).toHaveBeenCalledWith(['/']);
  });

  it('user update should call request service createReservation', () => {
    component.userAdmin = false;
    component.dateRange.setValue({start: new Date(2026,0,5), end: new Date(2026,0,10)});
    
    component.requestModification();
    expect(requestServiceMock.createRequest).toHaveBeenCalled();
  });
});
