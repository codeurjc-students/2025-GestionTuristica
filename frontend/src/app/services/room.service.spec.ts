import { HttpTestingController, provideHttpClientTesting } from "@angular/common/http/testing";
import { provideHttpClient } from "@angular/common/http";
import { TestBed } from "@angular/core/testing";
import { RoomService } from "./room.service";

describe('RoomService', () => {
    let service: RoomService;
    let httpMock: HttpTestingController;

    beforeEach(() => {
        TestBed.configureTestingModule({
            providers: [RoomService, provideHttpClient(), provideHttpClientTesting()]
        });
        service = TestBed.inject(RoomService);
        httpMock = TestBed.inject(HttpTestingController);
    });

    it('get room by roomId', () => {
        const mockRoom = {
            id: 1,
            name: 'Room1',
            description: 'A nice room',
            price: 50,
            available: true
        } as any;

        service.getRoomByRoomId(1).subscribe((room) => {
            expect(room).toEqual(mockRoom);
        });

        const request = httpMock.expectOne('http://localhost:8080/api/v1/rooms/1');
        expect(request.request.method).toBe('GET');
        request.flush(mockRoom);
    });
});