import { Page } from './../shared/pagination/page';
import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

export interface Room {
        id: number;
        name: string;
        description: string;
        price: number;
        averageRating?: number;
        mainImageUrl?: string;
    };

@Injectable({
    providedIn: 'root'
})
export class RoomService {

    private readonly apiUrl = 'http://localhost:8080/api/v1';

    constructor(private readonly http: HttpClient){}

    getRoomByRoomId(roomId: number): Observable<Room> {
        return this.http.get<Room>(this.apiUrl + "/rooms/" + roomId);
    }

    getRoomsByHotelSlug(hotelSlug: string, page: number): Observable<Page<Room>> {
        return this.http.get<Page<Room>>(this.apiUrl + "/rooms/hotel/" + hotelSlug, {params: {page: page}});
    }

    getNonPaginatedRoomsByHotelSlug(hotelSlug: string): Observable<Room[]> {
        return this.http.get<Room[]>(this.apiUrl + "/rooms/admin/hotel/" + hotelSlug);
    }
}
