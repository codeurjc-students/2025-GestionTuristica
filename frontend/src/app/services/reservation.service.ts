import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

export interface ReservationRequest {
    roomId?: number;
    startDate: string;
    endDate: string;
}

export interface Reservation {
        id: number;
        reservationIdentifier: string;
        roomId: number;
        roomName: string;
        startDate: string;
        endDate: string;
        createdAt: Date;
    };

export interface ReservedRange {
    startDate: Date;
    endDate: Date;
}

@Injectable({
    providedIn: 'root'
})
export class ReservationService {

    private readonly apiUrl = 'http://localhost:8080/api/v1';

    constructor(private readonly http: HttpClient){}

    reserveRoom(roomId: number, reservation: ReservationRequest): Observable<Reservation> {
        return this.http.post<Reservation>(this.apiUrl + "/reservations/" + roomId + "/reserve", reservation);
    };

    getReservations(): Observable<Reservation[]> {
        return this.http.get<Reservation[]>(this.apiUrl + "/reservations");
    };

    getReservedDates(roomId: number): Observable<ReservedRange[]> {
        return this.http.get<ReservedRange[]>(this.apiUrl + "/reservations/" + roomId + "/reserved-dates");
    };
}
