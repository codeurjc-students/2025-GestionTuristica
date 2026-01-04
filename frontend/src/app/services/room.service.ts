import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";

export interface Room {
        id?: number;
        name: string;
        description: string;
        price: number;
        available: boolean;
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
}
