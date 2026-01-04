import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';

import { RoomDetail } from './room-detail';
import { Room } from '../../services/hotel.service';
import { of } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { RoomService } from '../../services/room.service';
import { ReservationService } from '../../services/reservation.service';

describe('RoomDetail', () => {
  let component: RoomDetail;
  let fixture: ComponentFixture<RoomDetail>;
  let roomServiceMock: any;
  let reservationServiceMock: any;
  let routerMock: any;
  let activatedRouteMock: any;

  const mockRoom: Room = {
    id: 1,
    name: 'Room Test',
    description: 'Test room',
    price: 30,
    available: true
  }

  const mockReservedRanges = [
    {startDate: new Date(2025,11,24), endDate: new Date(2025,11,26)},
    {startDate: new Date(2025,11,28), endDate: new Date(2025,11,31)}
  ]

  beforeEach(async () => {

    roomServiceMock = {
      getRoomByRoomId: jasmine.createSpy('getRoomByRoomId').and.returnValue(of(mockRoom))
    };

    reservationServiceMock = {
      getReservedDates: jasmine.createSpy('getReservedDates').and.returnValue(of(mockReservedRanges)),
      reserveRoom: jasmine.createSpy('reserveRoom').and.returnValue(of({}))
    };

    routerMock = {
      navigate: jasmine.createSpy('navigate')
    };

    activatedRouteMock = {
      snapshot: {
        paramMap: {
          get: jasmine.createSpy().and.returnValue('1')
        }
      }
    };
    await TestBed.configureTestingModule({
      imports: [RoomDetail],
      providers: [
        { provide: ActivatedRoute, useValue: activatedRouteMock },
        { provide: RoomService, useValue: roomServiceMock },
        { provide: ReservationService, useValue: reservationServiceMock },
        { provide: Router, useValue: routerMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RoomDetail);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('room data and reserved ranges should be loaded on init', () => {
    expect(roomServiceMock.getRoomByRoomId).toHaveBeenCalledWith(1);
    expect(component.room).toEqual(mockRoom);
    expect(reservationServiceMock.getReservedDates).toHaveBeenCalledWith(1);
    expect(component.reservedDates).toEqual(mockReservedRanges);
  });

  it('should reserve room and navigate on success', () => {
    component.dateRange.patchValue({start: new Date(2026,0,1), end: new Date(2026,0,4)});
    component.reserveRoom();

    expect(reservationServiceMock.reserveRoom).toHaveBeenCalled();
    const [roomId, reservation] = reservationServiceMock.reserveRoom.calls.argsFor(0);

    expect(roomId).toBe(1);
    expect(reservation.startDate).toEqual('2026-01-01');
    expect(reservation.endDate).toEqual('2026-01-04');
    expect(routerMock.navigate).toHaveBeenCalled();
  });

  it('should block overlaped dates and show alert', fakeAsync(() => {
    spyOn(globalThis, 'alert');
    component.dateRange.patchValue({start: new Date(2025,11,25), end: new Date(2025,11,29)});
    tick();
    expect(globalThis.alert).toHaveBeenCalledWith('Las fechas seleccionadas contienen fechas ya reservadas. Por favor, elija otras fechas.');
    expect(component.dateRange.get('start')?.value).toBeNull();
    expect(component.dateRange.get('end')?.value).toBeNull();
  }));
});
