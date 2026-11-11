import { Component, OnInit } from '@angular/core';
import { Review, ReviewService, ReviewUpdateRequest } from '../../services/review.service';
import { ActivatedRoute, Router } from '@angular/router';

import { FormsModule } from '@angular/forms';
import { StarRating } from "../star-rating/star-rating";

@Component({
  selector: 'app-review-edit',
  imports: [FormsModule, StarRating],
  templateUrl: './review-edit.html',
  styleUrl: './review-edit.scss'
})
export class ReviewEdit implements OnInit {

  review: Review = {
    userEmail: '',
    userName: '',
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
  ) { }

  ngOnInit(): void {
    const reservationIdentifier = this.activatedRoute.snapshot.paramMap.get('reservationIdentifier');
    this.reviewService.getReviewByReservationIdentifier(reservationIdentifier!).subscribe({
      next: (data) => this.review = data,
      error: () => this.router.navigate(['/error'])
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
      error: () => this.router.navigate(['/error'])
    });
  }

  cancelEdit() {
    this.router.navigate(['/reviews']);
  }
}
