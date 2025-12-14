import { HotelService } from './../services/hotel.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HotelEdit } from './hotel-edit.component';

describe('HotelEdit', () => {
  
  let component: HotelEdit;
  let fixture: ComponentFixture<HotelEdit>;
  let hotelServiceMock: any;
  let routerMock: any;
  let activatedRouteMock: any;

  const mockHotel = {
    name: 'Hotel Test',
    description: 'Hotel de test',
    country: 'España',
    city: 'Test City',
    address: 'C/ Test, 123',
    stars: 4,
    slug: 'hotel-test',
    rooms: [
      {
        name: 'Habitación 1',
        description: 'Habitación test',
        price: 50,
        available: true
      }
    ]
  };

  beforeEach(async () => {
    hotelServiceMock = {
      getHotelBySlug: jasmine.createSpy('getHotelBySlug').and.returnValue(of(mockHotel)),
      updateHotel: jasmine.createSpy('edit').and.returnValue(of({}))
    }

    routerMock = {
        navigate: jasmine.createSpy('navigate')
    };

    activatedRouteMock = {
      snapshot: {
        paramMap: {
          get: jasmine.createSpy().and.returnValue('hotel-test')
        }
      }
    };

    await TestBed.configureTestingModule({
      imports: [HotelEdit],
      providers: [
        { provide: ActivatedRoute, useValue: activatedRouteMock },
        { provide: HotelService, useValue: hotelServiceMock },
        { provide: Router, useValue: routerMock },
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(HotelEdit);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('Hotel data should be loaded on init', () => {
    expect(hotelServiceMock.getHotelBySlug).toHaveBeenCalledWith('hotel-test');
    expect(component.hotelModel.name).toBe('Hotel Test');
    expect(component.hotelModel.rooms.length).toBe(1);
  });

  it('Submit should work and call updateHotel', () => {
    component.hotelModel = {...mockHotel, name: 'Hotel Test Name'};

    component.submit();

    expect(hotelServiceMock.updateHotel).toHaveBeenCalled();
    const [hotelSent, slugSent] = hotelServiceMock.updateHotel.calls.mostRecent().args;

    expect(hotelSent.slug).toBe('hotel-test-name');
    expect(slugSent).toBe('hotel-test');
    expect(routerMock.navigate).toHaveBeenCalledWith(['/']);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
