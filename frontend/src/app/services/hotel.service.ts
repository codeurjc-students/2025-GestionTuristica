import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

export interface Room {
        id: number;
        name: string;
        description: string;
        price: number;
        available: boolean;
        averageRating: number;
    };
export interface Hotel {
    id: number;
    name: string;
    description: string;
    country: string;
    city: string;
    address: string;
    stars: number;
    slug: string;
    rooms: Room[];
    averageRating: number;
}

export interface HotelRequest {
    name: string;
    description: string;
    country: string;
    city: string;
    address: string;
    stars: number;
    slug: string;
    rooms: RoomRequest[];
}

export interface RoomRequest {
    name: string;
    description: string;
    price: number;
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

    getHotelBySlug(slug: string): Observable<Hotel> {
        return this.http.get<Hotel>(this.apiUrl + "/hotels/" + slug);
    }

    create(hotel: HotelRequest): Observable<Hotel> {
        return this.http.post<Hotel>(this.apiUrl + "/hotels", hotel);
    }

    updateHotel(hotel: HotelRequest, oldSlug: string): Observable<Hotel> {
        return this.http.put<Hotel>(this.apiUrl + "/hotels/" + oldSlug, hotel);
    }

    removeHotel(slug: string): Observable<Hotel> {
        return this.http.delete<Hotel>(this.apiUrl + "/hotels/" + slug)
    }
}
