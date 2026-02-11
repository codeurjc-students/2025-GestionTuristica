import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

export interface ReservationRequest {
    startDate: string;
    endDate: string;
}

export interface Reservation {
        id: number;
        reservationIdentifier: string;
        roomId: number;
        roomName: string;
        userEmail: string;
        startDate: string;
        endDate: string;
        status: string;
        createdAt: Date;
    };

export interface ReservedRange {
    startDate: Date;
    endDate: Date;
}

export type ReservationFilter = 'CANCELLED' | 'NON_CANCELLED';

@Injectable({
    providedIn: 'root'
})
export class ReservationService {

    private readonly apiUrl = 'http://localhost:8080/api/v1';

    constructor(private readonly http: HttpClient){}

    reserveRoom(roomId: number, reservation: ReservationRequest): Observable<Reservation> {
        return this.http.post<Reservation>(this.apiUrl + "/reservations/" + roomId + "/reserve", reservation);
    };

    getReservations(filter: ReservationFilter): Observable<Reservation[]> {
        return this.http.get<Reservation[]>(this.apiUrl + "/reservations", {
                params: filter ? {filter: filter} : {}
            });
    };

    getReservedDates(roomId: number): Observable<ReservedRange[]> {
        return this.http.get<ReservedRange[]>(this.apiUrl + "/reservations/" + roomId + "/reserved-dates");
    };

    getReservationByIdentifier(reservationIdentifier: string): Observable<Reservation> {
        return this.http.get<Reservation>(this.apiUrl + "/reservations/" + reservationIdentifier);
    }

    getReservationsByUserId(userId: string, filter: ReservationFilter): Observable<Reservation[]> {
        return this.http.get<Reservation[]>(this.apiUrl + "/reservations/user/" + userId, {
            params: filter ? {filter: filter} : {}
         });
    }

    updateReservation(reservationIdentifier: string, reservationRequest: Partial<ReservationRequest>): Observable<Reservation> {
        return this.http.patch<Reservation>(this.apiUrl + "/reservations/" + reservationIdentifier, reservationRequest);
    }

    cancelReservation(reservationIdentifier: string): Observable<void> {
        return this.http.delete<void>(this.apiUrl + "/reservations/" + reservationIdentifier);
    }
}
