import { Component, OnInit } from '@angular/core';
import { Review, ReviewService } from '../../services/review.service';
import { ActivatedRoute, Router } from '@angular/router';
import { MatAnchor } from "@angular/material/button";

@Component({
  selector: 'app-review-detail',
  imports: [MatAnchor],
  templateUrl: './review-detail.html',
})
export class ReviewDetail implements OnInit{

  review: Review = {
    userEmail: '',
    reservationIdentifier: '',
    roomName: '',
    message: '',
    rating: 0,
    creationTime: ''
  };

  constructor(
    private readonly reviewService: ReviewService,
    private readonly router: Router,
    private readonly activatedRoute: ActivatedRoute
  ) {};

  ngOnInit(): void {
    this.loadReview();
  }

  loadReview() {
    const reservationIdentifier = this.activatedRoute.snapshot.paramMap.get('reservationIdentifier');
    this.reviewService.getReviewByReservationIdentifier(reservationIdentifier!).subscribe({
      next: (data) => this.review = data,
      error: (err) => console.error(err)
    })
  }

  updateReview() {
    const reservationIdentifier = this.activatedRoute.snapshot.paramMap.get('reservationIdentifier');
    this.router.navigate(['/reviews/edit', reservationIdentifier]);
  }

  deleteReview() {
    const reservationIdentifier = this.activatedRoute.snapshot.paramMap.get('reservationIdentifier');
    this.reviewService.deleteReview(reservationIdentifier!).subscribe({
      next: () => {
        this.router.navigate(['/reviews']);
      },
      error: (err) => console.error(err)
    });
  }

  cancel() {
    this.router.navigate(['/reviews']);
  }
}
