import { ReservationFilter } from './../../services/reservation.service';
import { RequestService, ModificationRequestCreation } from './../../services/request.service';
import { AuthService } from './../../services/auth.service';
import { Component, OnInit } from '@angular/core';
import { Reservation, ReservationService } from '../../services/reservation.service';
import { DatePipe, CommonModule } from '@angular/common';
import { RouterLink } from "@angular/router";
import { MatButtonModule } from "@angular/material/button";

@Component({
  selector: 'app-reservation-list',
  standalone: true,
  imports: [DatePipe, RouterLink, MatButtonModule, CommonModule],
  templateUrl: './reservation-list.html',
})
export class ReservationList implements OnInit{

  reservations: Reservation[] = [];
  userAdmin!: boolean;
  showCancelled: boolean = false;
  pageNumber: number = 0;
  firstPage!: boolean;
  lastPage!: boolean;
  numberOfReservations!: number;
  numberOfPages!: number;

  constructor(
    private readonly reservationService: ReservationService,
    private readonly authService: AuthService,
    private readonly requestService: RequestService
  ){}

  ngOnInit(): void {
    this.loadReservations(this.showCancelled, this.pageNumber);
  }

  loadReservations(showCancelled: boolean, page: number) {
    let filter: ReservationFilter = showCancelled ? 'CANCELLED' : 'NON_CANCELLED';
    if(this.authService.getRole() === 'ROLE_ADMIN'){
      this.reservationService.getReservations(filter, page).subscribe({
        next: (data) => {
          this.reservations = data.content;
          this.pageNumber = data.number;
          this.firstPage = data.first;
          this.lastPage = data.last;
          this.numberOfReservations = data.totalElements;
          this.numberOfPages = data.totalPages;
          this.userAdmin = true;
        },
        error: (err) => console.error(err)
      });
    } else {
      const userId = this.authService.getUserId();
      if(userId) {
        this.reservationService.getReservationsByUserId(userId, filter, page).subscribe({
          next: (data) => {
            this.reservations = data.content;
            this.pageNumber = data.number;
            this.firstPage = data.first;
            this.lastPage = data.last;
            this.numberOfReservations = data.totalElements;
            this.numberOfPages = data.totalPages;
            this.userAdmin = false;
          },
          error: (err) => console.error(err)
        });
      }
    }
  }

  cancelReservation(reservationIdentifier: string): void {
    const confirmed = globalThis.confirm('¿Confirmar cancelación de reserva?');

    if(!confirmed) {
      return;
    }

    this.reservationService.cancelReservation(reservationIdentifier).subscribe({
      next: () => {
        this.reservations = this.reservations.filter(reservation => reservation.reservationIdentifier !== reservationIdentifier);
      },
      error: (err) => console.error(err)
    });
  }

  requestCancellation(reservation: Reservation) {
    
    reservation.status = 'CANCELLATION_REQUESTED';

    const cancellationRequest: ModificationRequestCreation = {
      type: 'CANCELLATION',
      userEmail: this.authService.getUserEmail()!,
      reservationIdentifier: reservation.reservationIdentifier
    };

    this.requestService.createRequest(cancellationRequest).subscribe({
      error: (err) => console.error(err)
    });
  }

  alterView() {
    this.showCancelled = !this.showCancelled;
    this.loadReservations(this.showCancelled, this.pageNumber);
  }

  downloadPdf(reservationIdentifier: string) {
    this.reservationService.downloadReservationPdf(reservationIdentifier).subscribe({
      next: (blob): void => {
        const url = globalThis.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = reservationIdentifier + '.pdf';
        a.click();

        globalThis.URL.revokeObjectURL(url);
      },
      error: (err) => console.error(err)
    });
  }

  nextPage() {
    if(!this.lastPage) {
      this.pageNumber++;
      this.loadReservations(this.showCancelled, this.pageNumber);
    }
  }

  previousPage() {
    if(!this.firstPage) {
      this.pageNumber--;
      this.loadReservations(this.showCancelled, this.pageNumber);
    }
  }
}
