import { Component, OnInit } from '@angular/core';
import { ReviewService } from '../../services/review.service';
import { Router } from '@angular/router';
import { Reservation, ReservationFilter, ReservationService } from '../../services/reservation.service';
import { AuthService } from '../../services/auth.service';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-reviews',
  imports: [DatePipe],
  templateUrl: './reviews.html',
})
export class Reviews implements OnInit {

  userId!: string;
  reservations: Reservation[] = [];
  reservationsFilter: ReservationFilter = 'NON_CANCELLED';
  pageNumber: number = 0;
  firstPage!: boolean;
  lastPage!: boolean;
  numberOfReviews!: number;
  numberOfPages!: number;

  constructor(
    private readonly reviewService: ReviewService,
    private readonly reservationService: ReservationService,
    private readonly authService: AuthService,
    private readonly router: Router
  ){}

  ngOnInit(): void {
    this.userId = this.authService.getUserId() || '';
    if(this.userId) {
      this.loadReviews(this.userId, this.pageNumber);
    }
  }

  loadReviews(userId: string, page: number) {
    this.reservationService.getReservationsByUserId(userId, this.reservationsFilter, page).subscribe({
        next: (data) => {
          this.reservations = data.content;
          this.pageNumber = data.number;
          this.firstPage = data.first;
          this.lastPage = data.last;
          this.numberOfReviews = data.totalElements;
          this.numberOfPages = data.totalPages;
        },
        error: (error) => console.error(error)
      });
  }

  goToReviewCreation(reservationIdentifier: string) {
    this.router.navigate(['/reviews/create', reservationIdentifier]);
  }

  viewReview(reservationIdentifier: string) {
    this.router.navigate(['/reviews/detail', reservationIdentifier]);
  }

  canBeReviewed(reservation: Reservation): boolean {
    const today = new Date();
    const endDate = new Date(reservation.endDate);

    today.setHours(0,0,0,0);
    endDate.setHours(0,0,0,0);

    return !reservation.reviewed && endDate <= today;
  }

  nextPage() {
    if(!this.lastPage) {
      this.pageNumber++;
      this.loadReviews(this.userId, this.pageNumber);
    }
  }

  previousPage() {
    if(!this.firstPage) {
      this.pageNumber--;
      this.loadReviews(this.userId, this.pageNumber);
    }
  }
}
