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

  constructor(
    private readonly reservationService: ReservationService,
    private readonly authService: AuthService,
    private readonly requestService: RequestService
  ){}

  ngOnInit(): void {
    this.loadReservations(this.showCancelled);
  }

  loadReservations(showCancelled: boolean) {
    let filter: ReservationFilter = showCancelled ? 'CANCELLED' : 'NON_CANCELLED';
    if(this.authService.getRole() === 'ROLE_ADMIN'){
      this.reservationService.getReservations(filter).subscribe({
        next: (data) => {
          this.reservations = data;
          this.userAdmin = true;
        },
        error: (err) => console.error(err)
      });
    } else {
      const userId = this.authService.getUserId();
      if(userId) {
        this.reservationService.getReservationsByUserId(userId, filter).subscribe({
          next: (data) => {
            this.reservations = data;
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

  requestCancellation(reservationIdentifier: string) {
    const cancellationRequest: ModificationRequestCreation = {
      type: 'CANCELLATION',
      userEmail: this.authService.getUserEmail()!,
      reservationIdentifier: reservationIdentifier
    };

    this.requestService.createRequest(cancellationRequest).subscribe({
      error: (err) => console.error(err)
    });
  }

  alterView() {
    this.showCancelled = !this.showCancelled;
    this.loadReservations(this.showCancelled);
  }
}
