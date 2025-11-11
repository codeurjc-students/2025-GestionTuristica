import { HttpTestingController, provideHttpClientTesting } from "@angular/common/http/testing";
import { provideHttpClient } from "@angular/common/http";
import { HotelService } from "./hotel.service"
import { TestBed } from "@angular/core/testing";

describe('HotelService', () => {
    let service: HotelService;
    let httpMock: HttpTestingController;

    beforeEach(() => {
        TestBed.configureTestingModule({
            providers: [HotelService, provideHttpClient(), provideHttpClientTesting()]
        });
        service = TestBed.inject(HotelService);
        httpMock = TestBed.inject(HttpTestingController);
    });

    it('find hotels', () => {
        const mockHotels = [{id: 1, name: 'Hotel'}] as any;

        service.getHotels().subscribe((hotels) => {
            expect(hotels).toEqual(mockHotels);
        });

        const request = httpMock.expectOne('http://localhost:8080/api/v1/hotels');
        expect(request.request.method).toBe('GET');
        request.flush(mockHotels);
    });
});