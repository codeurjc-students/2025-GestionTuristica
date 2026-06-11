import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { Page } from "../shared/pagination/page";

export interface Room {
        id: number;
        name: string;
        description: string;
        price: number;
        averageRating?: number;
        mainImageUrl?: string;
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
    mainImageUrl?: string;
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
    id?: number;
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

    getHotels(pageNumber: number): Observable<Page<Hotel>> {
        return this.http.get<Page<Hotel>>(this.apiUrl + "/hotels", {params: {page: pageNumber}});
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
