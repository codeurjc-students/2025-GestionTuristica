import { CommonModule } from '@angular/common';
import { ReviewService, ReviewRequest } from './../../services/review.service';
import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from "@angular/forms";

@Component({
  selector: 'app-review-create',
  imports: [CommonModule, FormsModule],
  templateUrl: './review-create.html'
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
