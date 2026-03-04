import { Component, OnInit } from '@angular/core';
import { Review, ReviewService, ReviewUpdateRequest } from '../../services/review.service';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-review-edit',
  imports: [CommonModule, FormsModule],
  templateUrl: './review-edit.html',
})
export class ReviewEdit implements OnInit{

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
    ) {}

  ngOnInit(): void {
    const reservationIdentifier = this.activatedRoute.snapshot.paramMap.get('reservationIdentifier');
    this.reviewService.getReviewByReservationIdentifier(reservationIdentifier!).subscribe({
      next: (data) => this.review = data,
      error: (err) => console.error(err)
    });
  }

  updateReview() {
    const reservationIdentifier = this.activatedRoute.snapshot.paramMap.get('reservationIdentifier');
    const reviewUpdateRequest: ReviewUpdateRequest = {
      message: this.review.message,
      rating: this.review.rating
    };

    this.reviewService.updateReview(reservationIdentifier!, reviewUpdateRequest).subscribe({
      next: () => {
        this.router.navigate(['/reviews']);
      },
      error: (err) => console.error(err)
    });
  }
}
