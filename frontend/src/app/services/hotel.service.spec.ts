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

    it('get hotel by slug', () => {
        const mockHotel = {id: 1, name: 'Hotel', slug: 'hotel-slug'} as any;

        service.getHotelBySlug('hotel-slug').subscribe((hotel) => {
            expect(hotel).toEqual(mockHotel);
        });

        const request = httpMock.expectOne('http://localhost:8080/api/v1/hotels/hotel-slug');
        expect(request.request.method).toBe('GET');
        request.flush(mockHotel);
    });

    it('create hotel', () => {
        const newHotel = {
            name: 'New Hotel',
            slug: 'new-hotel',
            description: '',
            country: 'España',
            city: 'Madrid',
            stars: 1,
            address: 'test address 1',
            rooms: []
        } as any;

        const createdHotel = {
            id: 1,
            name: 'New Hotel',
            slug: 'new-hotel',
            description: '',
            country: 'España',
            city: 'Madrid',
            stars: 1,
            address: 'test address 1',
              rooms: []
        } as any;

        service.create(newHotel).subscribe((hotel) => {
            expect(hotel).toEqual(createdHotel);
        });

        const request = httpMock.expectOne('http://localhost:8080/api/v1/hotels');
        expect(request.request.method).toBe('POST');
        request.flush(createdHotel);
    });

    it('update hotel', () => {
        const updatedHotel = {
            id: 1,
            name: 'New Hotel',
            slug: 'new-hotel',
            description: '',
            country: 'España',
            city: 'Madrid',
            stars: 1,
            address: 'test address 1',
            rooms: []
        } as any;

        service.updateHotel(updatedHotel, 'old-hotel-slug').subscribe((hotel) => {
            expect(hotel).toEqual(updatedHotel);
        });

        const request = httpMock.expectOne('http://localhost:8080/api/v1/hotels/old-hotel-slug');
        expect(request.request.method).toBe('PUT');
        request.flush(updatedHotel);
    });

    it('remove hotel', () => {
        service.removeHotel('hotel-slug').subscribe((hotel) => {
            expect(hotel).toBeNull();
        });

        const request = httpMock.expectOne('http://localhost:8080/api/v1/hotels/hotel-slug');
        expect(request.request.method).toBe('DELETE');
        request.flush(null);
    });
});