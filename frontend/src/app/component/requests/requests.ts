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
  pageNumber: number = 0;
  firstPage!: boolean;
  lastPage!: boolean;
  numberOfRequests!: number;
  numberOfPages!: number;


  constructor(private readonly requestService: RequestService) {}

  ngOnInit(): void {
      this.loadRequests(this.pageNumber);
  }

  loadRequests(page: number) {
    this.requestService.getRequests(this.currentStatus, page).subscribe({
        next: (data) => {
          this.requests = data.content;
          this.pageNumber = data.number;
          this.firstPage = data.first;
          this.lastPage = data.last;
          this.numberOfRequests = data.totalElements;
          this.numberOfPages = data.totalPages;
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
    this.loadRequests(this.pageNumber);
  }

  nextPage() {
    if(!this.lastPage) {
      this.pageNumber++;
      this.loadRequests(this.pageNumber);
    }
  }

  previousPage() {
    if(!this.firstPage) {
      this.pageNumber--;
      this.loadRequests(this.pageNumber);
    }
  }
}
