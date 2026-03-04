import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

export interface ReviewRequest {
    message: string;
    reservationIdentifier: string;
    rating: number;
}

export interface ReviewUpdateRequest {
    message: string;
    rating: number;
}

export interface Review {
    userEmail: string;
    reservationIdentifier: string;
    roomName: string;
    message: string;
    rating: number;
    creationTime: string;
}

@Injectable({
    providedIn: 'root'
})
export class ReviewService {

    private readonly apiUrl = 'http://localhost:8080/api/v1';

    constructor(private readonly http: HttpClient){}

    createReview(request: ReviewRequest): Observable<Review> {
        return this.http.post<Review>(this.apiUrl + "/reviews", request);
    };

    getReviewsByRoom(roomId: number): Observable<Review[]>{
        return this.http.get<Review[]>(this.apiUrl + "/reviews/room/" + roomId);
    };

    getReviewByReservationIdentifier(reservationIdentifier: string): Observable<Review> {
        return this.http.get<Review>(this.apiUrl + "/reviews/reservation/" + reservationIdentifier);
    };

    updateReview(reservationIdentifier: string, request: ReviewUpdateRequest): Observable<Review> {
        return this.http.put<Review>(this.apiUrl + '/reviews/reservation/' + reservationIdentifier, request);
    }

    deleteReview(reservationIdentifier: string): Observable<void> {
        return this.http.delete<void>(this.apiUrl + '/reviews/reservation/' + reservationIdentifier);
    }
}
