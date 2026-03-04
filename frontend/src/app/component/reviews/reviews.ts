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

  reservations: Reservation[] = [];
  reservationsFilter: ReservationFilter = 'NON_CANCELLED';

  constructor(
    private readonly reviewService: ReviewService,
    private readonly reservationService: ReservationService,
    private readonly authService: AuthService,
    private readonly router: Router
  ){}

  ngOnInit(): void {
    const userId = this.authService.getUserId();
    if(userId) {
      this.reservationService.getReservationsByUserId(userId, this.reservationsFilter).subscribe(
        next: (data) => {
          this.reservations = data;
        },
        error: (error) => console.error(error)
      )
    }
  }

  goToReviewCreation(reservationIdentifier: string) {
    this.router.navigate(['/reviews/create', reservationIdentifier]);
  }

  viewReview(reservationIdentifier: string) {
    this.router.navigate(['reviews/detail', reservationIdentifier]);
  }

  canBeReviewed(reservation: Reservation): boolean {
    const today = new Date();
    const endDate = new Date(reservation.endDate);

    today.setHours(0,0,0,0);
    endDate.setHours(0,0,0,0);

    return !reservation.reviewed && endDate <= today;
  }

}
