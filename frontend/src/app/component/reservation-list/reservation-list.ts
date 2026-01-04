import { Component, OnInit } from '@angular/core';
import { Reservation, ReservationService } from '../../services/reservation.service';
import { DatePipe } from '@angular/common';
import { RouterLink } from "@angular/router";

@Component({
  selector: 'app-reservation-list',
  standalone: true,
  imports: [DatePipe, RouterLink],
  templateUrl: './reservation-list.html',
})
export class ReservationList implements OnInit{

  reservations: Reservation[] = [];

  constructor(private readonly reservationService: ReservationService){}

  ngOnInit(): void {
      this.reservationService.getReservations().subscribe({
        next: (data) => {
          this.reservations = data;
        },
        error: (err) => console.error(err)
      });
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

}
