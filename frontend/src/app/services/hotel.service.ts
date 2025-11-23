import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

export interface Hotel {
    id: number;
    name: string;
    description: string;
    country: string;
    city: string;
    address: string;
    stars: number;
}

@Injectable({
    providedIn: 'root'
})
export class HotelService {

    private readonly apiUrl = 'http://localhost:8080/api/v1';

    constructor(private readonly http: HttpClient){}

    getHotels(): Observable<Hotel[]> {
        return this.http.get<Hotel[]>(this.apiUrl + "/hotels")
    }

    create(hotel: Partial<Hotel>): Observable<Hotel> {
        return this.http.post<Hotel>(this.apiUrl + "/hotels", hotel);
    }
}
