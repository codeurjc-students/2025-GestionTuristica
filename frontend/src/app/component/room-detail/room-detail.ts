import { Component, OnInit, ViewChild } from '@angular/core';
import { Room, RoomService } from '../../services/room.service';
import { ReservationService, ReservedRange } from '../../services/reservation.service';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDatepickerModule, MatDateRangePicker } from '@angular/material/datepicker';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatNativeDateModule } from '@angular/material/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';


@Component({
  selector: 'app-room-detail',
  imports: [
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatDatepickerModule,
    MatNativeDateModule
  ],
  templateUrl: './room-detail.html',
})
export class RoomDetail implements OnInit{
  @ViewChild('picker') picker!: MatDateRangePicker<Date>;
  roomId!: number;
  room!: Room;
  reservationStartDate?: Date;
  reservationEndDate?: Date;
  totalPrice?: number;
  nights: number = 0;
  dateRange = new FormGroup({
    start: new FormControl<Date | null>(null),
    end: new FormControl<Date | null>(null)
  });
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
    this.roomId = Number(this.route.snapshot.paramMap.get('roomId'));

    this.roomService.getRoomByRoomId(this.roomId).subscribe({
      next: (data) => {
        this.room = data;
      }
    });

    this.reservationService.getReservedDates(this.roomId).subscribe({
      next: (data) => {
        this.reservedDates = data.map(range => ({
          startDate: this.normalizeDate(new Date(range.startDate)),
          endDate: this.normalizeDate(new Date(range.endDate))
        }));
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
          this.totalPrice = undefined;
          this.nights = 0;
          return;
        }

        this.calculatePrice(start, end);
      }
    });
  }

  normalizeDate(date: Date): Date {
    const normalizedDate = new Date(date);
    normalizedDate.setHours(0, 0, 0, 0);
    return normalizedDate;
  }

  reserveRoom() {
    const startDate = this.dateToString(this.dateRange.get('start')?.value!);
    const endDate = this.dateToString(this.dateRange.get('end')?.value!);
    const reservationRequest = {roomId: this.roomId, startDate: startDate, endDate: endDate}
    this.reservationService.reserveRoom(this.roomId, reservationRequest).subscribe({
      next: () => void this.router.navigate(['/']),
      error: (err) => console.error(err)
    });
  }

  dateToString(date: Date): string {
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    return `${year}-${month}-${day}`;
  }

  private calculatePrice(start: Date, end: Date) {
    if(start && end) {
      const diffMs = end.getTime() - start.getTime();
      this.nights = Math.floor(diffMs / (1000 * 60 * 60 * 24));
      this.totalPrice = this.nights * this.room.price;
    }
  }

  checkOverlapedDates(startDate: Date, endDate: Date): boolean {
    return this.reservedDates.some(range =>
      (startDate < range.endDate) && (endDate > range.startDate)
    );
  }

  cancel() {
    this.router.navigate(['/']);
  }

}
