import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

export interface ModificationRequest {
    id: number;
    status: string;
    reservationIdentifier: string;
    requestedStartDate?: string;
    requestedEndDate?: string;
    type: string;
    userEmail: string;
}

export interface ModificationRequestCreation {
    type: 'MODIFICATION' | 'CANCELLATION';
    requestedStartDate?: string;
    requestedEndDate?: string;
    userEmail: string;
    reservationIdentifier: string;
}

export type RequestFilter = 'PENDING' | 'RESOLVED' | 'APPROVED' | 'REJECTED' | 'ALL';

@Injectable({
    providedIn: 'root'
})
export class RequestService {

    private readonly apiUrl = 'http://localhost:8080/api/v1';

    constructor(private readonly http: HttpClient){}

    createRequest(modificationRequest: ModificationRequestCreation): Observable<void> {
        return this.http.post<void>(this.apiUrl + "/requests", modificationRequest);
    };

    getRequests(status?: RequestFilter): Observable<ModificationRequest[]> {

        return this.http.get<ModificationRequest[]>(this.apiUrl + "/requests", {
            params: status ? { status: status} : {}
        });
    };

    approveRequest(requestId: number): Observable<void> {
        return this.http.patch<void>(this.apiUrl + '/requests/approve/' + requestId, {});
    };

    rejectRequest(requestId: number): Observable<void> {
        return this.http.patch<void>(this.apiUrl + '/requests/reject/' + requestId, {});
    };
}