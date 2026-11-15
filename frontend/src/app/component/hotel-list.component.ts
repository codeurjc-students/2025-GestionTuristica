import { DialogService } from './../services/dialog.service';

import { Component, OnInit } from "@angular/core";
import { Hotel, HotelFilters, HotelService } from "../services/hotel.service";
import { Router, RouterLink } from "@angular/router";
import { AuthService } from "../services/auth.service";
import { FormsModule } from "@angular/forms";
import { ChartData, ChartOptions } from "chart.js";
import { BaseChartDirective } from "ng2-charts";
import { ReservationService } from "../services/reservation.service";
import { StarRating } from "./star-rating/star-rating";

@Component({
    selector: 'hotel-list',
    standalone: true,
    imports: [RouterLink, FormsModule, BaseChartDirective, StarRating],
    templateUrl: './hotel-list.component.html',
    styleUrl: './hotel-list.component.scss'
})
export class HotelListComponent implements OnInit {

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

    constructor(private readonly hotelService: HotelService, private readonly authService: AuthService, private readonly reservationService: ReservationService, private readonly router: Router, private readonly dialogService: DialogService) { }

    ngOnInit() {
        this.adminUser = this.authService.getRole() === 'ROLE_ADMIN';

        this.loadHotels(this.pageNumber);

        this.loadHotelReservationsData();
    }

    deleteHotel(slug: string) {
        this.dialogService.confirmationDialog('Eliminar hotel', '¿Estás seguro/a que quieres eliminar este hotel? Esta acción no se puede deshacer.', 'Eliminar', 'Cancelar')
            .subscribe(
                confirm => {
                    if (confirm === true) {
                        this.hotelService.removeHotel(slug).subscribe({
                            next: () => {
                                this.hotels = this.hotels.filter(hotel => hotel.slug !== slug);
                                this.loadHotels(this.pageNumber);
                                this.dialogService.infoDialog('Éxito', 'Hotel eliminado correctamente')
                            },
                            error: () => this.dialogService.infoDialog('Error', 'Ha habido un error eliminando el hotel. Inténtalo de nuevo más tarde.')
                        });
                    }
                }
            )

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

                if (this.numberOfHotels === 0 && !this.firstPage) {
                    this.loadHotels(this.pageNumber - 1);
                }
            },
            error: () => this.router.navigate(['/error'])
        });
    }

    nextPage() {
        if (!this.lastPage) {
            this.pageNumber++;
            this.loadHotels(this.pageNumber);
        }
    }

    previousPage() {
        if (!this.firstPage) {
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
