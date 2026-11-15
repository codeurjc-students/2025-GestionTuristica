import { DialogService } from './../../services/dialog.service';
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
export class ReviewDetail implements OnInit {

  review: Review = {
    userName: '',
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
    private readonly activatedRoute: ActivatedRoute,
    private readonly dialogService: DialogService
  ) { };

  ngOnInit(): void {
    this.loadReview();
  }

  loadReview() {
    const reservationIdentifier = this.activatedRoute.snapshot.paramMap.get('reservationIdentifier');
    this.reviewService.getReviewByReservationIdentifier(reservationIdentifier!).subscribe({
      next: (data) => this.review = data,
      error: () => this.router.navigate(['/error'])
    })
  }

  updateReview() {
    const reservationIdentifier = this.activatedRoute.snapshot.paramMap.get('reservationIdentifier');
    this.router.navigate(['/reviews/edit', reservationIdentifier]);
  }

  deleteReview() {
    this.dialogService.confirmationDialog('Eliminar reseña', '¿Estás seguro/a de que quieres eliminar esta reseña? Esta acción no se puede deshacer', 'Eliminar', 'Cancelar')
      .subscribe(confirm => {
        if (confirm) {
          const reservationIdentifier = this.activatedRoute.snapshot.paramMap.get('reservationIdentifier');
          this.reviewService.deleteReview(reservationIdentifier!).subscribe({
            next: () => this.dialogService.infoDialog('Éxito', 'La reseña ha sido eliminada correctamente').subscribe(() => this.router.navigate(['/reviews'])),
            error: () => this.dialogService.infoDialog('Error', 'No se ha podido eliminar la reseña. Inténtalo de nuevo más tarde').subscribe(() => this.router.navigate(['/error']))
          });
        }
      })
  }

  cancel() {
    this.router.navigate(['/reviews']);
  }
}
