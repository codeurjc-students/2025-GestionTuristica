import { HotelService } from './../services/hotel.service';
import { ComponentFixture, TestBed } from "@angular/core/testing";
import { of } from 'rxjs';
import { HotelListComponent } from "./hotel-list.component";
import { provideHttpClient } from '@angular/common/http';
import { provideCharts, withDefaultRegisterables } from 'ng2-charts';
import { ReservationService } from '../services/reservation.service';

describe('HotelListComponent', () => {
    let component: HotelListComponent;
    let fixture: ComponentFixture<HotelListComponent>;
    let mockHotelService: any;
    let mockReservationService: any;


    const mockPage = {
    content: [],
    number: 0,
    first: true,
    last: true,
    totalElements: 2,
    totalPages: 1
    }

    const mockReservationsData = [
        {
            hotel: "Hotel 1",
            reservations: 10
        },
        {
            hotel: "Hotel 2",
            reservations: 8
        },
        {
            hotel: "Hotel 3",
            reservations: 7
        },
        {
            hotel: "Hotel 4",
            reservations: 4
        },
        {
            hotel: "Hotel 5",
            reservations: 2
        }
    ]

    beforeEach(async () => {
        mockHotelService = {
            getHotels: jasmine.createSpy('getHotels').and.returnValue(of(mockPage)),
            removeHotel: jasmine.createSpy('removeHotel').and.returnValue(of({}))
        };

        mockReservationService = {
            getMostReservedHotels: jasmine.createSpy('getMostReservedHotels').and.returnValue(of(mockReservationsData))
        }

        await TestBed.configureTestingModule({
            imports: [HotelListComponent],
            providers: [
                {provide: HotelService, useValue: mockHotelService},
                {provide: ReservationService, useValue: mockReservationService},
                provideHttpClient(),
                provideCharts(withDefaultRegisterables())
            ]
        }).compileComponents();

        fixture = TestBed.createComponent(HotelListComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('create component', () => {
        expect(component).toBeTruthy();
    });

    it('call onInit', () => {
        component.ngOnInit();
        expect(mockHotelService.getHotels).toHaveBeenCalled();
        expect(mockReservationService.getMostReservedHotels).toHaveBeenCalled();
    });

    it('empty initially', () => {
       expect(component.hotels.length).toBe(0);
    });

    it('remove hotel', () => {
        component.deleteHotel('test-slug');
        expect(mockHotelService.removeHotel).toHaveBeenCalledWith('test-slug');
    });
})
