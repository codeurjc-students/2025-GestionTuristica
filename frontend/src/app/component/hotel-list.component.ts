import { CommonModule } from "@angular/common";
import { Component, OnInit } from "@angular/core";
import { Hotel, HotelService } from "../services/hotel.service";
import { RouterLink } from "@angular/router";
import { AuthService } from "../services/auth.service";


@Component({
    selector: 'hotel-list',
    standalone: true,
    imports: [CommonModule, RouterLink],
    templateUrl: './hotel-list.component.html'
})
export class HotelListComponent implements OnInit{

    hotels: Hotel[] = [];
    adminUser!: boolean;

    constructor(private readonly hotelService: HotelService, private readonly authService: AuthService){}

    ngOnInit() {
        this.adminUser = this.authService.getRole() === 'ROLE_ADMIN';

        this.hotelService.getHotels().subscribe({
            next: (data) => this.hotels = data,
            error: (err) => console.error(err)
        });
    }

    deleteHotel(slug: string) {
        this.hotelService.removeHotel(slug).subscribe({
            next: () => {
                this.hotels = this.hotels.filter(hotel => hotel.slug !== slug);
            },
            error: (err) => console.error(err)
        });
    }
}
