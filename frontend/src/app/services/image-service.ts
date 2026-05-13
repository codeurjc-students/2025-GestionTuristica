import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

export interface Image {
        id: number;
        url: string;
        index: number;
    };

export interface ImagePreview {
        file: File;
        type: 'HOTEL' | 'ROOM';
        previewUrl: string;
        roomId?: number;
        position: number;
    };

@Injectable({
    providedIn: 'root',
})
export class ImageService {
    
    private readonly apiUrl = 'http://localhost:8080/api/v1';

    constructor(private readonly http: HttpClient) {}

    uploadHotelImage(formData: FormData, slug: string, position: number) {
        return this.http.post<Image>(this.apiUrl + "/images/hotel/" + slug + "?position=" + position, formData);       
    };

    getImagesByHotelSlug(slug: string) {
        return this.http.get<Image[]>(this.apiUrl + "/images/hotel/" + slug);
    }

    uploadRoomImage(formData: FormData, roomId: number, position: number) {
        return this.http.post<Image[]>(this.apiUrl + "/images/room/" + roomId + "?position=" + position, formData);
    }

    getImagesByRoomId(roomId: number) {
        return this.http.get<Image[]>(this.apiUrl + "/images/room/" + roomId);
    }
}
