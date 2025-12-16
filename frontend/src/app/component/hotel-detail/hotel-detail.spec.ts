import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs'
import { HotelDetail } from './hotel-detail';
import { ActivatedRoute, Router } from '@angular/router';
import { HotelService } from '../../services/hotel.service';

describe('HotelDetail', () => {
  let component: HotelDetail;
  let fixture: ComponentFixture<HotelDetail>;
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
      getHotelBySlug: jasmine.createSpy('getHotelBySlug').and.returnValue(of(mockHotel))
    };

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
      imports: [HotelDetail],
      providers: [
        { provide: ActivatedRoute, useValue: activatedRouteMock },
        { provide: HotelService, useValue: hotelServiceMock },
        { provide: Router, useValue: routerMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(HotelDetail);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('hotel data should be loaded on init', () => {
    expect(hotelServiceMock.getHotelBySlug).toHaveBeenCalledWith('hotel-test');
    expect(component.hotel.name).toBe('Hotel Test');
    expect(component.hotel.rooms.length).toBe(1);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
