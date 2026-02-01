import { Component, OnInit, ViewChild } from '@angular/core';
import { MatDatepickerModule, MatDateRangePicker } from '@angular/material/datepicker';
import { FormGroup, FormControl, ReactiveFormsModule } from '@angular/forms';
import { ReservationRequest, Reservation, ReservationService, ReservedRange } from '../../services/reservation.service';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatNativeDateModule } from '@angular/material/core';
import { ActivatedRoute, Router } from '@angular/router';
import { RoomService } from '../../services/room.service';

@Component({
  selector: 'app-reservation-edit',
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatNativeDateModule
  ],
  templateUrl: './reservation-edit.html'
})
export class ReservationEdit implements OnInit {
  @ViewChild('picker') picker!: MatDateRangePicker<Date>;
  reservationIdentifier!: string;
  reservation!: Reservation;
  roomId!: number;
  reservationStartDate?: Date;
  reservationEndDate?: Date;
  nights: number = 0;
  dateRange = new FormGroup({
    start: new FormControl<Date | null>(null),
    end: new FormControl<Date | null>(null)
  });
  actualReservedRange!: ReservedRange;
  minDate = new Date();
  reservedDates: ReservedRange[] = [];

  dateFilter = (date: Date | null): boolean => {
    if (!date) {
      return false;
    }

    const day = this.normalizeDate(date);

    const isReserved = this.reservedDates.some(range =>
      day >= range.startDate && day < range.endDate
    );

    return !isReserved;
  };

  constructor(
    private readonly route: ActivatedRoute,
    private readonly roomService: RoomService,
    private readonly reservationService: ReservationService,
    private readonly router: Router
  ){};

  ngOnInit(): void {
    this.reservationIdentifier = this.route.snapshot.paramMap.get('reservationIdentifier') as string;

    this.reservationService.getReservationByIdentifier(this.reservationIdentifier).subscribe({
      next: (data) => {
        this.reservation = data;
        this.roomId = this.reservation.roomId;
        this.actualReservedRange = {startDate: this.normalizeDate(new Date(this.reservation.startDate)), endDate: this.normalizeDate(new Date(this.reservation.endDate))}
        this.loadReservedDates();
      }
    });

    this.dateRange.valueChanges.subscribe(({start, end}) => {
      if(start && end) {
        if(this.checkOverlapedDates(this.normalizeDate(start), this.normalizeDate(end))) {
          alert('Las fechas seleccionadas contienen fechas ya reservadas. Por favor, elija otras fechas.');

          this.picker.close();
          setTimeout(() =>
                    this.dateRange.patchValue({start: null, end: null})
          );
          this.nights = 0;
          return;
        }

        this.calculateNights(start, end);
      }
    });
  }

  loadReservedDates() {
    this.reservationService.getReservedDates(this.roomId).subscribe({
      next: (data) => {
        this.reservedDates = data.map(range => ({
          startDate: this.normalizeDate(new Date(range.startDate)),
          endDate: this.normalizeDate(new Date(range.endDate))
        })).filter(range => !this.sameRange(range, this.actualReservedRange));
      }
    });
  }

  normalizeDate(date: Date): Date {
    const normalizedDate = new Date(date);
    normalizedDate.setHours(0, 0, 0, 0);
    return normalizedDate;
  }

  checkOverlapedDates(startDate: Date, endDate: Date): boolean {
    return this.reservedDates.some(range =>
      (startDate < range.endDate) && (endDate > range.startDate)
    );
  }

  dateToString(date: Date): string {
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  sameRange(range: ReservedRange, comparedRange: ReservedRange): boolean {
    return range.startDate.getTime() == comparedRange.startDate.getTime() && range.endDate.getTime() == comparedRange.endDate.getTime();
  }

  updateReservation() {
    const newStartDate = this.dateToString(this.dateRange.get('start')?.value!);
    const newEndDate = this.dateToString(this.dateRange.get('end')?.value!);
    const reservationRequest = {startDate: newStartDate, endDate: newEndDate} as Partial<ReservationRequest>;
    this.reservationService.updateReservation(this.reservation.reservationIdentifier, reservationRequest).subscribe({
      next: () => void this.router.navigate(['/']),
      error: (err) => console.error(err)
    })
  }

  private calculateNights(start: Date, end: Date) {
    if(start && end) {
      const diffMs = end.getTime() - start.getTime();
      this.nights = Math.floor(diffMs / (1000 * 60 * 60 * 24));
    }
  }

  cancel() {
    this.router.navigate(['/']);
  }
}
