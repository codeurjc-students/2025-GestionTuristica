import { CommonModule } from "@angular/common";
import { Component, OnInit } from "@angular/core";
import { Hotel, HotelService } from "../services/hotel.service";
import { RouterLink } from "@angular/router";
import { AuthService } from "../services/auth.service";


@Component({
    selector: 'hotel-list',
    standalone: true,
    imports: [CommonModule, RouterLink],
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

    constructor(private readonly hotelService: HotelService, private readonly authService: AuthService){}

    ngOnInit() {
        this.adminUser = this.authService.getRole() === 'ROLE_ADMIN';

        this.loadHotels(this.pageNumber);
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
        this.hotelService.getHotels(page).subscribe({
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
}
