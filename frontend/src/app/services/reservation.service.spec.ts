import { HttpTestingController, provideHttpClientTesting } from "@angular/common/http/testing";
import { provideHttpClient } from "@angular/common/http";
import { TestBed } from "@angular/core/testing";
import { ReservationService } from "./reservation.service";

describe('ReservationService', () => {
    let service: ReservationService;
    let httpMock: HttpTestingController;

    beforeEach(() => {
        TestBed.configureTestingModule({
            providers: [ReservationService, provideHttpClient(), provideHttpClientTesting()]
        });
        service = TestBed.inject(ReservationService);
        httpMock = TestBed.inject(HttpTestingController);
    });

    it('find reservations', () => {
        const mockReservations = [
            {
                id: 1,
                reservationIdentifier: 'RSV-123',
                roomId: 1,
                roomName: 'Room1',
                startDate: '2025-12-23',
                endDate: '2025-12-26',
                createdAt: new Date()
            },
            {
                id: 2,
                reservationIdentifier: 'RSV-456',
                roomId: 1,
                roomName: 'Room1',
                startDate: '2025-12-28',
                endDate: '2025-12-31',
                createdAt: new Date()
            }
        ] as any;

        service.getReservations().subscribe((reservations) => {
            expect(reservations).toEqual(mockReservations);
        });

        const request = httpMock.expectOne('http://localhost:8080/api/v1/reservations');
        expect(request.request.method).toBe('GET');
        request.flush(mockReservations);
    });

    it('get reservation by identifier', () => {
        const mockReservation = {
            id: 1,
            reservationIdentifier: 'RSV-123',
            roomId: 1,
            roomName: 'Room1',
            startDate: '2025-12-23',
            endDate: '2025-12-26',
            createdAt: new Date()
        } as any;

        service.getReservationByIdentifier('RSV-123').subscribe((reservation) => {
            expect(reservation).toEqual(mockReservation);
        });

        const request = httpMock.expectOne('http://localhost:8080/api/v1/reservations/RSV-123');
        expect(request.request.method).toBe('GET');
        request.flush(mockReservation);
    });

    it('reserve room', () => {
        const reservationRequest = {
            roomId: 1,
            startDate: '2025-12-23',
            endDate: '2025-12-26'
        } as any;

        const createdReservation = {
            id: 1,
            reservationIdentifier: 'RSV-123',
            roomId: 1,
            roomName: 'Room1',
            startDate: '2025-12-23',
            endDate: '2025-12-26',
            createdAt: new Date()
        } as any;

        service.reserveRoom(1, reservationRequest).subscribe((reservation) => {
            expect(reservation).toEqual(createdReservation);
        });

        const request = httpMock.expectOne('http://localhost:8080/api/v1/reservations/1/reserve');
        expect(request.request.method).toBe('POST');
        request.flush(createdReservation);
    });

    it('update reservation', () => {
        const reservationRequest = {
            startDate: '2025-12-23',
            endDate: '2025-12-26'
        }

        const updatedReservation = {
            id: 1,
            reservationIdentifier: 'RSV-123',
            roomId: 1,
            roomName: 'Room1',
            startDate: '2025-12-23',
            endDate: '2025-12-26',
            createdAt: new Date()
        } as any;

        service.updateReservation("RSV-123", reservationRequest).subscribe((reservation) => {
            expect(reservation).toEqual(updatedReservation);
        });

        const request = httpMock.expectOne('http://localhost:8080/api/v1/reservations/RSV-123');
        expect(request.request.method).toBe('PATCH');
        request.flush(updatedReservation);
    });

    it('cancel reservation', () => {
        service.cancelReservation('RSV-123').subscribe((reservation) => {
            expect(reservation).toBeNull();
        });

        const request = httpMock.expectOne('http://localhost:8080/api/v1/reservations/RSV-123');
        expect(request.request.method).toBe('DELETE');
        request.flush(null);
    });
});