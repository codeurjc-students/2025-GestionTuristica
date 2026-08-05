
import { ReviewService, ReviewRequest } from './../../services/review.service';
import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from "@angular/forms";
import { StarRating } from "../star-rating/star-rating";

@Component({
  selector: 'app-review-create',
  imports: [FormsModule, StarRating],
  templateUrl: './review-create.html',
  styleUrl: './review-create.scss'
})
export class ReviewCreate {

  message: string = "";
  rating: number = 1;

  constructor(
    private readonly activatedRoute: ActivatedRoute,
    private readonly reviewService: ReviewService,
    private readonly router: Router
  ){}

  createReview() {
    const reservationIdentifier = this.activatedRoute.snapshot.paramMap.get('reservationIdentifier');
    const request: ReviewRequest = {
      message: this.message,
      reservationIdentifier: reservationIdentifier || '',
      rating: this.rating
    };
    this.reviewService.createReview(request).subscribe({
      next: () => {
        this.router.navigate(['/reviews']);
      },
      error: (error) => console.error(error)
    })
  }

  cancel() {
    this.router.navigate(['/reviews']);
  }
}
