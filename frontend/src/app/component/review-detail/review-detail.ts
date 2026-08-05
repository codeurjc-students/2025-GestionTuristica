import { Component, OnInit } from '@angular/core';
import { Review, ReviewService } from '../../services/review.service';
import { ActivatedRoute, Router } from '@angular/router';
import { StarRating } from "../star-rating/star-rating";

@Component({
  selector: 'app-review-detail',
  templateUrl: './review-detail.html',
  styleUrl: './review-detail.scss',
  imports: [StarRating]
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
