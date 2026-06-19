import { CommonModule } from "@angular/common";
import { Component, OnInit } from "@angular/core";
import { Hotel, HotelFilters, HotelService } from "../services/hotel.service";
import { RouterLink } from "@angular/router";
import { AuthService } from "../services/auth.service";
import { FormsModule } from "@angular/forms";
import { ChartData, ChartOptions } from "chart.js";
import { BaseChartDirective } from "ng2-charts";
import { ReservationService } from "../services/reservation.service";

@Component({
    selector: 'hotel-list',
    standalone: true,
    imports: [CommonModule, RouterLink, FormsModule, BaseChartDirective],
    templateUrl: './hotel-list.component.html',
    styleUrl: './hotel-list.component.scss'
})
export class HotelListComponent implements OnInit{

    hotels: Hotel[] = [];
    adminUser!: boolean;
    pageNumber: number = 0;
    firstPage!: boolean;
    lastPage!: boolean;
    numberOfHotels!: number;
    numberOfPages!: number;

    filters: HotelFilters = {
        name: '',
        country: '',
        city: '',
        stars: undefined,
        rating: undefined
    };

    reservationsData: ChartData<'pie', number[], string | string[]> = {
                    labels: [],
                    datasets: [
                        {
                            data: []
                        }
                    ]
                };

    reservationOptions: ChartOptions<'pie'> = {
        plugins: {
            title: {
                display: true,
                text: 'Hoteles más reservados'
            },
            legend: {
                display: true,
                position: 'bottom'
            }
        }
    }

    constructor(private readonly hotelService: HotelService, private readonly authService: AuthService, private readonly reservationService: ReservationService){}

    ngOnInit() {
        this.adminUser = this.authService.getRole() === 'ROLE_ADMIN';

        this.loadHotels(this.pageNumber);

        this.loadHotelReservationsData();
    }

    deleteHotel(slug: string) {
        this.hotelService.removeHotel(slug).subscribe({
            next: () => {
                this.hotels = this.hotels.filter(hotel => hotel.slug !== slug);
                this.loadHotels(this.pageNumber);
            },
            error: (err) => console.error(err)
        });
    }

    loadHotels(page: number) {
        this.hotelService.getHotels(page, this.filters).subscribe({
            next: (data) => {
                this.hotels = data.content;
                this.pageNumber = data.number;
                this.firstPage = data.first;
                this.lastPage = data.last;
                this.numberOfHotels = data.totalElements;
                this.numberOfPages = data.totalPages;

                if(this.numberOfHotels === 0 && !this.firstPage) {
                    this.loadHotels(this.pageNumber - 1);
                }
            },
            error: (err) => console.error(err)
        });
    }

    nextPage() {
        if(!this.lastPage) {
            this.pageNumber++;
            this.loadHotels(this.pageNumber);
        }
    }

    previousPage() {
        if(!this.firstPage) {
            this.pageNumber--;
            this.loadHotels(this.pageNumber);
        }
    }

    searchByFilters() {
        this.pageNumber = 0;
        this.loadHotels(this.pageNumber);
    }

    resetFilters() {
        this.filters = {};
        this.pageNumber = 0;
        this.loadHotels(this.pageNumber);
    }

    loadHotelReservationsData() {
        this.reservationService.getMostReservedHotels().subscribe(
            (data) => {
                this.reservationsData = {
                    labels: data.map(item => item.hotel),
                    datasets: [
                        {
                            data: data.map(item => item.reservations)
                        }
                    ]
                };
            });
    }
}
