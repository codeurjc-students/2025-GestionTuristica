import { DialogService } from './../../services/dialog.service';
import { ReservationFilter } from './../../services/reservation.service';
import { RequestService, ModificationRequestCreation } from './../../services/request.service';
import { AuthService } from './../../services/auth.service';
import { Component, OnInit } from '@angular/core';
import { Reservation, ReservationService } from '../../services/reservation.service';
import { DatePipe, CommonModule } from '@angular/common';
import { Router, RouterLink } from "@angular/router";
import { MatButtonModule } from "@angular/material/button";

@Component({
  selector: 'app-reservation-list',
  standalone: true,
  imports: [DatePipe, RouterLink, MatButtonModule, CommonModule],
  templateUrl: './reservation-list.html',
  styleUrl: './reservation-list.scss'
})
export class ReservationList implements OnInit {

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
    private readonly requestService: RequestService,
    private readonly router: Router,
    private readonly dialogService: DialogService
  ) { }

  ngOnInit(): void {
    this.loadReservations(this.showCancelled, this.pageNumber);
  }

  loadReservations(showCancelled: boolean, page: number) {
    let filter: ReservationFilter = showCancelled ? 'CANCELLED' : 'NON_CANCELLED';
    if (this.authService.getRole() === 'ROLE_ADMIN') {
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
        error: () => this.router.navigate(['/error'])
      });
    } else {
      const userId = this.authService.getUserId();
      if (userId) {
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
          error: () => this.router.navigate(['/error'])
        });
      }
    }
  }

  cancelReservation(reservationIdentifier: string): void {
    this.dialogService.confirmationDialog('Cancelar reserva', '¿Está seguro/a de que quieres cancelar esta reserva? Esta acción no se puede deshacer', 'Aceptar', 'Cancelar')
      .subscribe(confirm => {
        if (confirm) {
          this.reservationService.cancelReservation(reservationIdentifier).subscribe({
            next: () => {
              this.dialogService.infoDialog('Éxito', 'Reserva cancelada con éxito.').subscribe(() =>
                this.reservations = this.reservations.filter(reservation => reservation.reservationIdentifier !== reservationIdentifier)
              )
            },
            error: (err) => this.dialogService.infoDialog('Error', 'No se ha podido cancelar la reserva. Inténtelo de nuevo más tarde.')
              .subscribe(() => this.router.navigate(['/error']))
          });
        }
      })
  }

  requestCancellation(reservation: Reservation) {
    this.dialogService.confirmationDialog('Cancelar reserva', '¿Está seguro/a de que quieres solicitar la cancelación de esta reserva? Esta acción no se puede deshacer', 'Solicitar', 'Cancelar')
      .subscribe(confirm => {
        if (confirm) {
          reservation.status = 'CANCELLATION_REQUESTED';

          const cancellationRequest: ModificationRequestCreation = {
            type: 'CANCELLATION',
            userEmail: this.authService.getUserEmail()!,
            reservationIdentifier: reservation.reservationIdentifier
          };

          this.requestService.createRequest(cancellationRequest).subscribe({
            next: () => {
              this.dialogService.infoDialog('Éxito', 'Se ha creado la solicitud de cancelación. Se le informará por correo si es aceptada.');
            },
            error: (err) => this.dialogService.infoDialog('Error', 'No se ha podido crear la solicitud de cancelación. Inténtelo de nuevo más tarde. En caso de que el problema persista puede solicitar la cancelación directamente a nuestro equipo.')
              .subscribe(() => this.router.navigate(['/']))
          });
        }
      })
  }

  alterView(cancelled: boolean) {
    this.showCancelled = cancelled;
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
      error: (err) => this.dialogService.infoDialog('Error', 'No se ha podido descargar el archivo pdf. Inténtelo de nuevo más tarde.')
    });
  }

  nextPage() {
    if (!this.lastPage) {
      this.pageNumber++;
      this.loadReservations(this.showCancelled, this.pageNumber);
    }
  }

  previousPage() {
    if (!this.firstPage) {
      this.pageNumber--;
      this.loadReservations(this.showCancelled, this.pageNumber);
    }
  }
}
