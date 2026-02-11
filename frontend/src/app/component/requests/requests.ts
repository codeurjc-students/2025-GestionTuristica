import { Component, OnInit } from '@angular/core';
import { ModificationRequest, RequestFilter, RequestService } from '../../services/request.service';
import { MatButtonModule } from "@angular/material/button";

@Component({
  selector: 'app-requests',
  imports: [MatButtonModule],
  templateUrl: './requests.html'
})
export class Requests implements OnInit{
  requests: ModificationRequest[] = [];
  currentStatus: RequestFilter = 'PENDING';

  constructor(private readonly requestService: RequestService) {}

  ngOnInit(): void {
      this.loadRequests();
  }

  loadRequests() {
    this.requestService.getRequests(this.currentStatus).subscribe({
        next: (data) => {
          this.requests = data;
        },
        error: (err) => console.error(err)
      });
  }

  approveRequest(requestId: number) {
    this.requestService.approveRequest(requestId).subscribe({
      next: () => {
        this.requests = this.requests.filter(request => request.id !== requestId);
      },
      error: (err) => console.error(err)
    });
  }

  rejectRequest(requestId: number) {
    this.requestService.rejectRequest(requestId).subscribe({
      next: () => {
        this.requests = this.requests.filter(request => request.id !== requestId);
      },
      error: (err) => console.error(err)
    });
  }

  changeStatus(status: RequestFilter) {
    this.currentStatus = status;
    this.loadRequests();
  }
}
